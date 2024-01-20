package me.datafox.dfxengine.injector;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.datafox.dfxengine.collections.FunctionClassMap;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.CyclicDependencyException;
import me.datafox.dfxengine.injector.exception.InvalidDefaultTypeException;
import me.datafox.dfxengine.injector.exception.MultipleValidComponentsException;
import me.datafox.dfxengine.injector.exception.UnknownComponentException;
import me.datafox.dfxengine.injector.utils.InjectorStrings;
import me.datafox.dfxengine.injector.utils.InjectorUtils;
import me.datafox.dfxengine.utils.ClassUtils;
import me.datafox.dfxengine.utils.LogUtils;
import me.datafox.dfxengine.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scans the classpath with {@link ClassGraph} and constructs an {@link Injector} instance. Has checks for existence of
 * dependencies, instantiation order and cyclic dependencies. For more information on how it works, see
 * {@link Injector}.
 *
 * @author datafox
 */
public class InjectorBuilder {
    /**
     * @return {@link InjectorBuilder} instance
     */
    public static InjectorBuilder create() {
        return new InjectorBuilder();
    }

    private final Logger logger;

    private final FunctionClassMap<Dependency<?>> dependencyMap;
    private final Map<Class<?>,Dependency<?>> classDefinitions;
    private final Map<MethodReference<?,?>,Dependency<?>> methodDefinitions;
    private final Map<Dependency<?>,Class<?>> reverseClassDefinitions;
    private final Map<Dependency<?>,MethodReference<?,?>> reverseMethodDefinitions;

    private final Set<String> packageWhitelist;
    private final Set<String> packageBlacklist;
    private final Set<String> classWhitelist;
    private final Set<String> classBlacklist;
    private boolean parameterizedWarnings;

    private InjectorBuilder() {
        logger = LoggerFactory.getLogger(getClass());

        dependencyMap = new FunctionClassMap<>(Dependency::getType);
        classDefinitions = new HashMap<>();
        methodDefinitions = new HashMap<>();
        reverseClassDefinitions = new HashMap<>();
        reverseMethodDefinitions = new HashMap<>();

        packageWhitelist = new HashSet<>();
        packageBlacklist = new HashSet<>();
        classWhitelist = new HashSet<>();
        classBlacklist = new HashSet<>();
        parameterizedWarnings = true;
    }

    /**
     * Whitelists a single package. Package name should be in the standard format of <i>com.example.package</i>
     *
     * @param packageName name of package to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistPackage(String packageName) {
        packageWhitelist.add(packageName);
        return this;
    }

    /**
     * Whitelists multiple packages. Package names should be in the standard format of <i>com.example.package</i>
     *
     * @param packageNames names of packages to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistPackages(Collection<String> packageNames) {
        packageWhitelist.addAll(packageNames);
        return this;
    }

    /**
     * Blacklists a single package. Package name should be in the standard format of <i>com.example.package</i>
     *
     * @param packageName name of package to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistPackage(String packageName) {
        packageBlacklist.add(packageName);
        return this;
    }

    /**
     * Blacklists multiple packages. Package names should be in the standard format of <i>com.example.package</i>
     *
     * @param packageNames names of packages to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistPackages(Collection<String> packageNames) {
        packageBlacklist.addAll(packageNames);
        return this;
    }

    /**
     * Whitelists a single class. Class name should be in the standard format of <i>com.example.package.Class</i>
     *
     * @param className name of class to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistClass(String className) {
        classWhitelist.add(className);
        return this;
    }

    /**
     * Whitelists multiple classes. Class names should be in the standard format of <i>com.example.package.Class</i>
     *
     * @param classNames names of classes to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistClasses(Collection<String> classNames) {
        classWhitelist.addAll(classNames);
        return this;
    }

    /**
     * Blacklists a single class. Class name should be in the standard format of <i>com.example.package.Class</i>
     *
     * @param className name of class to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistClass(String className) {
        classBlacklist.add(className);
        return this;
    }

    /**
     * Blacklists multiple classes. Class names should be in the standard format of <i>com.example.package.Class</i>
     *
     * @param classNames names of classes to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistClasses(Collection<String> classNames) {
        classBlacklist.addAll(classNames);
        return this;
    }

    /**
     * Disables warnings in logs when scanned components or their superclasses/interfaces have parameterized types.
     *
     * @return builder
     */
    public InjectorBuilder disableParameterizedWarnings() {
        parameterizedWarnings = false;
        return this;
    }

    /**
     * Builds the Injector. See {@link Injector} for detailed information of what this entails.
     *
     * @return injector
     */
    public Injector build() {
        logger.info(InjectorStrings.BUILD_STARTED);

        ClassGraph classGraph = new ClassGraph().enableAllInfo();

        if(!packageWhitelist.isEmpty()) {
            logger.info(InjectorStrings.packageWhitelistPresent(packageWhitelist));
            classGraph.acceptPackages(packageWhitelist.toArray(String[]::new));
        }

        if(!packageBlacklist.isEmpty()) {
            logger.info(InjectorStrings.packageBlacklistPresent(packageBlacklist));
            classGraph.rejectPackages(packageBlacklist.toArray(String[]::new));
        }

        if(!classWhitelist.isEmpty()) {
            logger.info(InjectorStrings.classWhitelistPresent(classWhitelist));
            classGraph.acceptClasses(classWhitelist.toArray(String[]::new));
        }

        if(!classBlacklist.isEmpty()) {
            logger.info(InjectorStrings.classBlacklistPresent(classBlacklist));
            classGraph.rejectClasses(classBlacklist.toArray(String[]::new));
        }

        logger.info(InjectorStrings.SCANNING_CLASSPATH);

        ScanResult scan = classGraph.scan();

        ClassInfoList componentClasses = scan.getClassesWithAnnotation(Component.class);
        ClassInfoList componentMethodClasses = scan.getClassesWithMethodAnnotation(Component.class);

        classDefinitions.putAll(
                componentClasses.stream()
                        .map(ClassInfo::loadClass)
                        .peek(this::checkClass)
                        .collect(Collectors.toMap(
                                type -> type,
                                this::createClassDependency)));

        methodDefinitions.putAll(
                componentMethodClasses.stream()
                        .map(ClassInfo::loadClass)
                        .flatMap(this::streamMethodReferences)
                        .filter(reference -> reference.hasMethodAnnotation(Component.class))
                        .peek(this::checkMethod)
                        .collect(Collectors.toMap(
                                method -> method,
                                this::createMethodDependency)));

        dependencyMap.putAll(classDefinitions.values());
        dependencyMap.putAll(methodDefinitions.values());

        logger.info(InjectorStrings.RESOLVING_DEPENDENCIES);

        classDefinitions.forEach(this::resolveClassDependencies);
        methodDefinitions.forEach(this::resolveMethodDependencies);

        logger.info(InjectorStrings.CHECKING_CYCLIC);

        classDefinitions.values().forEach(dependency -> checkCyclicDependencies(dependency, dependency));
        methodDefinitions.values().forEach(dependency -> checkCyclicDependencies(dependency, dependency));

        logger.info(InjectorStrings.DETERMINING_ORDER);

        Map<Dependency<?>,Integer> orderMap = getInstantiationOrder(
                Stream.concat(classDefinitions.values().stream(),
                                methodDefinitions.values().stream())
                        .collect(Collectors.toList()));

        logger.info(InjectorStrings.INITIALIZING_INJECTOR);

        Injector injector = new Injector(
                Stream.concat(
                                classDefinitions.entrySet().stream().flatMap(this::getPerInstanceConstructorReference),
                                methodDefinitions.entrySet().stream().flatMap(this::getPerInstanceMethodReference))
                        .filter(Predicate.not(this::isOverrodePerInstanceReference))
                        .collect(Collectors.toList()));

        logger.info(InjectorStrings.INSTANTIATING_COMPONENTS);

        reverseClassDefinitions.putAll(MapUtils.reverseMap(classDefinitions));
        reverseMethodDefinitions.putAll(MapUtils.reverseMap(methodDefinitions));

        orderMap.keySet()
                .stream()
                .filter(Predicate.not(Dependency::isPerInstance))
                .filter(Predicate.not(this::isOverrodeComponent))
                .sorted(Comparator.comparingInt(orderMap::get))
                .forEach(dependency -> instantiateComponents(dependency, injector));

        logger.info(InjectorStrings.FINALIZING_INJECTOR);

        injector.finishBuilding();

        scan.close();

        logger.info(InjectorStrings.BUILD_FINISHED);

        return injector;
    }

    private <T> void checkClass(Class<T> type) {
        logger.debug(InjectorStrings.componentClassFound(type));

        ClassUtils.getAnnotationFromArray(type.getAnnotations(), Component.class)
                .filter(component -> !component.defaultFor().equals(Object.class))
                .ifPresent(component -> {
                    if(ClassUtils.getSuperclassesFor(type)
                            .noneMatch(t -> t.equals(component.defaultFor()))) {
                        throw LogUtils.logExceptionAndGet(logger,
                                InjectorStrings.invalidOverrideType(type, component.defaultFor()),
                                InvalidDefaultTypeException::new);
                    }
                });

        checkParameterized(type);
    }

    private <T,R> void checkMethod(MethodReference<T,R> reference) {
        logger.debug(InjectorStrings.componentMethodFound(reference));

        ClassUtils.getAnnotationFromArray(reference.getMethod().getAnnotations(), Component.class)
                .filter(component -> !component.defaultFor().equals(Object.class))
                .ifPresent(component -> {
                    if(ClassUtils.getSuperclassesFor(reference.getReturnType())
                            .noneMatch(t -> t.equals(component.defaultFor()))) {
                        throw LogUtils.logExceptionAndGet(logger,
                                InjectorStrings.invalidMethodOverrideType(reference,
                                        component.defaultFor()),
                                InvalidDefaultTypeException::new);
                    }
                });

        if(!Modifier.isStatic(reference.getMethod().getModifiers())) {
            checkParameterized(reference.getOwner());
        }

        checkParameterized(reference.getReturnType());
    }

    private <T> void checkParameterized(Class<T> type) {
        if(!parameterizedWarnings) {
            return;
        }

        logger.debug(InjectorStrings.checkingParameterized(type));

        if(type.getTypeParameters().length != 0) {
            logger.warn(InjectorStrings.parameterizedType(type));
        }

        ClassUtils.getSuperclassesFor(type)
                .filter(Predicate.not(Predicate.isEqual(type)))
                .forEach(this::checkParameterized);
    }

    private <T> Dependency<T> createClassDependency(Class<T> type) {
        return Dependency.of(type,
                ClassUtils
                        .getAnnotationFromArray(type.getAnnotations(), Component.class)
                        .orElse(null),
                isPerInstance(type.getAnnotations()));
    }

    private <T> Dependency<T> createMethodDependency(MethodReference<?,T> reference) {
        return Dependency.of(reference.getReturnType(),
                ClassUtils
                        .getAnnotationFromArray(reference.getMethod().getAnnotations(), Component.class)
                        .orElse(null),
                isPerInstance(reference.getMethod().getAnnotations()));
    }

    private void resolveClassDependencies(Class<?> type, Dependency<?> dependency) {
        logger.info(InjectorStrings.resolvingComponentDependencies(type));
        registerDependenciesFromExecutable(dependency, InjectorUtils.getConstructor(type, logger));
        registerDependenciesFromFields(dependency, ClassUtils.getFieldsWithAnnotation(type, Inject.class));
    }

    private void resolveMethodDependencies(MethodReference<?,?> reference, Dependency<?> dependency) {
        Class<?> type = reference.getOwner();
        Method method = reference.getMethod();

        logger.info(InjectorStrings.resolvingMethodDependencies(reference));

        if(!Modifier.isStatic(method.getModifiers())) {
            logger.debug(InjectorStrings.methodNotStatic(type));
            if(classDefinitions.containsKey(type)) {
                dependency.add(classDefinitions.get(type));
            } else {
                Dependency<?> classDependency = createClassDependency(type);
                classDefinitions.put(type, classDependency);
                dependency.add(classDependency);
            }
        }

        registerDependenciesFromExecutable(dependency, method);
    }

    private void checkCyclicDependencies(Dependency<?> original, Dependency<?> current) {
        if(!original.equals(current)) {
            logger.debug(InjectorStrings.checkingCyclicFor(original.getType(), current.getType()));
        }

        if(current.contains(original)) {
            throw LogUtils.logExceptionAndGet(logger,
                    InjectorStrings.cyclicDetected(original.getType(), current.getType()),
                    CyclicDependencyException::new);
        }

        current.getDependencies()
                .stream()
                .forEach(dependency ->
                        checkCyclicDependencies(original, dependency));
    }

    private Map<Dependency<?>,Integer> getInstantiationOrder(Collection<Dependency<?>> dependencies) {
        Map<Dependency<?>,Integer> orderMap = new HashMap<>();

        Set<Dependency<?>> unresolved = new HashSet<>(dependencies);

        while(!unresolved.isEmpty()) {
            for(Dependency<?> dependency : unresolved) {
                if(dependency.isEmpty()) {
                    logger.debug(InjectorStrings.noDependenciesOrder(dependency.getType()));

                    orderMap.put(dependency, 0);
                    continue;
                }

                if(orderMap.keySet().containsAll(dependency.getDependencies())) {
                    dependency.getDependencies()
                            .stream()
                            .map(orderMap::get)
                            .mapToInt(Integer::intValue)
                            .max().ifPresent(priority -> {
                                priority++;
                                logger.debug(InjectorStrings.allDependenciesOrder(dependency.getType(), priority));
                                orderMap.put(dependency, priority);
                            });
                }
            }
            unresolved.removeAll(orderMap.keySet());
        }

        return orderMap;
    }

    private Stream<Injector.PerInstanceReference<?,?>> getPerInstanceConstructorReference(Map.Entry<Class<?>,Dependency<?>> entry) {
        if(!entry.getValue().isPerInstance()) return Stream.empty();

        Class<?> type = entry.getKey();

        logger.debug(InjectorStrings.registeringPerInstanceClass(type));

        return Stream.of(Injector.PerInstanceReference.of(type, InjectorUtils.getConstructor(type, logger)));
    }

    private Stream<Injector.PerInstanceReference<?,?>> getPerInstanceMethodReference(Map.Entry<MethodReference<?,?>,Dependency<?>> entry) {
        if(!entry.getValue().isPerInstance()) return Stream.empty();

        MethodReference<?,?> reference = entry.getKey();

        logger.debug(InjectorStrings.registeringPerInstanceMethod(entry.getKey()));

        return Stream.of(Injector.PerInstanceReference
                .of(reference.getReturnType(), reference.getOwner(), reference.getMethod()));
    }

    private void instantiateComponents(Dependency<?> dependency, Injector injector) {
        logger.info(InjectorStrings.instantiatingComponent(dependency.getType()));

        if(reverseClassDefinitions.containsKey(dependency)) {
            logger.debug(InjectorStrings.instantiatedByConstructor(dependency.getType()));

            Class<?> type = reverseClassDefinitions.get(dependency);

            injector.addComponent(injector.newInstance(type));

            return;
        }
        if(reverseMethodDefinitions.containsKey(dependency)) {
            MethodReference<?,?> reference = reverseMethodDefinitions.get(dependency);
            boolean isStatic = Modifier.isStatic(reference.getMethod().getModifiers());

            logger.debug(InjectorStrings.instantiatedByMethod(dependency.getType(), reference));

            Class<?> dependencyClass = reference.getOwner();
            injector.addComponent(injector.invokeMethod(dependencyClass,
                    reference.getMethod(),
                    isStatic ? null : injector.getSingletonComponent(dependencyClass,
                            dependency.getType())));
        }
    }

    private boolean isPerInstance(Annotation[] annotations) {
        return ClassUtils
                .getAnnotationFromArray(annotations, Component.class)
                .map(Component::value)
                .map(InstantiationPolicy.PER_INSTANCE::equals)
                .orElse(false);
    }

    private void registerDependenciesFromExecutable(Dependency<?> dependency, Executable executable) {
        for(int i=0; i<executable.getParameterCount(); i++) {
            registerDependency(dependency,
                    executable.getParameterTypes()[i],
                    executable.getGenericParameterTypes()[i]);
        }
    }

    private void registerDependency(Dependency<?> dependency, Class<?> type, Type parametricType) {
        logger.debug(InjectorStrings.registeringDependency(type, dependency.getType()));

        if(type.equals(InstantiationDetails.class)) {
            return;
        }

        boolean list = List.class.isAssignableFrom(type);

        if(list) {
            type = InjectorUtils.getListType(parametricType, logger);

            logger.debug(InjectorStrings.listDependency(type));
        }

        if(!dependencyMap.contains(type)) {
            throw LogUtils.logExceptionAndGet(logger, InjectorStrings.unknownComponent(type),
                    UnknownComponentException::new);
        }

        if(!list && !dependencyMap.isSingleton(type)) {
            throw LogUtils.logExceptionAndGet(logger, InjectorStrings.multipleValidComponents(type),
                    MultipleValidComponentsException::new);
        }

        dependency.addAll(dependencyMap.get(type));
    }

    private void registerDependenciesFromFields(Dependency<?> dependency, List<Field> fields) {
        fields.forEach(field ->
                registerDependency(dependency,
                        field.getType(),
                        field.getGenericType()));
    }

    private <T> Stream<MethodReference<T,?>> streamMethodReferences(Class<T> type) {
        return Arrays
                .stream(type.getDeclaredMethods())
                .map(method -> MethodReference.of(type, method.getReturnType(), method));
    }

    private boolean isOverrodeComponent(Dependency<?> dependency) {
        if(dependency.getAnnotation() == null) {
            return false;
        }
        if(dependency.getAnnotation().defaultFor().equals(Object.class)) {
            return false;
        }
        return ClassUtils.getSuperclassesFor(dependency.getAnnotation().defaultFor())
                .map(dependencyMap::get)
                .flatMap(List::stream)
                .anyMatch(Predicate.not(this::isOverride));
    }

    private boolean isOverrodePerInstanceReference(Injector.PerInstanceReference<?,?> reference) {
        return getComponentAnnotation(reference).map(component ->
                isOverrodePerInstanceReference(reference, component.defaultFor()))
                .orElse(false);
    }

    private boolean isOverrodePerInstanceReference(Injector.PerInstanceReference<?,?> reference, Class<?> override) {
        if(override.equals(Object.class)) {
            return false;
        }
        return ClassUtils.getSuperclassesFor(override)
                .map(dependencyMap::get)
                .flatMap(List::stream)
                .anyMatch(Predicate.not(this::isOverride));
    }

    private boolean isOverride(Dependency<?> dependency) {
        if(dependency.annotation == null) {
            return false;
        }
        return !dependency.annotation.defaultFor().equals(Object.class);
    }

    private Optional<Component> getComponentAnnotation(Injector.PerInstanceReference<?,?> reference) {
        Optional<Component> annotation;
        if(reference.getExecutable() instanceof Method) {
            Method method = (Method) reference.getExecutable();
            annotation = ClassUtils.getAnnotationFromArray(
                    method.getAnnotations(), Component.class);
        } else if(reference.getExecutable() instanceof Constructor<?>) {
            Constructor<?> constructor = (Constructor<?>) reference.getExecutable();
            annotation = ClassUtils.getAnnotationFromArray(
                    reference.getType().getAnnotations(), Component.class);
        } else {
            return Optional.empty();
        }
        return annotation;
    }

    @Data
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings("MissingJavadoc")
    public static class MethodReference<T,R> {
        private final Class<T> owner;

        private final Class<R> returnType;

        private final Method method;

        public boolean hasMethodAnnotation(Class<? extends Annotation> annotation) {
            return method.isAnnotationPresent(annotation);
        }

        public static <T,R> MethodReference<T,R> of(Class<T> owner, Class<R> returnType, Method method) {
            return new MethodReference<>(owner, returnType, method);
        }
    }

    @Data
    private static class Dependency<T> {
        private static long runningId = 0;

        private final long id;

        private final Class<T> type;

        private final Component annotation;

        private final boolean perInstance;

        @EqualsAndHashCode.Exclude
        private final Set<Dependency<?>> dependencies;

        private Dependency(Class<T> type, Component annotation, boolean perInstance) {
            id = runningId++;
            this.type = type;
            this.annotation = annotation;
            this.perInstance = perInstance;
            dependencies = new HashSet<>();
        }

        Set<Dependency<?>> getDependencies() {
            return Collections.unmodifiableSet(dependencies);
        }

        void add(Dependency<?> dependency) {
            dependencies.add(dependency);
        }

        void addAll(Collection<Dependency<?>> collection) {
            dependencies.addAll(collection);
        }

        boolean contains(Dependency<?> dependency) {
            return dependencies.contains(dependency);
        }

        boolean isEmpty() {
            return dependencies.isEmpty();
        }

        public static <T> Dependency<T> of(Class<T> type, Component annotation, boolean perInstance) {
            return new Dependency<>(type, annotation, perInstance);
        }
    }
}
