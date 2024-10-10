package me.datafox.dfxengine.injector;

import io.github.classgraph.*;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.ComponentClassWithMultipleValidConstructorsException;
import me.datafox.dfxengine.injector.exception.ComponentClassWithNoValidConstructorsException;
import me.datafox.dfxengine.injector.exception.CyclicDependencyException;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.injector.internal.ComponentDataFactory;
import me.datafox.dfxengine.injector.internal.FieldReference;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.injector.utils.InjectorStrings.*;

/**
 * Builder for the {@link InjectorImpl}. For general information, see
 * <a href="https://github.com/melodicore/dfxengine/blob/master/injector/README.md">README.md</a> on GitHub.
 *
 * @author datafox
 */
public class InjectorBuilder {
    private final Logger logger;
    private final List<String> packageWhitelist;
    private final List<String> packageBlacklist;
    private final List<String> classWhitelist;
    private final List<String> classBlacklist;

    /**
     * Constructor for the builder
     */
    public InjectorBuilder() {
        logger = LoggerFactory.getLogger(InjectorBuilder.class);
        packageWhitelist = new ArrayList<>();
        packageBlacklist = new ArrayList<>();
        classWhitelist = new ArrayList<>();
        classBlacklist = new ArrayList<>();
    }

    /**
     * Whitelists a package. Package name should be in the standard format of <i>com.example.package</i>
     *
     * @param packageName name of the package to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistPackage(String packageName) {
        packageWhitelist.add(Pattern.quote(packageName));
        return this;
    }

    /**
     * Whitelists packages. Package names are in the standard format of <i>com.example.package</i>
     *
     * @param regex regular expression matching the packages to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistPackageRegex(String regex) {
        packageWhitelist.add(regex);
        return this;
    }

    /**
     * Blacklists a package. Package name should be in the standard format of <i>com.example.package</i>
     *
     * @param packageName name of package to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistPackage(String packageName) {
        packageBlacklist.add(Pattern.quote(packageName));
        return this;
    }

    /**
     * Blacklists packages. Package names are in the standard format of <i>com.example.package</i>
     *
     * @param regex regular expression matching the packages to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistPackageRegex(String regex) {
        packageBlacklist.add(regex);
        return this;
    }

    /**
     * Whitelists a class. Class name should be in the standard format of <i>com.example.package.Class</i>
     *
     * @param className name of class to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistClass(String className) {
        classWhitelist.add(Pattern.quote(className));
        return this;
    }

    /**
     * Whitelists classes. Class names are in the standard format of <i>com.example.package.Class</i>
     *
     * @param regex regular expression matching the classes to whitelist
     * @return builder
     */
    public InjectorBuilder whitelistClassRegex(String regex) {
        classWhitelist.add(regex);
        return this;
    }

    /**
     * Blacklists a class. Class name should be in the standard format of <i>com.example.package.Class</i>
     *
     * @param className name of class to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistClass(String className) {
        classBlacklist.add(Pattern.quote(className));
        return this;
    }

    /**
     * Blacklists classes. Class names are in the standard format of <i>com.example.package.Class</i>
     *
     * @param regex regular expression matching the classes to blacklist
     * @return builder
     */
    public InjectorBuilder blacklistClassRegex(String regex) {
        classBlacklist.add(regex);
        return this;
    }

    /**
     * Builds the {@link InjectorImpl} according to the scan done at compile time.
     *
     * @return the {@link InjectorImpl}
     */
    public InjectorImpl build() {
        logger.info(SCANNING_CLASSPATH);
        ScanResult scan = new ClassGraph().enableAllInfo().enableSystemJarsAndModules().scan();

        checkAndLogWhitelistAndBlacklist();

        ClassInfoList componentClasses = scan
                .getClassesWithAnnotation(Component.class)
                .filter(this::whitelistBlacklistFilter);

        logComponentClasses(componentClasses);

        ClassInfoList componentMethodClasses = scan
                .getClassesWithMethodAnnotation(Component.class)
                .filter(this::whitelistBlacklistFilter);

        logComponentMethodClasses(componentMethodClasses);

        ClassInfoList instantiatedComponentMethodClasses =
                componentMethodClasses.filter(info -> info
                        .getDeclaredMethodInfo()
                        .stream()
                        .filter(method -> method.hasAnnotation(Component.class))
                        .anyMatch(Predicate.not(MethodInfo::isStatic)));
        instantiatedComponentMethodClasses.removeAll(componentClasses);

        ClassInfoList instantiatedClasses = new ClassInfoList(componentClasses);
        instantiatedClasses.addAll(instantiatedComponentMethodClasses);

        logInstantiatedClasses(instantiatedClasses);

        warnNonComponentClassesWithAnnotation(scan
                .getAllClasses()
                .filter(this::whitelistBlacklistFilter)
                .filter(this::hasInjectOrInitializeAnnotation)
                .filter(info -> !instantiatedClasses.contains(info) &&
                        componentMethodClasses
                                .stream()
                                .map(ClassInfo::getDeclaredMethodInfo)
                                .flatMap(MethodInfoList::stream)
                                .filter(method -> method.hasAnnotation(Component.class))
                                .map(MethodInfo::getTypeDescriptor)
                                .map(MethodTypeSignature::toString)
                                .map(str -> str.split(" ", 2)[0])
                                .noneMatch(info.getName()::equals)));

        MethodInfoList constructors = instantiatedClasses
                .stream()
                .map(this::getValidConstructor)
                .collect(Collectors.toCollection(MethodInfoList::new));

        logInvokedConstructors(constructors);

        MethodInfoList methods = componentMethodClasses
                .stream()
                .flatMap(info -> info
                        .getDeclaredMethodInfo()
                        .stream()
                        .filter(method -> method.hasAnnotation(Component.class)))
                .collect(Collectors.toCollection(MethodInfoList::new));

        logInvokedMethods(methods);

        MethodInfoList executables = new MethodInfoList(constructors);
        executables.addAll(methods);

        ComponentDataFactory factory = new ComponentDataFactory(scan.getAllClassesAsMap());

        List<ComponentData<?>> components = executables
                .stream()
                .map(factory::buildComponentData)
                .collect(Collectors.toList());

        logComponents(components);

        parseDependencies(components);

        checkCyclicDependencies(components);

        Map<ComponentData<?>,Integer> orderMap = getOrder(components);

        return new InjectorImpl(factory, components.stream()
                .sorted(Comparator.comparing(orderMap::get)));
    }

    private void checkAndLogWhitelistAndBlacklist() {
        if(!packageWhitelist.isEmpty()) {
            logger.info(packageWhitelistPresent(packageWhitelist.size()));
            logger.debug(whitelistOrBlacklistRules(packageWhitelist));
            packageWhitelist.add(Pattern.quote(InjectorImpl.class.getPackageName()));
        }

        if(!packageBlacklist.isEmpty()) {
            logger.info(packageBlacklistPresent(packageBlacklist.size()));
            logger.debug(whitelistOrBlacklistRules(packageBlacklist));
        }

        if(!classWhitelist.isEmpty()) {
            logger.info(classWhitelistPresent(classWhitelist.size()));
            logger.debug(whitelistOrBlacklistRules(classWhitelist));
            classWhitelist.add(Pattern.quote(InjectorImpl.class.getName()));
        }

        if(!classBlacklist.isEmpty()) {
            logger.info(classBlacklistPresent(classBlacklist.size()));
            logger.debug(whitelistOrBlacklistRules(classBlacklist));
        }
    }

    private boolean hasInjectOrInitializeAnnotation(ClassInfo info) {
        return info.hasDeclaredMethodAnnotation(Initialize.class) ||
                info.hasDeclaredFieldAnnotation(Inject.class) ||
                info.getDeclaredConstructorInfo().stream().anyMatch(method ->
                        method.hasAnnotation(Inject.class));
    }

    private void logComponentClasses(ClassInfoList classes) {
        logger.info(componentClassesFound(classes.size()));
        if(!classes.isEmpty()) {
            logger.debug(componentClasses(classes));
        }
    }

    private void logComponentMethodClasses(ClassInfoList classes) {
        logger.info(componentMethodClassesFound(classes.size()));
        if(!classes.isEmpty()) {
            logger.debug(componentMethodClasses(classes));
        }
    }

    private void logInstantiatedClasses(ClassInfoList classes) {
        logger.info(instantiatedClassesFound(classes.size()));
        if(!classes.isEmpty()) {
            logger.debug(instantiatedClasses(classes));
        }
    }

    private void logInvokedConstructors(MethodInfoList constructors) {
        logger.info(invokedConstructorsFound(constructors.size()));
        if(!constructors.isEmpty()) {
            logger.debug(invokedConstructors(constructors));
        }
    }

    private void logInvokedMethods(MethodInfoList methods) {
        logger.info(invokedMethodsFound(methods.size()));
        if(!methods.isEmpty()) {
            logger.debug(invokedMethods(methods));
        }
    }

    private void logComponents(List<ComponentData<?>> components) {
        logger.info(componentsFound(components.size()));
        if(!components.isEmpty()) {
            logger.debug(components(components));
        }
    }

    private void warnNonComponentClassesWithAnnotation(ClassInfoList classes) {
        classes.forEach(this::warnNonComponentClassesWithAnnotation);
    }

    private void warnNonComponentClassesWithAnnotation(ClassInfo info) {
        MethodInfoList constructors = info
                .getDeclaredConstructorInfo()
                .filter(method -> method.hasAnnotation(Inject.class));

        constructors.forEach(constructor -> logger.warn(
                nonComponentClassWithInjectConstructor(constructor)));

        FieldInfoList fields = info
                .getDeclaredFieldInfo()
                .filter(field -> field.hasAnnotation(Inject.class));

        fields.forEach(field -> logger.warn(
                nonComponentClassWithInjectField(field)));

        MethodInfoList methods = info
                .getDeclaredMethodInfo()
                .filter(method -> method.hasAnnotation(Initialize.class));

        methods.forEach(method -> logger.warn(
                nonComponentClassWithInitializeMethod(method)));
    }

    private boolean whitelistBlacklistFilter(ClassInfo info) {
        if(!classWhitelist.isEmpty() && classWhitelist.stream().noneMatch(info.getName()::matches)) {
            return false;
        }
        if(!packageWhitelist.isEmpty() && packageWhitelist.stream().noneMatch(info.getPackageName()::matches)) {
            return false;
        }
        return classBlacklist.stream().noneMatch(info.getName()::matches) &&
                packageBlacklist.stream().noneMatch(info.getPackageName()::matches);
    }

    private MethodInfo getValidConstructor(ClassInfo info) {
        MethodInfoList list = info
                .getDeclaredConstructorInfo()
                .filter(method -> method.hasAnnotation(Inject.class));

        if(list.isEmpty()) {
            list = info.getDeclaredConstructorInfo().filter(method ->
                method.getParameterInfo().length == 0);
            if(list.isEmpty()) {
                throw LogUtils.logExceptionAndGet(logger,
                        noConstructor(info),
                        ComponentClassWithNoValidConstructorsException::new);
            }
        }
        if(list.size() > 1) {
            throw LogUtils.logExceptionAndGet(logger,
                    multipleConstructors(info, list),
                    ComponentClassWithMultipleValidConstructorsException::new);
        }
        return list.get(0);
    }

    private void parseDependencies(List<ComponentData<?>> components) {
        logger.info(BUILDING_DEPENDENCY_GRAPH);
        for(ComponentData<?> component : components) {
            component.setDependencies(component
                    .getParameters()
                    .stream()
                    .map(ClassReference::getActualReference)
                    .map(data -> parseDependency(data, components))
                    .collect(Collectors.toCollection(ArrayList::new)));
            if(component.getOwner() != null) {
                List<ComponentData<?>> owners = components
                        .stream()
                        .filter(data -> data.getReference() != null)
                        .filter(data -> component.getOwner().equals(data.getReference()))
                        .collect(Collectors.toList());
                component.getDependencies().add(owners);
            }
            component.getDependencies().addAll(component
                    .getFields()
                    .stream()
                    .map(FieldReference::getReference)
                    .map(ClassReference::getActualReference)
                    .map(data -> parseDependency(data, components))
                    .collect(Collectors.toList()));
        }
    }

    private List<ComponentData<?>> parseDependency(ClassReference<?> classReference, List<ComponentData<?>> components) {
        return components
                .stream()
                .filter(data -> data.getReference() != null)
                .filter(data -> classReference.isAssignableFrom(data.getReference().getActualReference()))
                .collect(Collectors.toList());
    }

    private void checkCyclicDependencies(List<ComponentData<?>> components) {
        logger.info(CHECKING_CYCLIC);
        for(ComponentData<?> component : components) {
            checkCyclicDependencies(new ArrayDeque<>(), component);
        }
    }

    private void checkCyclicDependencies(Deque<ComponentData<?>> visited, ComponentData<?> current) {
        if(visited.contains(current)) {
            throw LogUtils.logExceptionAndGet(logger,
                    cyclicDependencyDetected(current, visited),
                    CyclicDependencyException::new);
        }
        if(current.getDependencies().isEmpty()) {
            return;
        }
        visited.push(current);
        current.getDependencies()
                .stream()
                .flatMap(List::stream)
                .forEach(dependency -> checkCyclicDependencies(visited, dependency));
        visited.pop();
    }

    private Map<ComponentData<?>,Integer> getOrder(List<ComponentData<?>> components) {
        List<ComponentData<?>> remaining = new ArrayList<>(components);
        Map<ComponentData<?>,Integer> map = new HashMap<>();
        int order = 0;
        while(!remaining.isEmpty()) {
            for(ComponentData<?> data : new ArrayList<>(remaining)) {
                if(data.getDependencies().stream().flatMap(List::stream).allMatch(map::containsKey)) {
                    map.put(data, order);
                    remaining.remove(data);
                }
            }
            order++;
        }
        return map;
    }
}
