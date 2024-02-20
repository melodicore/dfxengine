package me.datafox.dfxengine.injector.utils;

import io.github.classgraph.*;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class InjectorStrings {
    public static final String SCANNING_CLASSPATH = "Scanning classpath";
    public static final String BUILDING_DEPENDENCY_GRAPH = "Building dependency graph";
    public static final String CHECKING_CYCLIC = "Checking for cyclic dependencies";
    public static final String SOMETHING_WENT_WRONG = "Oh no, something went wrong!";

    private static final String WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR = "%s %slist present with %s rule";
    private static final String WHITELIST_OR_BLACKLIST_PRESENT_PLURAL = "%s %slist present with %s rules";
    private static final String WHITELIST_OR_BLACKLIST_RULES = "Rules: %s";
    private static final String COMPONENT_CLASSES_FOUND_SINGULAR = "Found %s component class";
    private static final String COMPONENT_CLASSES_FOUND_PLURAL = "Found %s component classes";
    private static final String COMPONENT_CLASSES = "Component classes: %s";
    private static final String COMPONENT_METHOD_CLASSES_FOUND_SINGULAR = "Found %s class declaring component methods";
    private static final String COMPONENT_METHOD_CLASSES_FOUND_PLURAL = "Found %s classes declaring component methods";
    private static final String COMPONENT_METHOD_CLASSES = "Component method classes: %s";
    private static final String INSTANTIATED_CLASSES_FOUND_SINGULAR = "Found %s class to instantiate";
    private static final String INSTANTIATED_CLASSES_FOUND_PLURAL = "Found %s classes to instantiate";
    private static final String INSTANTIATED_CLASSES = "Instantiated classes: %s";
    private static final String INVOKED_CONSTRUCTORS_FOUND_SINGULAR = "Found %s constructor to invoke";
    private static final String INVOKED_CONSTRUCTORS_FOUND_PLURAL = "Found %s constructors to invoke";
    private static final String INVOKED_CONSTRUCTORS = "Invoked constructors: %s";
    private static final String INVOKED_METHODS_FOUND_SINGULAR = "Found %s method to invoke";
    private static final String INVOKED_METHODS_FOUND_PLURAL = "Found %s methods to invoke";
    private static final String INVOKED_METHODS = "Invoked methods: %s";
    private static final String COMPONENTS_FOUND_SINGULAR = "Found %s declared component";
    private static final String COMPONENTS_FOUND_PLURAL = "Found %s declared components";
    private static final String COMPONENTS = "Declared components: %s";
    private static final String NON_COMPONENT_CLASS_WITH_INJECT_CONSTRUCTOR = "Class %s is not recognised " +
            "as a component but has a constructor %s annotated with @Inject. This constructor will be ignored";
    private static final String NON_COMPONENT_CLASS_WITH_INJECT_FIELD = "Class %s is not recognised " +
            "as a component but has a field %s annotated with @Inject. This field will be ignored";
    private static final String NON_COMPONENT_CLASS_WITH_INITIALIZE_METHOD = "Class %s is not recognised " +
            "as a component but has a method %s annotated with @Initialize. This method will be ignored";
    private static final String BUILDING_COMPONENT_CLASS_DATA =
            "Building component data for class %s using constructor %s";
    private static final String BUILDING_COMPONENT_METHOD_DATA = "Building component data for class %s using method %s";
    private static final String CYCLIC_DEPENDENCY_DETECTED = "Detected a cyclic dependency: %s";

    public static String packageWhitelistPresent(int rules) {
        return forTwoStringsAndInt(rules == 1 ?
                WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Package", "white", rules);
    }

    public static String packageBlacklistPresent(int rules) {
        return forTwoStringsAndInt(rules == 1 ?
                        WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                        WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Package", "black", rules);
    }

    public static String classWhitelistPresent(int rules) {
        return forTwoStringsAndInt(rules == 1 ?
                        WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                        WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Class", "white", rules);
    }

    public static String classBlacklistPresent(int rules) {
        return forTwoStringsAndInt(rules == 1 ?
                        WHITELIST_OR_BLACKLIST_PRESENT_SINGULAR :
                        WHITELIST_OR_BLACKLIST_PRESENT_PLURAL,
                "Class", "black", rules);
    }

    public static String whitelistOrBlacklistRules(Collection<String> rules) {
        return forStringCollection(WHITELIST_OR_BLACKLIST_RULES, rules);
    }

    public static String componentClassesFound(int classes) {
        return forInt(classes == 1 ?
                COMPONENT_CLASSES_FOUND_SINGULAR :
                COMPONENT_CLASSES_FOUND_PLURAL,
                classes);
    }

    public static String componentClasses(ClassInfoList infos) {
        return forClassInfoList(COMPONENT_CLASSES, infos);
    }

    public static String componentMethodClassesFound(int classes) {
        return forInt(classes == 1 ?
                        COMPONENT_METHOD_CLASSES_FOUND_SINGULAR :
                        COMPONENT_METHOD_CLASSES_FOUND_PLURAL,
                classes);
    }

    public static String componentMethodClasses(ClassInfoList infos) {
        return forClassInfoList(COMPONENT_METHOD_CLASSES, infos);
    }

    public static String instantiatedClassesFound(int classes) {
        return forInt(classes == 1 ?
                        INSTANTIATED_CLASSES_FOUND_SINGULAR :
                        INSTANTIATED_CLASSES_FOUND_PLURAL,
                classes);
    }

    public static String instantiatedClasses(ClassInfoList infos) {
        return forClassInfoList(INSTANTIATED_CLASSES, infos);
    }

    public static String invokedConstructorsFound(int constructors) {
        return forInt(constructors == 1 ?
                        INVOKED_CONSTRUCTORS_FOUND_SINGULAR :
                        INVOKED_CONSTRUCTORS_FOUND_PLURAL,
                constructors);
    }

    public static String invokedConstructors(MethodInfoList constructors) {
        return forMethodInfoList(INVOKED_CONSTRUCTORS, constructors);
    }

    public static String invokedMethodsFound(int methods) {
        return forInt(methods == 1 ?
                        INVOKED_METHODS_FOUND_SINGULAR :
                        INVOKED_METHODS_FOUND_PLURAL,
                methods);
    }

    public static String invokedMethods(MethodInfoList methods) {
        return forMethodInfoList(INVOKED_METHODS, methods);
    }

    public static String componentsFound(int components) {
        return forInt(components == 1 ?
                        COMPONENTS_FOUND_SINGULAR :
                        COMPONENTS_FOUND_PLURAL,
                components);
    }

    public static String components(List<ComponentData<?>> components) {
        return forComponentDataList(COMPONENTS, components);
    }

    public static String nonComponentClassWithInjectConstructor(MethodInfo constructor) {
        return forClassInfoAndMethod(NON_COMPONENT_CLASS_WITH_INJECT_CONSTRUCTOR,
                constructor.getClassInfo(), constructor);
    }

    public static String nonComponentClassWithInjectField(FieldInfo field) {
        return forClassInfoAndField(NON_COMPONENT_CLASS_WITH_INJECT_FIELD,
                field.getClassInfo(), field);
    }

    public static String nonComponentClassWithInitializeMethod(MethodInfo method) {
        return forClassInfoAndMethod(NON_COMPONENT_CLASS_WITH_INITIALIZE_METHOD,
                method.getClassInfo(), method);
    }

    public static String buildingComponentClassData(ClassInfo info, MethodInfo constructor) {
        return forClassInfoAndMethod(BUILDING_COMPONENT_CLASS_DATA, info, constructor);
    }

    public static String buildingComponentMethodData(ClassInfo info, MethodInfo method) {
        return forClassInfoAndMethod(BUILDING_COMPONENT_METHOD_DATA, info, method);
    }

    public static String cyclicDependencyDetected(ComponentData<?> current, Stack<ComponentData<?>> visited) {
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
                .map(ClassReference::getName)
                .collect(Collectors.joining(" -> ")));
    }

    private static String forTwoStringsAndInt(String str, String string1, String string2, int integer) {
        return String.format(str, string1, string2, integer);
    }

    private static String forStringCollection(String str, Collection<String> strings) {
        return String.format(str,
                "\"" + StringUtils.joining(strings, "\", \"", "\" and \"") + "\"");
    }

    private static String forClassInfoList(String str, ClassInfoList classes) {
        return String.format(str, StringUtils.joining(classes
                        .stream()
                        .map(ClassInfo::getName)
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

    private static String forComponentDataList(String str, List<ComponentData<?>> components) {
        return String.format(str, StringUtils.joining(components
                        .stream()
                        .map(InjectorStrings::getComponentString)
                        .collect(Collectors.toList()),
                ", ", " and "));
    }

    private static String forInt(String str, int integer) {
        return String.format(str, integer);
    }

    private static String forClassInfoAndMethod(String str, ClassInfo info, MethodInfo method) {
        return String.format(str, info.getName(), getMethodParameterString(method));
    }

    private static String forClassInfoAndField(String str, ClassInfo info, FieldInfo field) {
        return String.format(str, info.getName(), getFieldParameterString(field));
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
            return component.getReference().getName();
        }
        return component.getReference().getName() +
                " " + component.getExecutable().getDeclaringClass().getName() +
                "." + component.getExecutable().getName() +
                "(" + component
                        .getParameters()
                        .stream()
                        .map(ClassReference::getName)
                        .collect(Collectors.joining(", ")) +
                ")";
    }

    private static String getFieldParameterString(FieldInfo field) {
        return field.getTypeSignatureOrTypeDescriptor() +
                " " +
                field.getClassName() +
                "." +
                field.getName();
    }
}
