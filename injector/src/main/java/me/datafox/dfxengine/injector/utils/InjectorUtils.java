package me.datafox.dfxengine.injector.utils;

import io.github.classgraph.MethodInfo;
import me.datafox.dfxengine.injector.internal.ClassReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class InjectorUtils {
    public static String getMethodReturnClassName(MethodInfo info) {
        return parseClass(stripMethodParameters(info
                    .getTypeSignatureOrTypeDescriptorStr())
                .split("[;<]", 2)[0]);
    }

    public static String stripMethodParameters(String typeSignature) {
        if(typeSignature == null) {
            return null;
        }
        return typeSignature
                .split("\\)", 2)[1];
    }

    public static String parseClass(String str) {
        switch(str.charAt(0)) {
            case 'Z':
                return Boolean.class.getName();
            case 'B':
                return Byte.class.getName();
            case 'S':
                return Short.class.getName();
            case 'I':
                return Integer.class.getName();
            case 'J':
                return Long.class.getName();
            case 'F':
                return Float.class.getName();
            case 'D':
                return Double.class.getName();
            case 'C':
                return Character.class.getName();
            case 'L':
                return str
                        .substring(1)
                        .replaceAll("/", ".");
            default:
                return Object.class.getName();
        }
    }

    public static String getWithin(String str, char start, char end) {
        int s = -1;
        int e = -1;
        int counter = 0;
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(start == c) {
                counter++;
                if(s == -1) {
                    s = i + 1;
                }
            } else if(s != -1 && end == c) {
                counter--;
                if(counter == 0) {
                    e = i;
                    break;
                }
            }
        }
        if(s == -1) {
            return str;
        }
        if(e == -1) {
            return str.substring(s);
        }
        return str.substring(s, e);
    }

    public static String[] splitParameters(String typeParameters) {
        List<String> list = new ArrayList<>();
        int counter = 0;
        int lastSplit = 0;
        for(int i = 0; i < typeParameters.length(); i++) {
            if(counter == 0) {
                if(typeParameters.startsWith(", ", i)) {
                    list.add(typeParameters.substring(lastSplit, i));
                    i++;
                    lastSplit = i + 1;
                    continue;
                }
            }
            if(typeParameters.charAt(i) == '<') {
                counter++;
            } else if(typeParameters.charAt(i) == '>') {
                counter--;
            }
        }
        list.add(typeParameters.substring(lastSplit));
        return list.toArray(String[]::new);
    }

    public static Stream<ClassReference<?>> getSuperclassesRecursive(ClassReference<?> classReference) {
        return Stream.concat(Stream.of(classReference),
                classReference
                        .getSuperclasses()
                        .stream()
                        .flatMap(InjectorUtils::getSuperclassesRecursive));
    }
}
