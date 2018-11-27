package com.bluetroy.servlet.utils;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: heyixin
 * Date: 2018-11-26
 * Time: 11:11
 */
public class ClassScanner {
    private static final List<Class> CLASS_LIST = new ArrayList<>();
    private static final String CLASS_FILE = ".class";
    private static final URL ROOT = ClassScanner.class.getClassLoader().getResource("");

    static {
        if (isJar(ROOT)) {
            scanInJar(ROOT);
        } else if (isFile(ROOT)) {
            scanInDirectory(ROOT);
        }
    }

    public static List<Class> getClassList() {
        return List.copyOf(CLASS_LIST);
    }

    private static void scanInJar(URL url) {
        try {
            String jarPath = System.getProperty("java.class.path");
            Enumeration<JarEntry> jarEntry = new JarFile(jarPath).entries();
            while (jarEntry.hasMoreElements()) {
                JarEntry subEntry = jarEntry.nextElement();
                if (isMyClass(subEntry)) {
                    CLASS_LIST.add(getClass(subEntry));
                }
            }
        } catch (Exception ignored) {

        }

    }

    private static Class getClass(JarEntry subEntry) throws ClassNotFoundException {
        String className = jarEntryToClassName(subEntry);
        return Class.forName(className);
    }

    private static String jarEntryToClassName(JarEntry jarEntry) {
        String classPath = jarEntry.getRealName().replaceFirst(getRelativeRootPath(), "");
        return pathToClassName(classPath);
    }

    private static String pathToClassName(String classPath) {
        return classPath.substring(0, classPath.indexOf(CLASS_FILE)).replace(File.separatorChar, '.');
    }

    private static boolean isMyClass(JarEntry jarEntry) {
        String realName = jarEntry.getRealName();
        return realName.contains(getRelativeRootPath()) && isClassPath(realName);
    }


    //todo 完成
    private static void scanInDirectory(URL url) {
        try {
            Files.walkFileTree(Paths.get(url.toURI()), new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        if (isClassPath(file.toString())) {
                            CLASS_LIST.add(ClassScanner.getClass(file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception ignored) {
        }
    }

    private static Class getClass(Path file) throws ClassNotFoundException {
        String className = pathToClassName(file.toString().substring(ROOT.getFile().length()));
        return Class.forName(className);
    }

    private static boolean isClassPath(String path) {
        return path.endsWith(".class");
    }

    private static boolean isJar(URL url) {
        return url.getProtocol().startsWith("jar");
    }

    private static boolean isFile(URL url) {
        return url.getProtocol().startsWith("file");
    }

    private static String getRelativeRootPath() {
        URL url = ClassScanner.class.getClassLoader().getResource("");
        String classRootPath = url.getFile().substring(5).replace("!", "");
        String jarPath = System.getProperty("java.class.path");
        return classRootPath.replace(jarPath, "").substring(1);
    }

}
