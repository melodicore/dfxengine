package me.datafox.dfxengine.injector.internal;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodParameterInfo;
import me.datafox.dfxengine.injector.TypeRef;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.ArrayComponentException;
import me.datafox.dfxengine.injector.exception.UnresolvedOrUnknownTypeException;
import me.datafox.dfxengine.injector.exception.FinalFieldDependencyException;
import me.datafox.dfxengine.injector.utils.InjectorStrings;
import me.datafox.dfxengine.injector.utils.InjectorUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Factory that builds {@link ComponentData} instances and by extension {@link ClassReference} and {@link TypeRef}
 * instances.
 *
 * @author datafox
 */
public class ComponentDataFactory {
    private final Logger logger;

    private final Map<String,ClassInfo> classInfoMap;

    private MethodInfo currentInfo;

    public ComponentDataFactory(Map<String,ClassInfo> classInfoMap) {
        logger = LoggerFactory.getLogger(getClass());
        this.classInfoMap = classInfoMap;
    }

    /**
     * @param info constructor or method of a component
     * @return {@link ComponentData} for the specified constructor or method
     * @param <T> type of the component
     */
    public <T> ComponentData<T> buildComponentData(MethodInfo info) {
        currentInfo = info;
        Component annotation = null;
        ComponentData.ComponentDataBuilder<T> builder = ComponentData.builder();
        if(info.isConstructor()) {
            String classString = getClassString(info.getClassInfo());
            logger.debug(InjectorStrings.buildingComponentClassData(classString, info));
            ClassReference<T> reference = buildClassReference(
                    classString,
                    getSuperStrings(info.getClassInfo()));
            builder.reference(reference)
                    .executable(info.loadClassAndGetConstructor());
            if(info.getClassInfo().hasAnnotation(Component.class)) {
                annotation = (Component) info.getClassInfo().getAnnotationInfo(Component.class).loadClassAndInstantiate();
            }
            for(FieldInfo field : info.getClassInfo().getDeclaredFieldInfo().filter(f -> f.hasAnnotation(Inject.class))) {
                if(field.isFinal()) {
                    throw LogUtils.logExceptionAndGet(logger,
                            InjectorStrings.finalFieldDependency(field),
                            FinalFieldDependencyException::new);
                }
                if(InstantiationPolicy.class.getName().equals(getClassString(field))) {
                    continue;
                }
                builder.field(FieldReference
                        .builder()
                        .field(field.loadClassAndGetField())
                        .reference(buildClassReference(getClassString(field), getSuperStrings(field)))
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
                    if(InstantiationPolicy.class.getName().equals(getClassString(param))) {
                        continue;
                    }
                    initBuilder.parameter(buildClassReference(getClassString(param), getSuperStrings(param)));
                }
                builder.initializer(initBuilder.build());
            }
        } else {
            String classString = getClassString(info);
            logger.debug(InjectorStrings.buildingComponentMethodData(classString, info));
            builder.reference(buildClassReference(
                            classString,
                            getSuperStrings(info)))
                    .executable(info.loadClassAndGetMethod());
            if(!info.isStatic()) {
                builder.owner(buildClassReference(
                        getClassString(info.getClassInfo()),
                        getSuperStrings(info.getClassInfo())));
            }
            if(info.hasAnnotation(Component.class)) {
                annotation = (Component) info.getAnnotationInfo(Component.class).loadClassAndInstantiate();
            }
        }
        if(annotation != null) {
            builder.policy(annotation.value())
                    .order(annotation.order());
        } else {
            builder.policy(InstantiationPolicy.ONCE)
                    .order(0);
        }
        for(MethodParameterInfo param : info.getParameterInfo()) {
            if(InstantiationPolicy.class.getName().equals(getClassString(param))) {
                continue;
            }
            builder.parameter(buildClassReference(getClassString(param), getSuperStrings(param)));
        }
        return builder.build();
    }

    private String getClassString(ClassInfo info) {
        return info
                .getTypeSignatureOrTypeDescriptor()
                .toString()
                .split(" class | interface ", 2)[1]
                .split(" extends | implements ")[0];
    }

    private String getClassString(MethodInfo info) {
        if(info.getTypeDescriptor().toString().contains("[]")) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.arrayComponent(info, info.getClassInfo()),
                    ArrayComponentException::new);
        }
        String str = info.getTypeSignatureOrTypeDescriptor().toString();
        if(str.startsWith("<")) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.unresolvedTypeParameter(info, InjectorUtils.splitParameters(str).get(0)),
                    UnresolvedOrUnknownTypeException::new);
        }
        return info
                .getTypeSignatureOrTypeDescriptor()
                .toString()
                .split(" \\(", 2)[0];
    }

    private String getClassString(MethodParameterInfo info) {
        if(info.getTypeDescriptor().toString().contains("[]")) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.arrayDependency(info),
                    ArrayComponentException::new);
        }
        return info
                .getTypeSignatureOrTypeDescriptor()
                .toString();
    }

    private String getClassString(FieldInfo info) {
        if(info.getTypeDescriptor().toString().contains("[]")) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.arrayFieldDependency(info),
                    ArrayComponentException::new);
        }
        return info
                .getTypeSignatureOrTypeDescriptor()
                .toString();
    }

    private List<String> getSuperStrings(ClassInfo info) {
        String[] split = info
                .getTypeSignatureOrTypeDescriptor()
                .toString()
                .split(" extends | implements ", 2);
        if(split.length == 1) {
            return List.of();
        }
        return Arrays.stream(split[1].split(" implements "))
                .map(InjectorUtils::splitParameters)
                .flatMap(List::stream)
                .flatMap(this::parseSupers)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getSuperStrings(MethodInfo info) {
        List<String> list = parseSupers(getClassString(info))
                .distinct()
                .collect(Collectors.toList());
        return list.subList(1, list.size());
    }

    private List<String> getSuperStrings(MethodParameterInfo info) {
        List<String> list = parseSupers(getClassString(info))
                .distinct()
                .collect(Collectors.toList());
        return list.subList(1, list.size());
    }

    private List<String> getSuperStrings(FieldInfo info) {
        List<String> list = parseSupers(getClassString(info))
                .distinct()
                .collect(Collectors.toList());
        return list.subList(1, list.size());
    }

    private Stream<String> parseSupers(String classString) {
        String[] base = classString.split("<", 2);
        Stream<String> stream = Stream.of(classString);
        ClassInfo info = classInfoMap.get(base[0]);
        if(info == null) {
            info = classInfoMap.get(getBoxedPrimitiveName(base[0]));
        }
        List<String> params;
        List<String> genericParams;
        if(base.length == 1) {
            params = List.of();
            genericParams = List.of();
        } else {
            params = InjectorUtils.splitParameters(base[1].substring(0, base[1].length() - 1));
            genericParams = InjectorUtils
                    .splitParameters(info
                            .getTypeSignatureOrTypeDescriptor()
                            .toString()
                            .split("<", 2)[1])
                    .stream()
                    .map(type -> type.split(" extends | super ", 2)[0])
                    .collect(Collectors.toList());
        }
        return Stream.concat(stream, Optional
                .of(info)
                .stream()
                .flatMap(this::getSuperclassStrings)
                .map(superclass -> replaceParams(superclass, params, genericParams))
                .flatMap(this::parseSupers));
    }

    private Stream<String> getSuperclassStrings(ClassInfo original) {
        if(!original.getTypeDescriptor().toString().contains(" extends ") && !original.getTypeDescriptor().toString().contains(" implements ")) {
            return Stream.empty();
        }
        return InjectorUtils.getSuperclasses(InjectorUtils.stripKeywords(original.toString()));
    }

    private String replaceParams(String superclass, List<String> params, List<String> genericParams) {
        if(params.isEmpty() || !superclass.contains("<")) {
            return superclass;
        }
        String[] split = superclass.split("<", 2);
        List<String> superParams = InjectorUtils.splitParameters(split[1]);
        StringBuilder sb = new StringBuilder(split[0]).append("<");
        String prefix = "";
        for(String s : superParams) {
            sb.append(prefix).append(params.get(genericParams.indexOf(s)));
            prefix = ", ";
        }
        sb.append(">");
        return sb.toString();
    }

    private <T> ClassReference<T> buildClassReference(String classString, List<String> superStrings) {
        ClassReference<T> reference = ClassReference
                .<T>builder()
                .typeRef(buildTypeRef(classString))
                .superclasses(superStrings
                        .stream()
                        .map(this::buildTypeRef)
                        .collect(Collectors.toList()))
                .build();
        checkAndSetCollection(reference);
        return reference;
    }

    @SuppressWarnings("unchecked")
    private <T> TypeRef<T> buildTypeRef(String classString) {
        String[] split = classString.split("<", 2);
        Class<T> type = null;
        if(split[0].endsWith("[]")) {
            String str = split[0].substring(0, split[0].length() - 2);
            type = (Class<T>) getPrimitiveArray(str);
            if(type == null) {
                return buildArrayTypeRef(str);
            }
        } else {
            ClassInfo info = classInfoMap.get(split[0]);
            if(info == null) {
                if(split[0].startsWith("?")) {
                    if(split[0].startsWith("? extends ")) {
                        info = classInfoMap.get(split[0].substring(10));
                    } else {
                        type = (Class<T>) Object.class;
                    }
                } else {
                    info = classInfoMap.get(getBoxedPrimitiveName(split[0]));
                }
            }
            if(type == null) {
                if(info == null) {
                    throw LogUtils.logExceptionAndGet(logger,
                            InjectorStrings.unresolvedTypeParameter(currentInfo, classString),
                            UnresolvedOrUnknownTypeException::new);
                }
                type = (Class<T>) info.loadClass();
            }
        }
        TypeRef.TypeRefBuilder<T> builder = TypeRef.<T>builder()
                .type(type);
        if(split.length != 1) {
            builder.parameters(InjectorUtils
                    .splitParameters(split[1])
                    .stream()
                    .map(this::buildTypeRef)
                    .collect(Collectors.toList()));
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private <T> TypeRef<T> buildArrayTypeRef(String classString) {
        return TypeRef
                .<T>builder()
                .type((Class<T>) Array.class)
                .parameter(buildTypeRef(classString))
                .build();
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

    @SuppressWarnings("unchecked")
    private <T> void checkAndSetCollection(ClassReference<T> data) {
        if(List.class.equals(data.getTypeRef().getType())) {
            TypeRef<?> param = data.getTypeRef().getParameters().get(0);
            data.setList(true);
            if(param.getType().equals(Object.class)) {
                data.setListReference(ClassReference.object());
            } else {
                data.setListReference(ClassReference
                        .builder()
                        .typeRef((TypeRef<Object>) param)
                        .build());
            }
        }
    }
}