package me.datafox.dfxengine.injector.utils;

import io.github.classgraph.*;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.injector.serialization.ExecutableData;
import me.datafox.dfxengine.injector.serialization.FieldData;
import me.datafox.dfxengine.utils.StringUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
@SuppressWarnings("MissingJavadoc")
public class InjectorStrings {
    public static final String SCANNING_CLASSPATH = "Scanning classpath";
    public static final String LOADING_SCAN = "Loading scanned classpath from json";
    public static final String BUILDING_DEPENDENCY_GRAPH = "Building dependency graph";
    public static final String CHECKING_CYCLIC = "Checking for cyclic dependencies";
    public static final String SOMETHING_WENT_WRONG = "Oh no, something went wrong!";
    public static final String NO_OUTPUT_DIR = "Please specify output directory";

    private static final String WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR = "%s %slist present with %s rule";
    private static final String WHITELIST_OR_BLACKLIST_PRESENT_PLURAL = "%s %slist present with %s rules";
    private static final String WHITELIST_OR_BLACKLIST_RULES = "Rules: %s";
    private static final String COMPONENT_CLASSES_FOUND_SINGULAR = "Found %s component class";
    private static final String COMPONENT_CLASSES_FOUND_PLURAL = "Found %s component classes";
    private static final String COMPONENT_CLASSES = "Component classes: %s";
    private static final String COMPONENT_METHOD_CLASSES_FOUND_SINGULAR = "Found %s class declaring component methods";
    private static final String COMPONENT_METHOD_CLASSES_FOUND_PLURAL = "Found %s classes declaring component methods";
    private static final String COMPONENT_METHOD_CLASSES = "Component method classes: %s";
    private static final String EVENT_METHOD_CLASSES_FOUND_SINGULAR = "Found %s class declaring event handler methods";
    private static final String EVENT_METHOD_CLASSES_FOUND_PLURAL = "Found %s classes declaring event handler methods";
    private static final String EVENT_METHOD_CLASSES = "Event method handler classes: %s";
    private static final String INSTANTIATED_CLASSES_FOUND_SINGULAR = "Found %s class to instantiate";
    private static final String INSTANTIATED_CLASSES_FOUND_PLURAL = "Found %s classes to instantiate";
    private static final String INSTANTIATED_CLASSES = "Instantiated classes: %s";
    private static final String INVOKED_CONSTRUCTORS_FOUND_SINGULAR = "Found %s constructor to invoke";
    private static final String INVOKED_CONSTRUCTORS_FOUND_PLURAL = "Found %s constructors to invoke";
    private static final String INVOKED_CONSTRUCTORS = "Invoked constructors: %s";
    private static final String INVOKED_METHODS_FOUND_SINGULAR = "Found %s method to invoke";
    private static final String INVOKED_METHODS_FOUND_PLURAL = "Found %s methods to invoke";
    private static final String INVOKED_METHODS = "Invoked methods: %s";
    private static final String EVENTS_FOUND_SINGULAR = "Found %s event handler";
    private static final String EVENTS_FOUND_PLURAL = "Found %s event handlers";
    private static final String EVENTS = "Event handlers: %s";
    private static final String COMPONENTS_FOUND_SINGULAR = "Found %s declared component";
    private static final String COMPONENTS_FOUND_PLURAL = "Found %s declared components";
    private static final String COMPONENTS = "Declared components: %s";
    private static final String NON_COMPONENT_CLASS_WITH_INJECT_CONSTRUCTOR = "Class %s is not recognised " +
            "as a component but has a constructor %s annotated with @Inject. This constructor will be ignored";
    private static final String NON_COMPONENT_CLASS_WITH_INJECT_FIELD = "Class %s is not recognised " +
            "as a component but has a field %s annotated with @Inject. This field will be ignored";
    private static final String NON_COMPONENT_CLASS_WITH_INITIALIZE_METHOD = "Class %s is not recognised " +
            "as a component but has a method %s annotated with @Initialize. This method will be ignored";
    private static final String MULTIPLE_CONSTRUCTORS =
            "Class %s has multiple constructors annotated with @Inject and cannot be instantiated: %s";
    private static final String NO_CONSTRUCTOR =
            "Class %s has no default constructor or a constructor annotated with @Inject and cannot be instantiated";
    private static final String BUILDING_COMPONENT_CLASS_DATA =
            "Building component data for class %s using constructor %s";
    private static final String BUILDING_VOID_COMPONENT_DATA = "Building void component data using method %s";
    private static final String BUILDING_COMPONENT_METHOD_DATA = "Building component data for class %s using method %s";
    private static final String PER_INSTANCE_VOID_COMPONENT =
            "Void component method %s has InstantiationPolicy.PER_INSTANCE, but void methods are only invoked once";
    private static final String PER_INSTANCE_COMPONENT_EVENT =
            "Component %s has InstantiationPolicy.PER_INSTANCE but contains non-static event handler methods. " +
            "Non-static event handler methods are not invoked in PER_INSTANCE components";
    private static final String FINAL_FIELD_DEPENDENCY =
            "Field %s in class %s is annotated with @Inject but is final, only non-final fields can be injected";
    private static final String UNKNOWN_TYPE =
            "Class %s cannot be found";
    private static final String UNRESOLVED_TYPE_PARAMETER =
            "Component %s has unresolved type parameter, unresolved type parameters cannot be injected";
    private static final String INVALID_ARRAY = "Array %s cannot be resolved to a type";
    private static final String EVENT_PARAMETER_COUNT =
            "Event handler method %s has %s parameters but exactly one is required";
    private static final String PARAMETRIC_EVENT_WITHOUT_INTERFACE =
            "Event class %s has type parameters but does not implement ParametricEvent";
    private static final String NO_DEPENDENCIES = "A single Component %s was requested but none are present";
    private static final String MULTIPLE_DEPENDENCIES =
            "A single Component %s was requested but multiple are present";
    private static final String CYCLIC_DEPENDENCY_DETECTED = "Detected a cyclic dependency: %s";

    public static String packageWhitelistPresent(int rules) {
        return String.format(rules == 1 ?
                WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Package", "white", rules);
    }

    public static String packageBlacklistPresent(int rules) {
        return String.format(rules == 1 ?
                        WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                        WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Package", "black", rules);
    }

    public static String classWhitelistPresent(int rules) {
        return String.format(rules == 1 ?
                        WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                        WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Class", "white", rules);
    }

    public static String classBlacklistPresent(int rules) {
        return String.format(rules == 1 ?
                        WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                        WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Class", "black", rules);
    }

    public static String whitelistOrBlacklistRules(Collection<String> rules) {
        return String.format(WHITELIST_OR_BLACKLIST_RULES,
                "\"" + StringUtils.joining(rules, "\", \"", "\" and \"") + "\"");
    }

    public static String componentClassesFound(int classes) {
        return String.format(classes == 1 ?
                COMPONENT_CLASSES_FOUND_SINGULAR :
                COMPONENT_CLASSES_FOUND_PLURAL,
                classes);
    }

    public static String componentClasses(ClassInfoList infos) {
        return forClassInfoList(COMPONENT_CLASSES, infos);
    }

    public static String componentMethodClassesFound(int classes) {
        return String.format(classes == 1 ?
                        COMPONENT_METHOD_CLASSES_FOUND_SINGULAR :
                        COMPONENT_METHOD_CLASSES_FOUND_PLURAL,
                classes);
    }

    public static String componentMethodClasses(ClassInfoList infos) {
        return forClassInfoList(COMPONENT_METHOD_CLASSES, infos);
    }

    public static String eventMethodClassesFound(int classes) {
        return String.format(classes == 1 ?
                        EVENT_METHOD_CLASSES_FOUND_SINGULAR :
                        EVENT_METHOD_CLASSES_FOUND_PLURAL,
                classes);
    }

    public static String eventMethodClasses(ClassInfoList infos) {
        return forClassInfoList(EVENT_METHOD_CLASSES, infos);
    }

    public static String instantiatedClassesFound(int classes) {
        return String.format(classes == 1 ?
                        INSTANTIATED_CLASSES_FOUND_SINGULAR :
                        INSTANTIATED_CLASSES_FOUND_PLURAL,
                classes);
    }

    public static String instantiatedClasses(ClassInfoList infos) {
        return forClassInfoList(INSTANTIATED_CLASSES, infos);
    }

    public static String invokedConstructorsFound(int constructors) {
        return String.format(constructors == 1 ?
                        INVOKED_CONSTRUCTORS_FOUND_SINGULAR :
                        INVOKED_CONSTRUCTORS_FOUND_PLURAL,
                constructors);
    }

    public static String invokedConstructors(MethodInfoList constructors) {
        return forMethodInfoList(INVOKED_CONSTRUCTORS, constructors);
    }

    public static String invokedMethodsFound(int methods) {
        return String.format(methods == 1 ?
                        INVOKED_METHODS_FOUND_SINGULAR :
                        INVOKED_METHODS_FOUND_PLURAL,
                methods);
    }

    public static String invokedMethods(MethodInfoList methods) {
        return forMethodInfoList(INVOKED_METHODS, methods);
    }

    public static String eventsFound(int events) {
        return String.format(events == 1 ?
                        EVENTS_FOUND_SINGULAR :
                        EVENTS_FOUND_PLURAL,
                events);
    }

    public static String events(MethodInfoList events) {
        return forMethodInfoList(EVENTS, events);
    }

    public static String componentsFound(int components) {
        return String.format(components == 1 ?
                        COMPONENTS_FOUND_SINGULAR :
                        COMPONENTS_FOUND_PLURAL,
                components);
    }

    public static String components(List<ComponentData<?>> components) {
        return String.format(COMPONENTS, StringUtils.joining(components
                        .stream()
                        .map(InjectorStrings::getComponentString)
                        .collect(Collectors.toList()),
                ", ", " and "));
    }

    public static String nonComponentClassWithInjectConstructor(MethodInfo constructor) {
        return String.format(NON_COMPONENT_CLASS_WITH_INJECT_CONSTRUCTOR,
                getName(constructor.getClassInfo()), getMethodParameterString(constructor));
    }

    public static String nonComponentClassWithInjectField(FieldInfo field) {
        return String.format(NON_COMPONENT_CLASS_WITH_INJECT_FIELD,
                getName(field.getClassInfo()), getFieldParameterString(field));
    }

    public static String nonComponentClassWithInitializeMethod(MethodInfo method) {
        return String.format(NON_COMPONENT_CLASS_WITH_INITIALIZE_METHOD,
                getName(method.getClassInfo()), getMethodParameterString(method));
    }

    public static String multipleConstructors(ClassInfo info, MethodInfoList constructors) {
        return String.format(MULTIPLE_CONSTRUCTORS, getName(info),
                StringUtils.joining(constructors
                        .stream()
                        .map(InjectorStrings::getMethodParameterString)
                        .collect(Collectors.toList()), ", ", " and "));
    }

    public static String noConstructor(ClassInfo info) {
        return String.format(NO_CONSTRUCTOR, getName(info));
    }

    public static String buildingComponentClassData(String classString, ExecutableData constructor) {
        return String.format(BUILDING_COMPONENT_CLASS_DATA, classString, getMethodParameterString(constructor));
    }

    public static String buildingVoidComponentData(ExecutableData method) {
        return String.format(BUILDING_VOID_COMPONENT_DATA, getMethodParameterString(method));
    }

    public static String buildingComponentMethodData(String classString, ExecutableData method) {
        return String.format(BUILDING_COMPONENT_METHOD_DATA, classString, getMethodParameterString(method));
    }

    public static String perInstanceVoidComponent(ExecutableData method) {
        return String.format(PER_INSTANCE_VOID_COMPONENT, getMethodParameterString(method));
    }

    public static String perInstanceComponentEvent(ExecutableData method) {
        return String.format(PER_INSTANCE_COMPONENT_EVENT, getMethodParameterString(method));
    }

    public static String noDependenciesRuntime(ClassReference<?> reference) {
        return String.format(NO_DEPENDENCIES, reference.getSignature());
    }

    public static String multipleDependenciesRuntime(ClassReference<?> reference) {
        return String.format(MULTIPLE_DEPENDENCIES, reference.getSignature());
    }

    public static String finalFieldDependency(FieldData<?> field) {
        return String.format(FINAL_FIELD_DEPENDENCY, getFieldParameterString(field), field.getName());
    }

    public static String unknownType(String classString) {
        return String.format(UNKNOWN_TYPE, classString);
    }

    public static String unresolvedTypeParameter(String classString) {
        return String.format(UNRESOLVED_TYPE_PARAMETER, classString);
    }

    public static String invalidArray(String array) {
        return String.format(INVALID_ARRAY, array);
    }

    public static String eventParameterCount(ExecutableData event, int parameters) {
        return String.format(EVENT_PARAMETER_COUNT, event.getMethod(), parameters);
    }

    public static String parametricEventWithoutInterface(Object event) {
        return String.format(PARAMETRIC_EVENT_WITHOUT_INTERFACE, event.getClass().getName());
    }

    public static String cyclicDependencyDetected(ComponentData<?> current, Deque<ComponentData<?>> visited) {
        List<ComponentData<?>> list = new ArrayList<>(visited);
        int i = list.indexOf(current);
        if(i < 0) {
            return SOMETHING_WENT_WRONG;
        }
        list = list.subList(i, list.size());
        list.add(current);
        return String.format(CYCLIC_DEPENDENCY_DETECTED, list.stream()
                .map(ComponentData::getReference)
                .map(ClassReference::getActualReference)
                .map(ClassReference::getSignature)
                .collect(Collectors.joining(" -> ")));
    }

    private static String forClassInfoList(String str, ClassInfoList classes) {
        return String.format(str, StringUtils.joining(classes
                        .stream()
                        .map(InjectorStrings::getName)
                        .collect(Collectors.toList()),
                ", ", " and "));
    }

    private static String forMethodInfoList(String str, MethodInfoList methods) {
        return String.format(str, StringUtils.joining(methods
                        .stream()
                        .map(InjectorStrings::getMethodParameterString)
                        .collect(Collectors.toList()),
                ", ", " and "));
    }

    private static String getMethodParameterString(ExecutableData method) {
        String prefix = method.getSignature();
        if(method.getMethod() != null) {
            prefix += "." + method.getMethod();
        }
        return prefix +
                "(" +
                String.join(", ", method.getParameterSignatures()) +
                ")";
    }

    private static String getMethodParameterString(MethodInfo method) {
        String prefix = method.getClassName();
        if(!method.isConstructor()) {
            prefix += "." + method.getName();
        }
        return prefix +
                "(" +
                Arrays.stream(method.getParameterInfo())
                        .map(MethodParameterInfo::getTypeSignatureOrTypeDescriptor)
                        .map(TypeSignature::toString)
                        .collect(Collectors.joining(", ")) +
                ")";
    }

    private static String getComponentString(ComponentData<?> component) {
        if(component.getExecutable() instanceof Constructor) {
            return component.getReference().getSignature();
        }
        String signature;
        if(component.getReference() == null) {
            signature = "void";
        } else {
            signature = component.getReference().getSignature();
        }
        return signature +
                " " + getName(component.getExecutable().getDeclaringClass()) +
                "." + component.getExecutable().getName() +
                "(" + component
                        .getParameters()
                        .stream()
                        .map(ClassReference::getSignature)
                        .collect(Collectors.joining(", ")) +
                ")";
    }

    private static String getFieldParameterString(FieldData<?> field) {
        return field.getSignature() +
                " " +
                field.getName();
    }

    private static String getFieldParameterString(FieldInfo field) {
        return field.getTypeSignatureOrTypeDescriptor() +
                " " +
                field.getName();
    }

    private static String getName(ClassInfo info) {
        if(info.isArrayClass()) {
            return getName(info.loadClass());
        }
        return info.getName();
    }

    private static String getName(Class<?> type) {
        if(type.isArray()) {
            return type.arrayType().getName() + "[]";
        }
         return type.getName();
    }
}
