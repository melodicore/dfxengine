package me.datafox.dfxengine.injector;

import io.github.classgraph.*;
import lombok.Builder;
import lombok.Singular;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.exception.ComponentClassWithMultipleValidConstructorsException;
import me.datafox.dfxengine.injector.exception.ComponentClassWithNoValidConstructorsException;
import me.datafox.dfxengine.injector.internal.ClassHierarchyFactory;
import me.datafox.dfxengine.injector.serialization.ClassHierarchy;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.datafox.dfxengine.injector.utils.InjectorStrings.*;

/**
 * A class for scanning a full classpath into a {@link ClassHierarchy}.
 *
 * @author datafox
 */
public class ClassScanner {
    private final Logger logger;
    private final List<String> whitelistedPackages;
    private final List<String> blacklistedPackages;
    private final List<String> whitelistedClasses;
    private final List<String> blacklistedClasses;

    /**
     * Constructs a new class scanner. All whitelists and blacklists use regular expressions.
     *
     * @param whitelistedPackages packages to whitelist
     * @param blacklistedPackages packages to blacklist
     * @param whitelistedClasses classes to whitelist
     * @param blacklistedClasses classes to blacklist
     */
    @Builder
    public ClassScanner(@Singular List<String> whitelistedPackages,
                        @Singular List<String> blacklistedPackages,
                        @Singular List<String> whitelistedClasses,
                        @Singular List<String> blacklistedClasses) {
        this.logger = LoggerFactory.getLogger(ClassScanner.class);
        this.whitelistedPackages = new ArrayList<>(whitelistedPackages);
        this.blacklistedPackages = new ArrayList<>(blacklistedPackages);
        this.whitelistedClasses = new ArrayList<>(whitelistedClasses);
        this.blacklistedClasses = new ArrayList<>(blacklistedClasses);
    }

    /**
     * Scans a class hierarchy with default settings.
     *
     * @return scanned class hierarchy
     */
    public ClassHierarchy scan() {
        return scan(false);
    }

    /**
     * Scans a class hierarchy with default settings with optional logging.
     *
     * @param logIgnored logs ignored classes if {@code true}
     * @return scanned class hierarchy
     */
    public ClassHierarchy scan(boolean logIgnored) {
        try(ScanResult scan = new ClassGraph()
                .enableAllInfo()
                .enableSystemJarsAndModules()
                .rejectPackages("jdk.*", "oracle.*", "sun.*", "com.sun.*", "jakarta.*", "org.gradle.*", "javax.*")
                .scan()) {
            return scan(scan, logIgnored);
        }
    }

    /**
     * Scans a class hierarchy from a {@link ClassGraph} {@link ScanResult}.
     *
     * @param logIgnored logs ignored classes if {@code true}
     * @return scanned class hierarchy
     */
    public ClassHierarchy scan(ScanResult scan, boolean logIgnored) {
        checkAndLogWhitelistAndBlacklist();

        ClassInfoList componentClasses = scan
                .getClassesWithAnnotation(Component.class)
                .filter(this::whitelistBlacklistFilter);

        logComponentClasses(componentClasses);

        ClassInfoList componentMethodClasses = scan
                .getClassesWithMethodAnnotation(Component.class)
                .filter(this::whitelistBlacklistFilter);

        logComponentMethodClasses(componentMethodClasses);

        ClassInfoList eventMethodClasses = scan
                .getClassesWithMethodAnnotation(EventHandler.class)
                .filter(this::whitelistBlacklistFilter);

        logEventMethodClasses(eventMethodClasses);

        ClassInfoList instantiatedComponentMethodClasses =
                componentMethodClasses.filter(info -> info
                        .getDeclaredMethodInfo()
                        .stream()
                        .filter(method -> method.hasAnnotation(Component.class))
                        .anyMatch(Predicate.not(MethodInfo::isStatic)));

        ClassInfoList instantiatedClasses = Stream.concat(componentClasses.stream(),
                        instantiatedComponentMethodClasses.stream())
                .distinct()
                .collect(Collectors.toCollection(ClassInfoList::new));

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

        MethodInfoList events = eventMethodClasses
                .stream()
                .flatMap(info -> info
                        .getDeclaredMethodInfo()
                        .stream()
                        .filter(method -> method.hasAnnotation(EventHandler.class)))
                .collect(Collectors.toCollection(MethodInfoList::new));

        logEvents(events);

        MethodInfoList executables = new MethodInfoList(constructors);
        executables.addAll(methods);

        return new ClassHierarchyFactory(logIgnored).build(scan.getAllClassesAsMap(), executables, events);
    }

    private void checkAndLogWhitelistAndBlacklist() {
        if(!whitelistedPackages.isEmpty()) {
            logger.info(packageWhitelistPresent(whitelistedPackages.size()));
            logger.debug(whitelistOrBlacklistRules(whitelistedPackages));
            whitelistedPackages.add(Pattern.quote(InjectorImpl.class.getPackageName()));
        }

        if(!blacklistedPackages.isEmpty()) {
            logger.info(packageBlacklistPresent(blacklistedPackages.size()));
            logger.debug(whitelistOrBlacklistRules(blacklistedPackages));
        }

        if(!whitelistedClasses.isEmpty()) {
            logger.info(classWhitelistPresent(whitelistedClasses.size()));
            logger.debug(whitelistOrBlacklistRules(whitelistedClasses));
            whitelistedClasses.add(Pattern.quote(InjectorImpl.class.getName()));
        }

        if(!blacklistedClasses.isEmpty()) {
            logger.info(classBlacklistPresent(blacklistedClasses.size()));
            logger.debug(whitelistOrBlacklistRules(blacklistedClasses));
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

    private void logEventMethodClasses(ClassInfoList classes) {
        logger.info(eventMethodClassesFound(classes.size()));
        if(!classes.isEmpty()) {
            logger.debug(eventMethodClasses(classes));
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

    private void logEvents(MethodInfoList events) {
        logger.info(eventsFound(events.size()));
        if(!events.isEmpty()) {
            logger.debug(events(events));
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
        if(!whitelistedClasses.isEmpty() && whitelistedClasses.stream().noneMatch(info.getName()::matches)) {
            return false;
        }
        if(!whitelistedPackages.isEmpty() && whitelistedPackages.stream().noneMatch(info.getPackageName()::matches)) {
            return false;
        }
        return blacklistedClasses.stream().noneMatch(info.getName()::matches) &&
                blacklistedPackages.stream().noneMatch(info.getPackageName()::matches);
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
}