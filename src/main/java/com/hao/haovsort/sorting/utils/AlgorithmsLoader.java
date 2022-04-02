package com.hao.haovsort.sorting.utils;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AlgorithmsLoader {

    public final static String FOLDERPATH = "resources/algorithms";

    public List<Class<?>> getAllAlgorithmsClasses() throws IOException {
        CodeSource src = AlgorithmsLoader.class.getProtectionDomain().getCodeSource();
        List<String> algorithm_paths = new ArrayList<>();
        if (src != null) {
            System.out.println("src-" + src.getLocation().toString());
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if (name.startsWith(FOLDERPATH))
                    algorithm_paths.add(name);
            }
            return algorithm_paths.stream().map((t) -> t.substring(FOLDERPATH.length() + 1))
                    .filter((u) -> !u.contains("$"))
                    .filter((v) -> v.endsWith(".class")).map((w) -> getClass(w, FOLDERPATH.replaceAll("[/]", ".")))
                    .collect(Collectors.toList());
        }
        throw new IOException();
    }

    public Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
