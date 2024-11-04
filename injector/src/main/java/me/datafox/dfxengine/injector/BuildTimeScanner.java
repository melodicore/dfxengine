package me.datafox.dfxengine.injector;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import me.datafox.dfxengine.injector.utils.InjectorStrings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author datafox
 */
public class BuildTimeScanner {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            throw new RuntimeException(InjectorStrings.NO_OUTPUT_DIR);
        }
        String dir = args[0];
        File file = new File(dir, "scan.json");
        try(ScanResult scan = new ClassGraph()
                .enableAllInfo()
                .enableSystemJarsAndModules()
                .scan();
            PrintWriter writer = new PrintWriter(file)) {
            writer.print(scan.toJSON());
        }
    }
}