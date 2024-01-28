package me.datafox.dfxengine.injector.utils;

import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.injector.InstantiationDetails;
import me.datafox.dfxengine.utils.StringUtils;

import java.lang.reflect.*;
import java.util.Collection;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
@SuppressWarnings({"MissingJavadoc", "SameParameterValue"})
public class InjectorStrings {
    public static final String BUILD_STARTED = "Building injector";
    public static final String SCANNING_CLASSPATH = "Scanning classpath";
    public static final String RESOLVING_DEPENDENCIES = "Resolving dependencies";
    public static final String CHECKING_CYCLIC = "Checking for cyclic dependencies";
    public static final String DETERMINING_ORDER = "Determining dependency instantiation order";
    public static final String INITIALIZING_INJECTOR = "Initializing injector";
    public static final String INSTANTIATING_COMPONENTS = "Instantiating components";
    public static final String FINALIZING_INJECTOR = "Finalizing injector";
    public static final String BUILD_FINISHED = "Build finished successfully";
    public static final String DEFAULT_CONSTRUCTOR = "Default constructor found";
    public static final String RUNNING_INITIALIZERS = "Running initializers";
    public static final String ODD_LIST_TYPE =
            "List's parameterized type could not be determined, defaulting to Object.class";

    private static final String PACKAGE_WHITELIST_PRESENT = "Package whitelist present with values: %s";
    private static final String PACKAGE_BLACKLIST_PRESENT = "Package blacklist present with values: %s";
    private static final String CLASS_WHITELIST_PRESENT = "Class whitelist present with values: %s";
    private static final String CLASS_BLACKLIST_PRESENT = "Class blacklist present with values: %s";
    private static final String CHECKING_PARAMETERIZED = "Checking for parameterized types in %s";
    private static final String PARAMETERIZED_TYPE = "%s has a parameterized type, this is " +
            "not supported and may cause runtime exceptions or other unexpected behavior";
    private static final String PARAMETERIZED_TYPE_DEPENDENCY = "%s has a parameterized dependency to " +
            "%s, this is not supported and may cause runtime exceptions or other unexpected behavior";
    private static final String PARAMETERIZED_TYPE_METHOD_DEPENDENCY = "Method %s in %s has a parameterized " +
            "dependency to %s, this is not supported and may cause runtime exceptions or other unexpected behavior";
    private static final String COMPONENT_CLASS_FOUND = "Found component %s";
    private static final String INVALID_OVERRIDE_TYPE =
            "Component %s has an override type %s which is not the component's superclass or -interface";
    private static final String COMPONENT_METHOD_FOUND = "Found component method %s in %s";
    private static final String INVALID_OVERRIDE_TYPE_METHOD =
            "Component %s in method %s has an override type %s which is not the component's superclass or -interface";
    private static final String RESOLVING_COMPONENT_DEPENDENCIES = "Resolving dependencies for %s";
    private static final String RESOLVING_METHOD_DEPENDENCIES = "Resolving dependencies for method %s in %s";
    private static final String METHOD_NOT_STATIC = "Method is not static, registering declaring %s as a dependency";
    private static final String CHECKING_CYCLIC_FOR = "Checking cyclic dependencies for %s, currently visiting %s";
    private static final String CYCLIC_DETECTED = "Cyclic dependency for %s detected at %s";
    private static final String NO_DEPENDENCIES_ORDER = "%s has no dependencies, adding to the order map with priority 0";
    private static final String ALL_DEPENDENCIES_ORDER =
            "All dependencies of %s have been resolved, adding to the order map with priority %s";
    private static final String REGISTERING_PER_INSTANCE_CLASS = "Registering per instance component %s";
    private static final String REGISTERING_PER_INSTANCE_METHOD = "Registering per instance component method %s in %s";
    private static final String INSTANTIATING_COMPONENT = "Instantiating %s";
    private static final String INSTANTIATED_BY_CONSTRUCTOR = "%s is instantiated by constructor";
    private static final String INSTANTIATED_BY_METHOD = "%s is instantiated by method %s in %s";
    private static final String VALID_CONSTRUCTORS = "Checking for valid constructors in %s";
    private static final String NO_VALID_CONSTRUCTOR = "No valid constructor found in %s";
    private static final String MULTIPLE_INJECT_CONSTRUCTORS =
            "Multiple constructors annotated with @Inject are present in %s but only one is allowed to be present";
    private static final String VALID_CONSTRUCTOR_FOUND = "Valid constructor %s found";
    private static final String REGISTERING_DEPENDENCY = "Registering %s as a dependency for %s";
    private static final String FETCHING_COMPONENTS = "Fetching components for %s";
    private static final String FETCHING_SINGLETON = "Fetching singleton component for %s";
    private static final String UNKNOWN_COMPONENT = "No component for %s is present";
    private static final String MULTIPLE_VALID_COMPONENTS =
            "Multiple valid components for %s are present but a singleton was requested";
    private static final String REGISTERING_COMPONENT = "Registering component %s";
    private static final String INSTANTIATING_CONSTRUCTOR = "Instantiating constructor %s";
    private static final String COULD_NOT_INSTANTIATE_CONSTRUCTOR = "Could not instantiate constructor %s";
    private static final String INSTANTIATING_PER_INSTANCE = "Instantiating per instance component %s, requested by %s";
    private static final String EXECUTABLE_NOT_CONSTRUCTOR_OR_METHOD =
            "Executable %s in %s is not a constructor or a method";
    private static final String INVOKING_METHOD = "Invoking method %s in %s";
    private static final String INVOKING_STATIC_METHOD = "Invoking static method %s";
    private static final String COULD_NOT_INVOKE_METHOD = "Could not invoke method %s in %s";
    private static final String FETCHING_METHOD_DEPENDENCIES = "Fetching dependencies in method %s in %s";
    private static final String FETCHING_CONSTRUCTOR_DEPENDENCIES = "Fetching dependencies in constructor %s";
    private static final String INITIALIZING_FIELDS = "Initializing fields for %s";
    private static final String INITIALIZING_FIELD = "Initializing field %s in %s";
    private static final String FIELD_INACCESSIBLE = "Could not access field %s in %s";
    private static final String REGISTERING_METHODS = "Registering initialization methods in %s";
    private static final String METHOD_REGISTERED = "Initialization method %s in %s registered";
    private static final String FETCHING_DEPENDENCY = "Fetching dependency %s for %s";
    private static final String LIST_DEPENDENCY = "List dependency for %s detected";
    private static final String MAYBE_ODD_LIST_TYPE = "List has multiple parameterized types, using %s";
    private static final String PARAMETERIZED_LIST_TYPE = "List's parameterized type %s is also parameterized, " +
            "the type parameter will be ignored which may cause runtime exceptions or unexpected behavior";

    public static String packageWhitelistPresent(Collection<String> packages) {
        return forCollection(PACKAGE_WHITELIST_PRESENT, packages);
    }

    public static String packageBlacklistPresent(Collection<String> packages) {
        return forCollection(PACKAGE_BLACKLIST_PRESENT, packages);
    }

    public static String classWhitelistPresent(Collection<String> classes) {
        return forCollection(CLASS_WHITELIST_PRESENT, classes);
    }

    public static String classBlacklistPresent(Collection<String> classes) {
        return forCollection(CLASS_BLACKLIST_PRESENT, classes);
    }

    public static String checkingParameterized(Class<?> type) {
        return forType(CHECKING_PARAMETERIZED, type);
    }

    public static String checkingParameterized(Type type) {
        return forActualType(CHECKING_PARAMETERIZED, type);
    }

    public static String parameterizedType(Class<?> type) {
        return forType(PARAMETERIZED_TYPE, type);
    }

    public static String parameterizedTypeDependency(Class<?> dependent, Type type) {
        return forTypeAndActualType(PARAMETERIZED_TYPE_DEPENDENCY, dependent, type);
    }

    public static String parameterizedTypeMethodDependency(InjectorBuilder.MethodReference<?,?> reference, Type type) {
        return forMethodAndTypeAndActualType(PARAMETERIZED_TYPE_METHOD_DEPENDENCY, reference.getMethod(), reference.getOwner(), type);
    }

    public static String componentClassFound(Class<?> type) {
        return forType(COMPONENT_CLASS_FOUND, type);
    }

    public static String invalidOverrideType(Class<?> type, Class<?> overrideType) {
        return forTwoTypes(INVALID_OVERRIDE_TYPE, type, overrideType);
    }

    public static String componentMethodFound(InjectorBuilder.MethodReference<?,?> reference) {
        return forMethodAndType(COMPONENT_METHOD_FOUND, reference.getMethod(), reference.getOwner());
    }

    public static String invalidMethodOverrideType(InjectorBuilder.MethodReference<?,?> reference, Class<?> overrideType) {
        return forTypeAndMethodAndType(INVALID_OVERRIDE_TYPE_METHOD, reference.getReturnType(), reference.getMethod(), overrideType);
    }

    public static String resolvingComponentDependencies(Class<?> type) {
        return forType(RESOLVING_COMPONENT_DEPENDENCIES, type);
    }

    public static String resolvingMethodDependencies(InjectorBuilder.MethodReference<?,?> reference) {
        return forMethodAndType(RESOLVING_METHOD_DEPENDENCIES, reference.getMethod(), reference.getOwner());
    }

    public static String methodNotStatic(Class<?> type) {
        return forType(METHOD_NOT_STATIC, type);
    }

    public static String checkingCyclicFor(Class<?> dependency, Class<?> visited) {
        return forTwoTypes(CHECKING_CYCLIC_FOR, dependency, visited);
    }

    public static String cyclicDetected(Class<?> dependency, Class<?> visited) {
        return forTwoTypes(CYCLIC_DETECTED, dependency, visited);
    }

    public static String noDependenciesOrder(Class<?> type) {
        return forType(NO_DEPENDENCIES_ORDER, type);
    }

    public static String allDependenciesOrder(Class<?> type, int priority) {
        return forTypeAndInt(ALL_DEPENDENCIES_ORDER, type, priority);
    }

    public static String registeringPerInstanceClass(Class<?> type) {
        return forType(REGISTERING_PER_INSTANCE_CLASS, type);
    }

    public static String registeringPerInstanceMethod(InjectorBuilder.MethodReference<?,?> reference) {
        return forMethodAndType(REGISTERING_PER_INSTANCE_METHOD, reference.getMethod(), reference.getOwner());
    }

    public static String instantiatingComponent(Class<?> type) {
        return forType(INSTANTIATING_COMPONENT, type);
    }

    public static String instantiatedByConstructor(Class<?> type) {
        return forType(INSTANTIATED_BY_CONSTRUCTOR, type);
    }

    public static String instantiatedByMethod(Class<?> type, InjectorBuilder.MethodReference<?,?> reference) {
        return forTypeAndMethodAndType(INSTANTIATED_BY_METHOD, type, reference.getMethod(), reference.getOwner());
    }

    public static String validConstructors(Class<?> type) {
        return forType(VALID_CONSTRUCTORS, type);
    }

    public static String noValidConstructor(Class<?> type) {
        return forType(NO_VALID_CONSTRUCTOR, type);
    }

    public static String multipleInjectConstructors(Class<?> type) {
        return forType(MULTIPLE_INJECT_CONSTRUCTORS, type);
    }

    public static String validConstructorFound(Constructor<?> constructor) {
        return forConstructor(VALID_CONSTRUCTOR_FOUND, constructor);
    }

    public static String registeringDependency(Class<?> dependency, Class<?> dependent) {
        return forTwoTypes(REGISTERING_DEPENDENCY, dependency, dependent);
    }

    public static String fetchingComponents(Class<?> type) {
        return forType(FETCHING_COMPONENTS, type);
    }

    public static String fetchingSingleton(Class<?> type) {
        return forType(FETCHING_SINGLETON, type);
    }

    public static String unknownComponent(Class<?> type) {
        return forType(UNKNOWN_COMPONENT, type);
    }

    public static String multipleValidComponents(Class<?> type) {
        return forType(MULTIPLE_VALID_COMPONENTS, type);
    }

    public static String registeringComponent(Class<?> type) {
        return forType(REGISTERING_COMPONENT, type);
    }

    public static String instantiatingConstructor(Constructor<?> constructor) {
        return forConstructor(INSTANTIATING_CONSTRUCTOR, constructor);
    }

    public static String couldNotInstantiateConstructor(Constructor<?> constructor) {
        return forConstructor(COULD_NOT_INSTANTIATE_CONSTRUCTOR, constructor);
    }

    public static String instantiatingPerInstance(InstantiationDetails<?,?> instantiationDetails) {
        if(instantiationDetails.getRequestingType() == null) {
            return forTypeAndString(INSTANTIATING_PER_INSTANCE, instantiationDetails.getType(), "null");
        }
        return forTwoTypes(INSTANTIATING_PER_INSTANCE, instantiationDetails.getType(),
                instantiationDetails.getRequestingType());
    }

    public static String executableNotConstructorOrMethod(Injector.PerInstanceReference<?,?> reference) {
        return forExecutableAndType(EXECUTABLE_NOT_CONSTRUCTOR_OR_METHOD, reference.getExecutable(), reference.getOwner());
    }

    public static String invokingMethod(Method method, Class<?> type) {
        return forExecutableAndType(INVOKING_METHOD, method, type);
    }

    public static String invokingStaticMethod(Method method) {
        return forMethod(INVOKING_STATIC_METHOD, method);
    }

    public static String couldNotInvokeMethod(Method method, Class<?> type) {
        return forMethodAndType(COULD_NOT_INVOKE_METHOD, method, type);
    }

    public static String fetchingMethodDependencies(Method method, Class<?> type) {
        return forMethodAndType(FETCHING_METHOD_DEPENDENCIES, method, type);
    }

    public static String fetchingConstructorDependencies(Constructor<?> constructor) {
        return forConstructor(FETCHING_CONSTRUCTOR_DEPENDENCIES, constructor);
    }

    public static String initializingFields(Class<?> type) {
        return forType(INITIALIZING_FIELDS, type);
    }

    public static String initializingField(Field field, Class<?> type) {
        return forFieldAndType(INITIALIZING_FIELD, field, type);
    }

    public static String fieldInaccessible(Field field, Class<?> type) {
        return forFieldAndType(FIELD_INACCESSIBLE, field, type);
    }

    public static String registeringMethods(Class<?> type) {
        return forType(REGISTERING_METHODS, type);
    }

    public static String methodRegistered(Method method, Class<?> type) {
        return forExecutableAndType(METHOD_REGISTERED, method, type);
    }

    public static String fetchingDependency(Class<?> type, Class<?> requesting) {
        return forTwoTypes(FETCHING_DEPENDENCY, type, requesting);
    }

    public static String listDependency(Class<?> type) {
        return forType(LIST_DEPENDENCY, type);
    }

    public static String maybeOddListType(Class<?> type) {
        return forType(MAYBE_ODD_LIST_TYPE, type);
    }

    public static String parameterizedListType(Class<?> type) {
        return forType(PARAMETERIZED_LIST_TYPE, type);
    }

    private static String forCollection(String str, Collection<String> list) {
        return String.format(str, StringUtils.replaceLast(String.join(", ", list), ", ", " and "));
    }

    private static String forType(String str, Class<?> type) {
        return String.format(str, StringUtils.className(type));
    }

    private static String forTypeAndInt(String str, Class<?> type, int integer) {
        return String.format(str, StringUtils.className(type), integer);
    }

    private static String forTypeAndString(String str, Class<?> type, String string) {
        return String.format(str, StringUtils.className(type), string);
    }

    private static String forTwoTypes(String str, Class<?> type1, Class<?> type2) {
        return String.format(str, StringUtils.className(type1), StringUtils.className(type2));
    }

    private static String forFieldAndType(String str, Field field, Class<?> type) {
        return String.format(str, field.getName(), StringUtils.className(type));
    }

    private static String forConstructor(String str, Constructor<?> constructor) {
        return String.format(str, StringUtils.constructorName(constructor));
    }

    private static String forMethod(String str, Method method) {
        return String.format(str, method.getName());
    }

    private static String forMethodAndType(String str, Method method, Class<?> type) {
        return String.format(str, method.getName(), StringUtils.className(type));
    }

    private static String forTypeAndMethodAndType(String str, Class<?> type1, Method method, Class<?> type2) {
        return String.format(str, StringUtils.className(type1),
                method.getName(),
                StringUtils.className(type2));
    }

    private static String forExecutableAndType(String str, Executable executable, Class<?> owner) {
        return String.format(str, executable.getName(),
                StringUtils.className(owner));
    }

    private static String forActualType(String str, Type type) {
        return String.format(str, StringUtils.typeName(type));
    }

    private static String forTypeAndActualType(String str, Class<?> type1, Type type2) {
        return String.format(str, StringUtils.className(type1), StringUtils.typeName(type2));
    }

    private static String forMethodAndTypeAndActualType(String str, Method method, Class<?> type1, Type type2) {
        return String.format(str, method.getName(),
                StringUtils.className(type1),
                StringUtils.typeName(type2));
    }
}
