package me.datafox.dfxengine.injector.internal;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.TypeRef;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.EventParameterCountException;
import me.datafox.dfxengine.injector.exception.FinalFieldDependencyException;
import me.datafox.dfxengine.injector.exception.InvalidArrayException;
import me.datafox.dfxengine.injector.exception.UnresolvedOrUnknownTypeException;
import me.datafox.dfxengine.injector.serialization.ClassData;
import me.datafox.dfxengine.injector.serialization.ExecutableData;
import me.datafox.dfxengine.injector.serialization.FieldData;
import me.datafox.dfxengine.injector.utils.InjectorStrings;
import me.datafox.dfxengine.injector.utils.InjectorUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Factory that builds {@link ComponentData} instances and by extension {@link ClassReference}, {@link FieldReference},
 * {@link InitializeReference} and {@link SelfReference} instances.
 *
 * @author datafox
 */
public class ComponentDataFactory {
    private final Logger logger;

    private final Map<String,ClassData<?>> classDataMap;

    private final Set<String> visited;

    private final Map<String, ClassReference<?>> originals;

    private final Map<String, SelfReference<?>> references;

    public ComponentDataFactory(Map<String, ClassData<?>> classDataMap) {
        logger = LoggerFactory.getLogger(getClass());
        this.classDataMap = classDataMap;
        visited = new HashSet<>();
        originals = new HashMap<>();
        references = new HashMap<>();
    }

    /**
     * @param data constructor or method of a component
     * @return {@link ComponentData} for the specified constructor or method
     * @param <T> type of the component
     */
    public <T> ComponentData<T> buildComponentData(ExecutableData data) {
        Component annotation = null;
        boolean voidType = false;
        ComponentData.ComponentDataBuilder<T> builder = ComponentData.builder();
        Executable executable = data.getExecutable();
        if(executable instanceof Constructor) {
            //Create class component
            logger.debug(InjectorStrings.buildingComponentClassData(data.getSignature(), data));
            ClassReference<T> reference = buildClassReferenceEntry(data.getSignature());
            builder.reference(reference).executable(executable);
            if(data.getOwner().isAnnotationPresent(Component.class)) {
                annotation = data.getOwner().getAnnotation(Component.class);
            }
            //Register fields with @Inject annotation
            for(Field field : data.getOwner().getDeclaredFields()) {
                if(!field.isAnnotationPresent(Inject.class)) {
                    continue;
                }
                FieldData<?> fieldData = data.getFields().get(field.getName());
                if(Modifier.isFinal(field.getModifiers())) {
                    throw LogUtils.logExceptionAndGet(logger,
                            InjectorStrings.finalFieldDependency(fieldData),
                            FinalFieldDependencyException::new);
                }
                if(InstantiationPolicy.class.equals(field.getType())) {
                    continue;
                }
                builder.field(FieldReference
                        .builder()
                        .field(field)
                        .reference(buildClassReferenceEntry(fieldData.getSignature()))
                        .build());
            }
            //Register methods with @Initialize annotation
            for(Method method : data.getOwner().getDeclaredMethods()) {
                if(!method.isAnnotationPresent(Initialize.class)) {
                    continue;
                }
                Initialize initialize = method.getAnnotation(Initialize.class);
                InitializeReference.InitializeReferenceBuilder<T> initBuilder = InitializeReference
                        .<T>builder()
                        .priority(initialize.value())
                        .reference(reference)
                        .method(method);
                ExecutableData methodData = data.getMethods().get(method.getName());
                for(String signature : methodData.getParameterSignatures()) {
                    ClassReference<?> paramRef = buildClassReferenceEntry(signature);
                    if(InstantiationPolicy.class.equals(paramRef.getType())) {
                        continue;
                    }
                    initBuilder.parameter(paramRef);
                }
                builder.initializer(initBuilder.build());
            }
        } else {
            //Create method component
            if("void".equals(data.getSignature())) {
                logger.debug(InjectorStrings.buildingVoidComponentData(data));
                voidType = true;
            } else {
                logger.debug(InjectorStrings.buildingComponentMethodData(data.getSignature(), data));
                builder.reference(buildClassReferenceEntry(data.getSignature()));
            }
            builder.executable(data.getExecutable());
            if(!Modifier.isStatic(executable.getModifiers())) {
                builder.owner(buildClassReferenceEntry(classDataMap.get(data.getOwner().getName()).getSignature()));
            }
            if(executable.isAnnotationPresent(Component.class)) {
                annotation = executable.getAnnotation(Component.class);
            }
        }
        if(annotation != null) {
            builder.policy(annotation.value()).order(annotation.order());
            if(annotation.value().equals(InstantiationPolicy.PER_INSTANCE)) {
                if(voidType) {
                    logger.warn(InjectorStrings.perInstanceVoidComponent(data));
                }
                if(Arrays.stream(data.getOwner().getDeclaredMethods()).anyMatch(m -> m.isAnnotationPresent(EventHandler.class))) {
                    logger.warn(InjectorStrings.perInstanceComponentEvent(data));
                }
            }
        } else {
            builder.policy(InstantiationPolicy.ONCE).order(0);
        }
        //Register dependencies (constructor/method parameters)
        for(String signature : data.getParameterSignatures()) {
            ClassReference<?> paramRef = buildClassReferenceEntry(signature);
            if(InstantiationPolicy.class.equals(paramRef.getType())) {
                continue;
            }
            builder.parameter(paramRef);
        }
        return builder.build();
    }

    public <T> EventData<T> buildEventData(ExecutableData method) {
        if(method.getParameters().length != 1) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.eventParameterCount(method, method.getParameters().length),
                    EventParameterCountException::new);
        }

        EventData.EventDataBuilder<T> builder = EventData
                .<T>builder()
                .event(buildClassReferenceEntry(method.getParameterSignatures().get(0)));

        if(!Modifier.isStatic(method.getExecutable().getModifiers())) {
            builder.owner(buildClassReferenceEntry(classDataMap.get(method.getOwner().getName()).getSignature()));
        }

        return builder.method((Method) method.getExecutable()).build();
    }

    public <T> ClassReference<T> buildClasReferenceFromTypeRef(TypeRef<T> typeRef) {
        if(typeRef == null) {
            return null;
        }
        String str = typeRef.toString();
        if(str.startsWith("[")) {
            str = getArrayFromInternal(str);
        }
        return buildClassReferenceEntry(toFullSignature(str));
    }

    private <T> ClassReference<T> buildClassReferenceEntry(String str) {
        visited.clear();
        originals.clear();
        references.clear();
        ClassReference<T> reference = buildClassReference(str);
        checkAndSetCollection(reference);
        references.forEach(this::setReferences);
        return reference;
    }

    @SuppressWarnings("unchecked")
    private <T> void setReferences(String key, SelfReference<T> reference) {
        reference.setReference((ClassReference<T>) originals.get(key));
    }

    /**
     * @param str class string in the format {@code com.package.Class<com.package.Parameter, com.package.OtherParameter>
     * extends com.package.Superclass implements com.package.Interface, com.package.OtherInterface}
     * @return {@link ClassReference} for specified class string
     * @param <T> type of the class
     */
    @SuppressWarnings("unchecked")
    private <T> ClassReference<T> buildClassReference(String str) {
        boolean sup = str.startsWith("!");
        String str1 = str;
        if(sup) {
            str = str.substring(1);
        }
        //ClassGraph does not create ClassInfo for Object.class
        if(Object.class.getName().equals(str1)) {
            return (ClassReference<T>) ClassReference.object();
        }
        /*
        If this class reference has already been created, return a SelfReference instead. This is to prevent cyclic
        ClassReference graphs with classes where the type parameter of a superclass/interface is the class itself.
        */
        if(visited.contains(str1)) {
            if(references.containsKey(str1)) {
                return (SelfReference<T>) references.get(str1);
            }
            SelfReference<T> ref = new SelfReference<>();
            references.put(str1, ref);
            return ref;
        }
        visited.add(str1);
        String original = str;
        //If the class is a primitive, create a reference to the boxed version
        String className = getBoxedPrimitiveName(str);
        if(className != null) {
            ClassReference<T> ref = buildClassReference(classDataMap.get(className).getSignature());
            originals.put(original, ref);
            return ref;
        }
        //If the class is an array, create a reference to it
        if(str.endsWith("[]")) {
            ClassReference<T> ref = buildArrayClassReference(str);
            originals.put(original, ref);
            return ref;
        }
        ClassReference.ClassReferenceBuilder<T> builder = ClassReference.<T>builder().sup(sup);
        //If the class does not have type parameters, superclasses or interfaces, create a reference to it.
        if(classDataMap.containsKey(str)) {
            ClassReference<T> ref = builder.type((Class<T>) classDataMap.get(str).getType()).build();
            originals.put(original, ref);
            return ref;
        }
        //If the class is an interface and extends other interfaces, parse them
        if(classDataMap.get(str.split("[< ]", 2)[0]).getType().isInterface()) {
            if(InjectorUtils.removeTypes(str).contains(" extends ")) {
                builder.interfaces(parseExtendsInterfaces(str).stream().map(this::buildClassReference).collect(Collectors.toList()));
                str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[0];
            }
        } else {
            //If the class is not an interface but implements interfaces, parse them
            if(str.contains(" implements ")) {
                builder.interfaces(parseInterfaces(str).stream().map(this::buildClassReference).collect(Collectors.toList()));
                str = str.split(" implements ", 2)[0];
            }
            //If the class has a superclass, parse it
            if(InjectorUtils.removeTypes(str).contains(" extends ")) {
                builder.superclass(buildClassReference(parseSuperclass(str)));
                str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[0];
            }
        }
        //If the class has type parameters, parse them
        if(str.contains("<")) {
            builder.parameters(parseParameters(str).stream().map(this::buildClassReference).collect(Collectors.toList()));
            str = str.split("<", 2)[0];
        }
        //If the class does not exist, throw an exception
        if(!classDataMap.containsKey(str)) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.unknownType(str),
                    UnresolvedOrUnknownTypeException::new);
        }
        ClassReference<T> ref = builder.type((Class<T>) classDataMap.get(str).getType()).build();
        originals.put(original, ref);
        return ref;
    }

    private List<String> parseInterfaces(String str) {
        str = str.split(" implements ", 2)[1];
        List<String> list = InjectorUtils.splitParameters(str);
        return list.stream()
                .filter(this::filterUnknownInterfaceParameters)
                .map(this::toFullSignature)
                .collect(Collectors.toList());
    }

    private List<String> parseExtendsInterfaces(String str) {
        str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[1];
        List<String> list = InjectorUtils.splitParameters(str);
        return list.stream()
                .filter(this::filterUnknownInterfaceParameters)
                .map(this::toFullSignature)
                .collect(Collectors.toList());
    }

    private String parseSuperclass(String str) {
        str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[1];
        return toFullSignature(str);
    }

    private List<String> parseParameters(String str) {
        str = str.split("<", 2)[1];
        str = str.substring(0, str.length() - 1);
        List<String> list = InjectorUtils.splitParameters(str);
        return list.stream()
                .map(this::toFullSignature)
                .collect(Collectors.toList());
    }

    /**
     * @param str class string with type parameters but without superclasses/interfaces
     * @return class string in the format specified by {@link #buildClassReference(String)}
     */
    private String toFullSignature(String str) {
        if(str.startsWith("[")) {
            str = getArrayFromInternal(str);
        }
        //If the class is an array, do nothing
        if(str.endsWith("[]")) {
            return str;
        }
        String sup = "";
        //Resolve vague type parameters ("?" and "? super T" become java.lang.Object, "? extends T" becomes "T")
        if(str.startsWith("?")) {
            if(str.startsWith("? extends ")) {
                str = str.substring(10);
            } else if(str.startsWith("? super ")) {
                str = str.substring(8);
                sup = "!";
            } else {
                return Object.class.getName();
            }
        }
        if(Object.class.getName().equals(str)) {
            return str;
        }
        //If type parameters are not present, use parseClass(String)
        if(classDataMap.containsKey(str)) {
            return sup + classDataMap.get(str).getSignature();
        }
        String[] split = str.split("<", 2);
        /*
        If the split only has one element, the class does not have parameters but did not get recognised in the previous
        step, it could not be found
         */
        if(split.length != 2) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.unresolvedTypeParameter(str),
                    UnresolvedOrUnknownTypeException::new);
        }
        //Replace generic type parameters in superclasses and interfaces with the child class's resolved parameters
        List<String> parameters = InjectorUtils.splitParameters(split[1]);
        String classString = classDataMap.get(split[0]).getSignature();
        List<String> genericParameters = InjectorUtils.splitParameters(classString.split("<", 2)[1]);
        List<String> simpleGenericParameters = genericParameters.stream().map(s -> s.split(" ", 2)[0]).collect(Collectors.toList());
        for(int i = 0; i < parameters.size(); i++) {
            classString = classString.replaceAll(Pattern.quote("<" + genericParameters.get(i) + ">"), "<" + InjectorUtils.escapeCapture(parameters.get(i)) + ">");
            classString = classString.replaceAll(Pattern.quote("<" + genericParameters.get(i) + ", "), "<" + InjectorUtils.escapeCapture(parameters.get(i)) + ", ");
            classString = classString.replaceAll(Pattern.quote(", " + genericParameters.get(i) + ", "), ", " + InjectorUtils.escapeCapture(parameters.get(i)) + ", ");
            classString = classString.replaceAll(Pattern.quote(", " + genericParameters.get(i) + ">"), ", " + InjectorUtils.escapeCapture(parameters.get(i)) + ">");
            classString = classString.replaceAll(Pattern.quote("<" + simpleGenericParameters.get(i) + ">"), "<" + InjectorUtils.escapeCapture(parameters.get(i)) + ">");
            classString = classString.replaceAll(Pattern.quote("<" + simpleGenericParameters.get(i) + ", "), "<" + InjectorUtils.escapeCapture(parameters.get(i)) + ", ");
            classString = classString.replaceAll(Pattern.quote(", " + simpleGenericParameters.get(i) + ", "), ", " + InjectorUtils.escapeCapture(parameters.get(i)) + ", ");
            classString = classString.replaceAll(Pattern.quote(", " + simpleGenericParameters.get(i) + ">"), ", " + InjectorUtils.escapeCapture(parameters.get(i)) + ">");
        }
        return sup + classString;
    }

    @SuppressWarnings("unchecked")
    private <T> ClassReference<T> buildArrayClassReference(String str) {
        if(str.endsWith("[]")) {
            str = str.substring(0, str.length() - 2);
        }
        Class<T> type = (Class<T>) getPrimitiveArray(str);
        if(type != null) {
            return ClassReference
                    .<T>builder()
                    .type(type)
                    .build();
        }
        if(classDataMap.containsKey(str)) {
            ClassReference<?> ref = buildClassReference(classDataMap.get(str).getSignature());
            return ClassReference
                    .<T>builder()
                    .type((Class<T>) Array.newInstance(ref.getType(), 0).getClass())
                    .parameters(ref.getParameters())
                    .superclass((ClassReference<? super T>) ref.getSuperclass())
                    .interfaces((Collection<? extends ClassReference<? super T>>) (Collection<?>) ref.getInterfaces())
                    .build();
        }
        throw LogUtils.logExceptionAndGet(logger,
                InjectorStrings.invalidArray(str),
                InvalidArrayException::new);
    }

    public String getBoxedPrimitiveName(String className) {
        switch(className) {
            case "boolean":
                return Boolean.class.getName();
            case "byte":
                return Byte.class.getName();
            case "short":
                return Short.class.getName();
            case "int":
                return Integer.class.getName();
            case "long":
                return Long.class.getName();
            case "float":
                return Float.class.getName();
            case "double":
                return Double.class.getName();
            case "char":
                return Character.class.getName();
            default:
                return null;
        }
    }

    private static Class<?> getPrimitiveArray(String parameter) {
        switch(parameter) {
            case "boolean":
                return boolean[].class;
            case "byte":
                return byte[].class;
            case "short":
                return short[].class;
            case "int":
                return int[].class;
            case "long":
                return long[].class;
            case "float":
                return float[].class;
            case "double":
                return double[].class;
            case "char":
                return char[].class;
            default:
                return null;
        }
    }

    private static String getArrayFromInternal(String parameter) {
        if(!parameter.startsWith("[")) {
            return parameter;
        }
        switch(parameter) {
            case "[Z":
                return "boolean[]";
            case "[B":
                return "byte[]";
            case "[S":
                return "short[]";
            case "[I":
                return "int[]";
            case "[J":
                return "long[]";
            case "[F":
                return "float[]";
            case "[D":
                return "double[]";
            case "[C":
                return "char[]";
            default:
                return parameter.substring(2, parameter.length() - 1) + "[]";
        }
    }

    private <T> void checkAndSetCollection(ClassReference<T> data) {
        if(List.class.equals(data.getType())) {
            data.setList(true);
            data.setListReference(data.getParameters().get(0));
        }
    }

    /**
     * @param str class string for an interface
     * @return {@code true} if the interface has type parameters present in the string or does not have type parameters
     * at all, or {@code false} if it has type parameters that are not present in the string
     */
    private boolean filterUnknownInterfaceParameters(String str) {
        return !classDataMap.containsKey(str) ||
                classDataMap.get(str).getType().getTypeParameters().length == 0;
    }
}