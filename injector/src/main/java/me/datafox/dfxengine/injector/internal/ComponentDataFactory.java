package me.datafox.dfxengine.injector.internal;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodParameterInfo;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.TypeRef;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.FinalFieldDependencyException;
import me.datafox.dfxengine.injector.exception.InvalidArrayException;
import me.datafox.dfxengine.injector.exception.UnresolvedOrUnknownTypeException;
import me.datafox.dfxengine.injector.utils.InjectorStrings;
import me.datafox.dfxengine.injector.utils.InjectorUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Factory that builds {@link ComponentData} instances and by extension {@link ClassReference} and {@link TypeRef}
 * instances.
 *
 * @author datafox
 */
public class ComponentDataFactory {
    private final Logger logger;

    private final Map<String,ClassInfo> classInfoMap;

    private final Set<String> visited;

    private final Map<String, ClassReference<?>> originals;

    private final Map<String, SelfReference<?>> references;

    public ComponentDataFactory(Map<String,ClassInfo> classInfoMap) {
        logger = LoggerFactory.getLogger(getClass());
        this.classInfoMap = classInfoMap;
        visited = new HashSet<>();
        originals = new HashMap<>();
        references = new HashMap<>();
    }

    /**
     * @param info constructor or method of a component
     * @return {@link ComponentData} for the specified constructor or method
     * @param <T> type of the component
     */
    public <T> ComponentData<T> buildComponentData(MethodInfo info) {
        Component annotation = null;
        ComponentData.ComponentDataBuilder<T> builder = ComponentData.builder();
        if(info.isConstructor()) {
            String classString = info.getClassInfo().getName();
            logger.debug(InjectorStrings.buildingComponentClassData(classString, info));
            ClassReference<T> reference = buildClassReferenceEntry(parseClass(info.getClassInfo()));
            builder.reference(reference).executable(info.loadClassAndGetConstructor());
            if(info.getClassInfo().hasAnnotation(Component.class)) {
                annotation = (Component) info.getClassInfo().getAnnotationInfo(Component.class).loadClassAndInstantiate();
            }
            for(FieldInfo field : info.getClassInfo().getDeclaredFieldInfo().filter(f -> f.hasAnnotation(Inject.class))) {
                if(field.isFinal()) {
                    throw LogUtils.logExceptionAndGet(logger,
                            InjectorStrings.finalFieldDependency(field),
                            FinalFieldDependencyException::new);
                }
                if(InstantiationPolicy.class.getName().equals(field.getTypeDescriptor().toString())) {
                    continue;
                }
                builder.field(FieldReference
                        .builder()
                        .field(field.loadClassAndGetField())
                        .reference(buildClassReferenceEntry(field.getTypeSignatureOrTypeDescriptor().toString()))
                        .build());
            }
            for(MethodInfo method : info.getClassInfo().getDeclaredMethodInfo().filter(m -> m.hasAnnotation(Initialize.class))) {
                Initialize initialize = (Initialize) method.getAnnotationInfo(Initialize.class).loadClassAndInstantiate();
                InitializeReference.InitializeReferenceBuilder<T> initBuilder = InitializeReference
                        .<T>builder()
                        .priority(initialize.value())
                        .reference(reference)
                        .method(method.loadClassAndGetMethod());
                for(MethodParameterInfo param : method.getParameterInfo()) {
                    if(InstantiationPolicy.class.getName().equals(param.getTypeDescriptor().toString())) {
                        continue;
                    }
                    initBuilder.parameter(buildClassReferenceEntry(param.getTypeSignatureOrTypeDescriptor().toString()));
                }
                builder.initializer(initBuilder.build());
            }
        } else {
            String classString = info.getClassName();
            logger.debug(InjectorStrings.buildingComponentMethodData(classString, info));
            builder.reference(buildClassReferenceEntry(parseMethod(info)))
                    .executable(info.loadClassAndGetMethod());
            if(!info.isStatic()) {
                builder.owner(buildClassReferenceEntry(parseClass(info.getClassInfo())));
            }
            if(info.hasAnnotation(Component.class)) {
                annotation = (Component) info.getAnnotationInfo(Component.class).loadClassAndInstantiate();
            }
        }
        if(annotation != null) {
            builder.policy(annotation.value()).order(annotation.order());
        } else {
            builder.policy(InstantiationPolicy.ONCE).order(0);
        }
        for(MethodParameterInfo param : info.getParameterInfo()) {
            if(InstantiationPolicy.class.getName().equals(param.getTypeDescriptor().toString())) {
                continue;
            }
            builder.parameter(buildClassReferenceEntry(param.getTypeSignatureOrTypeDescriptor().toString()));
        }
        return builder.build();
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

    @SuppressWarnings("unchecked")
    private <T> ClassReference<T> buildClassReference(String str) {
        if(Object.class.getName().equals(str)) {
            return (ClassReference<T>) ClassReference.object();
        }
        if(visited.contains(str)) {
            if(references.containsKey(str)) {
                return (SelfReference<T>) references.get(str);
            }
            SelfReference<T> ref = new SelfReference<>();
            references.put(str, ref);
            return ref;
        }
        visited.add(str);
        String original = str;
        String className = getBoxedPrimitiveName(str);
        if(className != null) {
            ClassReference<T> ref = buildClassReference(parseClass(classInfoMap.get(className)));
            originals.put(original, ref);
            return ref;
        }
        if(str.endsWith("[]")) {
            ClassReference<T> ref = buildArrayClassReference(str);
            originals.put(original, ref);
            return ref;
        }
        ClassReference.ClassReferenceBuilder<T> builder = ClassReference.builder();
        if(classInfoMap.containsKey(str)) {
            ClassReference<T> ref = builder.type((Class<T>) classInfoMap.get(str).loadClass()).build();
            originals.put(original, ref);
            return ref;
        }
        if(classInfoMap.get(str.split("[< ]", 2)[0]).isInterface()) {
            if(InjectorUtils.removeTypes(str).contains(" extends ")) {
                builder.interfaces(parseExtendsInterfaces(str).stream().map(this::buildClassReference).collect(Collectors.toList()));
                str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[0];
            }
        } else {
            if(str.contains(" implements ")) {
                builder.interfaces(parseInterfaces(str).stream().map(this::buildClassReference).collect(Collectors.toList()));
                str = str.split(" implements ", 2)[0];
            }
            if(InjectorUtils.removeTypes(str).contains(" extends ")) {
                builder.superclass(buildClassReference(parseSuperclass(str)));
                str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[0];
            }
        }
        if(str.contains("<")) {
            builder.parameters(parseParameters(str).stream().map(this::buildClassReference).collect(Collectors.toList()));
            str = str.split("<", 2)[0];
        }
        if(!classInfoMap.containsKey(str)) {
            throw new UnresolvedOrUnknownTypeException(str);
        }
        ClassReference<T> ref = builder.type((Class<T>) classInfoMap.get(str).loadClass()).build();
        originals.put(original, ref);
        return ref;
    }

    private List<String> parseInterfaces(String str) {
        str = str.split(" implements ", 2)[1];
        List<String> list = InjectorUtils.splitParameters(str);
        return list.stream().filter(this::filterUnknownInterfaceParameters)
                .map(this::toFullSignature).collect(Collectors.toList());
    }

    private List<String> parseExtendsInterfaces(String str) {
        str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[1];
        List<String> list = InjectorUtils.splitParameters(str);
        return list.stream().filter(this::filterUnknownInterfaceParameters)
                .map(this::toFullSignature).collect(Collectors.toList());
    }

    private String parseSuperclass(String str) {
        str = InjectorUtils.splitWithoutTypes(str, " extends ", 2)[1];
        return toFullSignature(str);
    }

    private List<String> parseParameters(String str) {
        str = str.split("<", 2)[1];
        str = str.substring(0, str.length() - 1);
        List<String> list = InjectorUtils.splitParameters(str);
        return list.stream().map(this::toFullSignature).collect(Collectors.toList());
    }

    private String toFullSignature(String data) {
        if(data.endsWith("[]")) {
            return data;
        }
        if(data.startsWith("?")) {
            if(data.startsWith("? extends ")) {
                data = data.substring(10);
            } else {
                return Object.class.getName();
            }
        }
        if(classInfoMap.containsKey(data)) {
            return parseClass(classInfoMap.get(data));
        }
        String[] split = data.split("<", 2);
        if(split.length != 2) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.unresolvedTypeParameter(data),
                    UnresolvedOrUnknownTypeException::new);
        }
        List<String> parameters = InjectorUtils.splitParameters(split[1]);
        String classString = parseClass(classInfoMap.get(split[0]));
        List<String> genericParameters = InjectorUtils.splitParameters(classString.split("<", 2)[1]);
        List<String> simpleGenericParameters = genericParameters.stream().map(s -> s.split(" ", 2)[0]).collect(Collectors.toList());
        for(int i = 0; i < parameters.size(); i++) {
            classString = classString.replaceAll(Pattern.quote("<" + genericParameters.get(i)), "<" + parameters.get(i));
            classString = classString.replaceAll(Pattern.quote(", " + genericParameters.get(i)), ", " + parameters.get(i));
            classString = classString.replaceAll(Pattern.quote("<" + simpleGenericParameters.get(i)), "<" + parameters.get(i));
            classString = classString.replaceAll(Pattern.quote(", " + simpleGenericParameters.get(i)), ", " + parameters.get(i));
        }
        return classString;
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
        if(classInfoMap.containsKey(str)) {
            return ClassReference
                    .<T>builder()
                    .type((Class<T>) Array.class)
                    .parameter(buildClassReference(parseClass(classInfoMap.get(str))))
                    .build();
        }
        throw LogUtils.logExceptionAndGet(logger,
                InjectorStrings.invalidArray(str),
                InvalidArrayException::new);
    }

    private String parseClass(ClassInfo info) {
        return info.getTypeSignatureOrTypeDescriptor()
                .toString()
                .split(" class | interface ", 2)[1];
    }

    private String parseMethod(MethodInfo info) {
        return info.getTypeSignatureOrTypeDescriptor()
                .toString()
                .split(" \\(", 2)[0];
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

    private <T> void checkAndSetCollection(ClassReference<T> data) {
        if(List.class.equals(data.getType())) {
            data.setList(true);
            data.setListReference(data.getParameters().get(0));
        }
    }

    private boolean filterUnknownInterfaceParameters(String s) {
        return !classInfoMap.containsKey(s) || classInfoMap.get(s).getTypeSignatureOrTypeDescriptor().getTypeParameters().isEmpty();
    }
}