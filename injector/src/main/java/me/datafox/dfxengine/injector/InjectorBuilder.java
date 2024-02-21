package me.datafox.dfxengine.injector;

import io.github.classgraph.*;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.*;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ClassReferenceFactory;
import me.datafox.dfxengine.injector.internal.ComponentData;
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
 * @author datafox
 */
public class InjectorBuilder {
    private static final ScanResult scan;

    static {
        scan = new ClassGraph().enableAllInfo().enableSystemJarsAndModules().scan();
    }

    private final Logger logger;
    private final List<String> packageWhitelist;
    private final List<String> packageBlacklist;
    private final List<String> classWhitelist;
    private final List<String> classBlacklist;
    private boolean closeScan;

    InjectorBuilder() {
        logger = LoggerFactory.getLogger(InjectorBuilder.class);
        packageWhitelist = new ArrayList<>();
        packageBlacklist = new ArrayList<>();
        classWhitelist = new ArrayList<>();
        classBlacklist = new ArrayList<>();
        closeScan = true;
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

    public InjectorBuilder closeScan(boolean close) {
        if(!close) {
            logger.warn(NOT_CLOSING_SCAN);
        }
        closeScan = close;
        return this;
    }

    public Injector build() {
        logger.info(SCANNING_CLASSPATH);
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

        ClassReferenceFactory factory = new ClassReferenceFactory(scan.getAllClassesAsMap());

        List<ComponentData<?>> components = executables
                .stream()
                .map(factory::buildComponentData)
                .collect(Collectors.toList());

        logComponents(components);

        if(closeScan) {
            scan.close();
        }

        parseDependencies(components);

        checkCyclicDependencies(components);

        Map<ComponentData<?>,Integer> orderMap = getOrder(components);

        return new Injector(components.stream()
                .sorted(Comparator.comparing(orderMap::get)));
    }

    private void checkAndLogWhitelistAndBlacklist() {
        if(!packageWhitelist.isEmpty()) {
            logger.info(packageWhitelistPresent(packageWhitelist.size()));
            logger.debug(whitelistOrBlacklistRules(packageWhitelist));
            packageWhitelist.add(Pattern.quote(Injector.class.getPackageName()));
        }

        if(!packageBlacklist.isEmpty()) {
            logger.info(packageBlacklistPresent(packageBlacklist.size()));
            logger.debug(whitelistOrBlacklistRules(packageBlacklist));
        }

        if(!classWhitelist.isEmpty()) {
            logger.info(classWhitelistPresent(classWhitelist.size()));
            logger.debug(whitelistOrBlacklistRules(classWhitelist));
            classWhitelist.add(Pattern.quote(Injector.class.getName()));
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
                        "Class " + info.loadClass() + " has no valid constructors and cannot be instantiated",
                        ComponentClassWithNoValidConstructorsException::new);
            }
        }
        if(list.size() > 1) {
            throw LogUtils.logExceptionAndGet(logger,
                    "Class " + info.loadClass() + " has multiple valid constructors " + list + " and cannot be instantiated",
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
            checkDependencies(component);
        }
    }

    private List<ComponentData<?>> parseDependency(ClassReference<?> classReference, List<ComponentData<?>> components) {
        return components
                .stream()
                .filter(data -> classReference.isAssignableFrom(data.getReference().getActualReference()))
                .collect(Collectors.toList());
    }

    private void checkDependencies(ComponentData<?> data) {
        for(int i = 0; i < data.getDependencies().size(); i++) {
            ClassReference<?> reference;
            if(i < data.getParameters().size()) {
                reference = data.getParameters().get(i);
            } else if(i == data.getParameters().size() && data.getOwner() != null) {
                reference = data.getOwner();
            } else {
                int index = i - data.getParameters().size();
                if(data.getOwner() != null) {
                    index--;
                }
                reference = data.getFields().get(index).getReference();
            }
            if(InstantiationDetails.class.equals(reference.getActualReference().getType())) {
                continue;
            }
            List<ComponentData<?>> dependency = data.getDependencies().get(i);
            if(dependency.isEmpty()) {
                throw LogUtils.logExceptionAndGet(logger,
                        data.getExecutable() + " depends on " + reference + " but none are found",
                        ComponentWithUnresolvedDependency::new);
            } else if(dependency.size() > 1 && !reference.isList()) {
                if(dependency.stream().filter(Predicate.not(ComponentData::isDefaultImpl)).count() != 1) {
                    throw LogUtils.logExceptionAndGet(logger,
                            data.getExecutable() + " depends on single " + reference + " but many were found",
                            ComponentWithMultipleOptionsForSingletonDependency::new);
                }
            }
        }
    }

    private void checkCyclicDependencies(List<ComponentData<?>> components) {
        logger.info(CHECKING_CYCLIC);
        for(ComponentData<?> component : components) {
            checkCyclicDependencies(new Stack<>(), component);
        }
    }

    private void checkCyclicDependencies(Stack<ComponentData<?>> visited, ComponentData<?> current) {
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
