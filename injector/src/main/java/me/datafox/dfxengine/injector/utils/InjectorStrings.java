package me.datafox.dfxengine.injector.utils;

import io.github.classgraph.*;
import me.datafox.dfxengine.injector.internal.ClassReference;
import me.datafox.dfxengine.injector.internal.ComponentData;
import me.datafox.dfxengine.utils.StringUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
public class InjectorStrings {
    public static final String NOT_CLOSING_SCAN = "Option set to not close ClassGraph scan. " +
            "This is not recommended, and only exists for testing purposes where multiple " +
            "Injector instances need to be created in the same process";
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
    private static final String MULTIPLE_CONSTRUCTORS =
            "Class %s has multiple constructors annotated with @Inject and cannot be instantiated: %s";
    private static final String NO_CONSTRUCTOR =
            "Class %s has no default constructor or a constructor annotated with @Inject and cannot be instantiated";
    private static final String BUILDING_COMPONENT_CLASS_DATA =
            "Building component data for class %s using constructor %s";
    private static final String BUILDING_COMPONENT_METHOD_DATA = "Building component data for class %s using method %s";
    private static final String FINAL_FIELD_DEPENDENCY =
            "Field %s in class %s is annotated with @Inject but is final, only non-final fields can be injected";
    private static final String UNRESOLVED_TYPE_PARAMETER =
            "Component %s has unresolved type parameter %s, unresolved type parameters cannot be injected";
    private static final String NO_DEPENDENCIES = "A single Component %s was requested but none are present";
    private static final String MULTIPLE_DEPENDENCIES =
            "A single Component %s was requested but multiple are present";
    private static final String ARRAY_COMPONENT =
            "Component method %s in class %s returns an array, array components are not permitted";
    private static final String ARRAY_DEPENDENCY =
            "Component %s depends on an array, array components are not permitted";
    private static final String ARRAY_FIELD_DEPENDENCY =
            "Class %s has field %s annotated with @Inject, array components are not permitted";
    private static final String CYCLIC_DEPENDENCY_DETECTED = "Detected a cyclic dependency: %s";
    private static final String TYPE_PARAMETER_MISMATCH_SS = "Class %s has %s type parameter but %s was provided";
    private static final String TYPE_PARAMETER_MISMATCH_SP = "Class %s has %s type parameter but %s were provided";
    private static final String TYPE_PARAMETER_MISMATCH_PS = "Class %s has %s type parameters but %s was provided";
    private static final String TYPE_PARAMETER_MISMATCH_PP = "Class %s has %s type parameters but %s were provided";

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
        return forClassAndMethodInfo(NON_COMPONENT_CLASS_WITH_INJECT_CONSTRUCTOR,
                constructor.getClassInfo(), constructor);
    }

    public static String nonComponentClassWithInjectField(FieldInfo field) {
        return forClassAndFieldInfo(NON_COMPONENT_CLASS_WITH_INJECT_FIELD,
                field.getClassInfo(), field);
    }

    public static String nonComponentClassWithInitializeMethod(MethodInfo method) {
        return forClassAndMethodInfo(NON_COMPONENT_CLASS_WITH_INITIALIZE_METHOD,
                method.getClassInfo(), method);
    }

    public static String multipleConstructors(ClassInfo info, MethodInfoList constructors) {
        return forClassInfoAndMethodInfoList(MULTIPLE_CONSTRUCTORS, info, constructors);
    }

    public static String noConstructor(ClassInfo info) {
        return forClassInfo(NO_CONSTRUCTOR, info);
    }

    public static String buildingComponentClassData(String classString, MethodInfo constructor) {
        return forStringAndMethodInfo(BUILDING_COMPONENT_CLASS_DATA, classString, constructor);
    }

    public static String buildingComponentMethodData(String classString, MethodInfo method) {
        return forStringAndMethodInfo(BUILDING_COMPONENT_METHOD_DATA, classString, method);
    }

    public static String noDependenciesRuntime(ClassReference<?> reference) {
        return forReference(NO_DEPENDENCIES, reference);
    }

    public static String multipleDependenciesRuntime(ClassReference<?> reference) {
        return forReference(MULTIPLE_DEPENDENCIES, reference);
    }

    public static String arrayComponent(MethodInfo method, ClassInfo info) {
        return forMethodAndClassInfo(ARRAY_COMPONENT, method, info);
    }

    public static String arrayDependency(MethodParameterInfo info) {
        return forMethodInfo(ARRAY_DEPENDENCY, info.getMethodInfo());
    }

    public static String arrayFieldDependency(FieldInfo info) {
        return forClassAndFieldInfo(ARRAY_FIELD_DEPENDENCY, info.getClassInfo(), info);
    }

    public static String finalFieldDependency(FieldInfo field) {
        return forFieldAndClassInfo(FINAL_FIELD_DEPENDENCY, field, field.getClassInfo());
    }

    public static String unresolvedTypeParameter(MethodInfo info, String parameter) {
        return forMethodComponentAndString(UNRESOLVED_TYPE_PARAMETER, info, parameter);
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

    public static String parameterCountMismatch(Class<?> type, int expected, int actual) {
        String str = TYPE_PARAMETER_MISMATCH_PP;
        if(expected == 1 && actual == 1) {
            str = TYPE_PARAMETER_MISMATCH_SS;
        } else if(expected == 1) {
            str = TYPE_PARAMETER_MISMATCH_SP;
        } else if(actual == 1) {
            str = TYPE_PARAMETER_MISMATCH_PS;
        }
        return forClassAndTwoInts(str, type, expected, actual);
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

    private static String forClassInfo(String str, ClassInfo info) {
        return String.format(str, info.getName());
    }

    private static String forMethodInfo(String str, MethodInfo info) {
        return String.format(str, getMethodParameterString(info));
    }

    private static String forClassAndMethodInfo(String str, ClassInfo info, MethodInfo method) {
        return String.format(str, info.getName(), getMethodParameterString(method));
    }

    private static String forStringAndMethodInfo(String str, String string, MethodInfo method) {
        return String.format(str, string, getMethodParameterString(method));
    }

    private static String forClassInfoAndMethodInfoList(String str, ClassInfo info, MethodInfoList methods) {
        return String.format(str, info.getName(),
                StringUtils.joining(methods
                        .stream()
                        .map(InjectorStrings::getMethodParameterString)
                        .collect(Collectors.toList()), ", ", " and "));
    }

    private static String forMethodAndClassInfo(String str, MethodInfo method, ClassInfo info) {
        return String.format(str, getMethodParameterString(method), info.getName());
    }

    private static String forClassAndFieldInfo(String str, ClassInfo info, FieldInfo field) {
        return String.format(str, info.getName(), getFieldParameterString(field));
    }

    private static String forFieldAndClassInfo(String str, FieldInfo field, ClassInfo info) {
        return String.format(str, getFieldParameterString(field), info.getName());
    }

    private static String forMethodComponentAndString(String str, MethodInfo info, String string) {
        return String.format(str, getMethodComponentString(info), string);
    }

    private static String forReference(String str, ClassReference<?> reference) {
        return String.format(str, reference.getSignature());
    }

    private static String forClassAndTwoInts(String str, Class<?> type, int int1, int int2) {
        return String.format(str, type.getName(), int1, int2);
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

    private static String getMethodComponentString(MethodInfo method) {
        String prefix;
        if(method.isConstructor()) {
            prefix = method.getClassInfo().getTypeSignatureOrTypeDescriptor().toString().split(" class | interface ",2)[1];
        } else {
            prefix = method.getTypeSignatureOrTypeDescriptor().getResultType().toString() + " " + method.getClassName() + "." + method.getName();
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
        return component.getReference().getSignature() +
                " " + component.getExecutable().getDeclaringClass().getName() +
                "." + component.getExecutable().getName() +
                "(" + component
                        .getParameters()
                        .stream()
                        .map(ClassReference::getSignature)
                        .collect(Collectors.joining(", ")) +
                ")";
    }

    private static String getFieldParameterString(FieldInfo field) {
        return field.getTypeSignatureOrTypeDescriptor() +
                " " +
                field.getName();
    }
}
