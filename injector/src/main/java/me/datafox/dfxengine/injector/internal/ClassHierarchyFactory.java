package me.datafox.dfxengine.injector.internal;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodParameterInfo;
import io.github.classgraph.TypeSignature;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.injector.serialization.ClassData;
import me.datafox.dfxengine.injector.serialization.ClassHierarchy;
import me.datafox.dfxengine.injector.serialization.ExecutableData;
import me.datafox.dfxengine.injector.serialization.FieldData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Factory that builds a {@link ClassHierarchy}.
 *
 * @author datafox
 */
@SuppressWarnings("MissingJavadoc")
public class ClassHierarchyFactory {
    private final Logger logger;

    private final boolean logIgnored;

    public ClassHierarchyFactory(boolean logIgnored) {
        if(logIgnored) {
            logger = LoggerFactory.getLogger(ClassHierarchyFactory.class);
        } else {
            logger = null;
        }
        this.logIgnored = logIgnored;
    }

    public ClassHierarchy build(Map<String,ClassInfo> classes, List<MethodInfo> methods, List<MethodInfo> events) {
        return new ClassHierarchy(parseClasses(classes), parseMethods(methods), parseMethods(events));
    }

    private List<ClassData<?>> parseClasses(Map<String,ClassInfo> classes) {
        return classes
                .values()
                .stream()
                .map(this::parseClass)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<ClassData<T>> parseClass(ClassInfo info) {
        Class<T> type;
        try {
            type = (Class<T>) info.loadClass();
            Class.forName(type.getName());
        } catch(Throwable e) {
            if(logIgnored) {
                logger.trace("Encountered unloadable class {}", info.getTypeDescriptor());
            }
            return Optional.empty();
        }
        String str = (" " + info
                .getTypeSignatureOrTypeDescriptor()
                .toString())
                .split(" class | interface | @interface | enum ", 2)[1];
        return Optional.of(new ClassData<>(type, str));
    }

    private List<ExecutableData<?>> parseMethods(List<MethodInfo> methods) {
        return methods
                .stream()
                .map(this::parseMethod)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private <T> ExecutableData<T> parseMethod(MethodInfo info) {
        List<String> parameters = Arrays
                .stream(info.getParameterInfo())
                .map(MethodParameterInfo::getTypeSignatureOrTypeDescriptor)
                .map(TypeSignature::toString)
                .collect(Collectors.toList());
        ClassData<T> data = (ClassData<T>) parseClass(info.getClassInfo()).orElseThrow();
        if(info.isConstructor()) {
            List<FieldData<?>> fields = info
                    .getClassInfo()
                    .getDeclaredFieldInfo()
                    .stream()
                    .filter(f -> f.hasAnnotation(Inject.class))
                    .map(f -> new FieldData<>(f.getName(), f.loadClassAndGetField().getType(), f.getTypeSignatureOrTypeDescriptor().toString()))
                    .collect(Collectors.toList());
            List<ExecutableData<?>> methods = info
                    .getClassInfo()
                    .getDeclaredMethodInfo()
                    .stream()
                    .filter(m -> m.hasAnnotation(Initialize.class))
                    .map(this::parseMethod)
                    .collect(Collectors.toList());
            return new ExecutableData<>(info.loadClassAndGetConstructor(), data.getType(), data.getSignature(), parameters, fields, methods);
        }
        String signature = info.getTypeSignatureOrTypeDescriptor()
                .toString()
                .split(" \\(", 2)[0];
        return new ExecutableData<>(info.loadClassAndGetMethod(), (Class<T>) info.getClassInfo().loadClass(), signature, parameters, List.of(), List.of());
    }
}
