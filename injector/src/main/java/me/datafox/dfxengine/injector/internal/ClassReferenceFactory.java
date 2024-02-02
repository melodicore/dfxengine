package me.datafox.dfxengine.injector.internal;

import io.github.classgraph.*;
import me.datafox.dfxengine.injector.Parameter;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.ComponentClassWithUnresolvedTypeParameterException;
import me.datafox.dfxengine.injector.exception.FinalFieldDependencyException;
import me.datafox.dfxengine.injector.exception.UnknownComponentTypeParameterException;
import me.datafox.dfxengine.injector.utils.InjectorUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class ClassReferenceFactory {
    private final Map<String,ClassInfo> classInfoMap;

    private boolean buildingParameters;

    public ClassReferenceFactory(Map<String,ClassInfo> classInfoMap) {
        this.classInfoMap = classInfoMap;
    }

    public <T> ComponentData<T> buildComponentData(MethodInfo info) {
        buildingParameters = false;
        ComponentData.ComponentDataBuilder<T> builder = ComponentData
                .builder();
        FieldInfoList fields;
        MethodInfoList initializers;
        Component component;
        if(info.isConstructor()) {
            builder.reference(buildClassReference(info.getClassInfo()))
                    .executable(info.loadClassAndGetConstructor());
            component = info.getClassInfo().loadClass().getAnnotation(Component.class);
            fields = info.getClassInfo().getDeclaredFieldInfo()
                    .filter(field -> field.hasAnnotation(Inject.class));
            initializers = info.getClassInfo()
                    .getDeclaredMethodInfo()
                    .filter(method -> method.hasAnnotation(Initialize.class));
        } else {
            builder.reference(buildClassReference(info))
                    .executable(info.loadClassAndGetMethod());
            component = info.loadClassAndGetMethod().getAnnotation(Component.class);
            ClassInfo classInfo = classInfoMap.get(info.getTypeDescriptor().toString().split(" ", 2)[0]);
            fields = classInfo.getDeclaredFieldInfo()
                    .filter(field -> field.hasAnnotation(Inject.class));
            initializers = classInfo
                    .getDeclaredMethodInfo()
                    .filter(method -> method.hasAnnotation(Initialize.class));
            if(!info.isStatic()) {
                builder.owner(buildClassReference(info.getClassInfo()));
            }
        }
        if(component != null) {
            builder.policy(component.value())
                    .defaultImpl(component.defaultImpl());
        } else {
            builder.policy(InstantiationPolicy.ONCE)
                    .defaultImpl(false);
        }
        buildingParameters = true;
        Arrays.stream(info.getParameterInfo())
                .map(this::buildClassReference)
                .forEach(builder::parameter);
        fields.stream()
                .map(this::buildFieldReference)
                .forEach(builder::field);
        buildingParameters = false;
        initializers.stream()
                .map(this::buildInitializeReference)
                .forEach(builder::initializer);
        ComponentData<T> data = builder.build();

        Stream<ClassReference<?>> stream1 = Stream.concat(
                Stream.of(data.getReference()),
                data.getParameters().stream());

        Stream<ClassReference<?>> stream2 = Stream.concat(
                data.getFields().stream().map(FieldReference::getReference),
                data.getInitializers().stream().map(InitializeReference::getReference));

        Stream.concat(Stream.concat(stream1, stream2), data
                        .getInitializers()
                        .stream()
                        .map(InitializeReference::getParameters)
                        .flatMap(List::stream))
                .forEach(this::checkAndSetCollection);

        return data;
    }

    public <T> ClassReference<T> buildClassReference(ClassInfo info) {
        return buildClassReference(info.getTypeDescriptor().toString(), info.toString());
    }

    public <T> ClassReference<T> buildClassReference(MethodInfo info) {
        return buildClassReference(info.getTypeDescriptor().toString(), info.toString());
    }

    public <T> ClassReference<T> buildClassReference(MethodParameterInfo info) {
        return buildClassReference(info.getTypeDescriptor().toString(), info.toString());
    }

    private <T> FieldReference<T> buildFieldReference(FieldInfo info) {
        if(info.isFinal()) {
            throw new FinalFieldDependencyException(info.toString());
        }
        return FieldReference
                .<T>builder()
                .field(info.loadClassAndGetField())
                .reference(buildClassReference(info.getTypeDescriptor().toString(), info.toString()))
                .build();
    }

    private <T> InitializeReference<T> buildInitializeReference(MethodInfo info) {
        InitializeReference.InitializeReferenceBuilder<T> builder = InitializeReference
                .<T>builder()
                .priority(info
                        .loadClassAndGetMethod()
                        .getAnnotation(Initialize.class)
                        .value())
                .reference(buildClassReference(info.getClassInfo()))
                .method(info.loadClassAndGetMethod());
        buildingParameters = true;
        builder = builder.parameters(Arrays
                .stream(info.getParameterInfo())
                .map(this::buildClassReference)
                .collect(Collectors.toList()));
        buildingParameters = false;
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private <T> ClassReference<T> buildClassReference(String descriptor, String info) {
        if(Object.class.getName().equals(info)) {
            if("?".equals(descriptor)) {
                return (ClassReference<T>) ClassReference.object();
            }
        }
        String[] split = descriptor.split(" class | interface ",2);
        String namePrefix = "";
        if(descriptor.startsWith("? super ")) {
            namePrefix = "? super ";
            descriptor = descriptor.substring(8);
        } else if(descriptor.startsWith("? extends ")) {
            namePrefix = "? extends ";
            descriptor = descriptor.substring(10);
        }
        descriptor = split.length == 1 ? descriptor : split[1].split(" extends | implements", 2)[0];
        String className = descriptor.split("[ <]", 2)[0];
        className = getBoxedPrimitiveName(className);
        split = info.split(Pattern.quote(className), 2);
        String details = split.length == 1 ? "" : split[1];
        ClassInfo classInfo = classInfoMap.get(className);
        String classDetails = classInfo.toString().split(Pattern.quote(className), 2)[1];
        return buildClassReference(namePrefix + className, details, classDetails);
    }

    private <T> ClassReference<T> buildClassReference(String name, String details, String genericDetails) {
        String typeParameters = "";
        if(details.startsWith("<")) {
            typeParameters = InjectorUtils.getWithin(details, '<', '>');
            details = details.substring(typeParameters.length() + 2);
        }
        String classTypeParameters = "";
        if(genericDetails.startsWith("<")) {
            classTypeParameters = InjectorUtils.getWithin(genericDetails, '<', '>');
            genericDetails = genericDetails.substring(classTypeParameters.length() + 2);
        }
        String superclasses = "";
        if(genericDetails.startsWith(" extends ") || genericDetails.startsWith(" implements ")) {
            superclasses = String.join(", ", genericDetails
                    .split(" extends | implements ", 2)[1]
                    .split(" implements "));
        }
        if(!genericDetails.equals(details)) {
            superclasses = injectParameterTypes(typeParameters, classTypeParameters, superclasses);
        }
        return buildClassReference(name,
                InjectorUtils.splitParameters(typeParameters),
                InjectorUtils.splitParameters(superclasses));
    }

    @SuppressWarnings("unchecked")
    public <T> ClassReference<T> buildClassReference(String name, String[] parameters, String[] superclasses) {
        ClassInfo info = classInfoMap.get(name);
        if(info == null) {
            if(name.startsWith("?")) {
                if(name.startsWith("? super ")) {
                    name = name.substring(8);
                } else if(name.startsWith("? extends ")) {
                    name = name.substring(10);
                } else {
                    name = Object.class.getName();
                }
            }
            info = classInfoMap.get(name);
            if(info == null) {
                throw new UnknownComponentTypeParameterException();
            }
        }
        ClassReference.ClassReferenceBuilder<T,?,?> builder = ClassReference
                .<T>builder()
                .type((Class<T>) info.loadClass());

        for(String parameter : parameters) {
            if(parameter.isBlank()) {
                continue;
            }
            builder.parameter(parseParameter(parameter));
        }

        for(String superclass : superclasses) {
            if(superclass.isBlank()) {
                continue;
            }
            String typeParameters = "";
            if(superclass.contains("<")) {
                typeParameters = InjectorUtils.getWithin(superclass, '<', '>');
            }
            ClassInfo superclassInfo = classInfoMap.get(superclass);
            if(superclassInfo == null) {
                if(superclass.startsWith("public ")) {
                    superclass = superclass.substring(7);
                }
                if(superclass.startsWith("abstract ")) {
                    superclass = superclass.substring(9);
                }
                if(superclass.contains("<")) {
                    superclass = superclass.split("<", 2)[0];
                }
                superclassInfo = classInfoMap.get(superclass);
                if(superclassInfo == null) {
                    throw new UnknownComponentTypeParameterException();
                }
            }
            String superclassString = superclassInfo.toString();
            String genericTypeParameters = "";
            if(superclassString.contains("<")) {
                genericTypeParameters = InjectorUtils.getWithin(superclassString, '<', '>');
            }
            for(String parameter : parameters) {
                if(parameter.startsWith("?") && !typeParameters.contains(parameter)) {
                    String str;
                    if(parameter.startsWith("? extends ")) {
                        str = parameter.substring(10);
                    } else if(parameter.startsWith("? super ")) {
                        str = parameter.substring(8);
                    } else {
                        throw new IllegalArgumentException();
                    }
                    typeParameters = typeParameters.replaceAll(str, parameter);
                }
            }
            builder.superclass(buildClassReference(superclass,
                    injectParameterTypes(typeParameters, genericTypeParameters, superclassString)));

        }

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private Parameter<?> parseParameter(String parameter) {
        if(parameter.endsWith("[]")) {
            parameter = parameter.substring(0, parameter.length() - 2);
            Class<?> type = getPrimitiveArray(parameter);
            if(type != null) {
                return Parameter
                        .builder()
                        .type((Class<Object>) type)
                        .build();
            } else {
                return Parameter
                        .<Array>builder()
                        .type(Array.class)
                        .parameter(parseParameter(parameter))
                        .build();
            }
        }
        String[] split = parameter.split("<", 2);
        String name = split[0];
        check: if(name.startsWith("?")) {
            if(name.equals("?")) {
                return Parameter.object();
            } if(buildingParameters) {
                if(name.startsWith("? super ")) {
                    return Parameter.object();
                } else if(name.startsWith("? extends ")) {
                    name = name.substring(10);
                    break check;
                }
            }
            throw new ComponentClassWithUnresolvedTypeParameterException(parameter);
        } else if(name.equals(Object.class.getName())) {
            return Parameter.object();
        }
        ClassInfo info = classInfoMap.get(name);
        if(info == null) {
            throw new IllegalArgumentException();
        }
        Class<?> type = info.loadClass();
        Parameter.ParameterBuilder<?> builder = Parameter
                .builder()
                .type((Class<Object>) type);

        if(split.length > 1) {
            Arrays.stream(InjectorUtils.splitParameters(
                    split[1].substring(0, split[1].length() - 1)))
                    .map(this::parseParameter)
                    .forEach(builder::parameter);
        } else if(type.getTypeParameters().length != 0) {
            for(int i = 0; i < type.getTypeParameters().length; i++) {
                builder.parameter(Parameter.object());
            }
        }
        return builder.build();
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
                return className;
        }
    }

    private Class<?> getPrimitiveArray(String parameter) {
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

    private String injectParameterTypes(String typeParameters, String genericTypeParameters, String classes) {
        String[] keys = InjectorUtils.splitParameters(genericTypeParameters);
        String[] values = InjectorUtils.splitParameters(typeParameters);
        for(int i = 0; i < keys.length; i++) {
            classes = injectParameterType(classes, keys[i], values[i]);
        }
        return classes;
    }

    private static String injectParameterType(String classes, String key, String value) {
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < classes.length(); j++) {
            if(classes.charAt(j) == '<') {
                sb.append('<');
                j++;
            } else if(classes.startsWith(", ", j)) {
                sb.append(", ");
                j += 2;
            } else {
                sb.append(classes.charAt(j));
                continue;
            }
            if(classes.startsWith(key, j)) {
                j += key.length();
                if(classes.charAt(j) == '>' || classes.startsWith(", ", j)) {
                    sb.append(value);
                } else {
                    sb.append(key);
                }
                sb.append(classes.charAt(j));
            } else {
                j--;
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private <T> void checkAndSetCollection(ClassReference<T> data) {
        if(List.class.equals(data.getType())) {
            if(data.getParameters().size() != 1) {
                throw new IllegalArgumentException();
            }
            Parameter<?> param = data.getParameters().get(0);
            data.setList(true);
            if(param.getType().equals(Object.class)) {
                data.setListReference(ClassReference.object());
            } else {
                data.setListReference(ClassReference
                        .builder()
                        .type((Class<Object>) param.getType())
                        .parameters(param.getParameters())
                        .build());
            }
        }
    }
}
