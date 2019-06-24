package com.maven.lastUpdated.clean;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Created by Intellij IDEA.
 *
 * @author Eric Cui
 * @since 2019-06-24 22:03
 */
public class FileCleaner {

    // maven仓库路径
    private static final String MAVEN_ROOT_PATH = "file:/Users/cuiguiyang/.m2/";
    // 将要删除的文件夹包含的文件
    private static final String lastUpdated_File_extension = ".lastUpdated";
    // jar包
    private static final String jar_File_extension = ".jar";
    private static final List<String> fileName = new ArrayList<>();

    public static void main(String[] args) throws URISyntaxException {
        File mvnRoot = Paths.get(new URI(MAVEN_ROOT_PATH)).toFile();
        File[] files = mvnRoot.listFiles();
        if (files == null) return;
        Arrays.stream(files).forEach(FileCleaner::cleanAbnormalDir);
    }

    private static void cleanAbnormalDir(File file) {
        fileName.clear();
        if (file.isDirectory() && isAbnormalDir(file)) {
            // fileDelete(file);
            System.out.println("删除目录：" + file.getAbsolutePath() + ", " + fileName.toString());
        } else if (file.isDirectory()) {
            File[] files =  file.listFiles();
            if (files == null) return;
            Arrays.stream(files).forEach(FileCleaner::cleanAbnormalDir);
        }
    }

    private static void fileDelete(File file) {
        try {
            Files.walk(file.toPath()).map(Path::toFile).forEach(File::deleteOnExit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.deleteOnExit();
    }


    private static boolean isAbnormalDir(File file) {
        File[] files =  file.listFiles();
        if (!file.isDirectory() || files == null) {
            return false;
        }
        if (files.length == 0) {
            return true;
        }

        boolean hasAbnormal = false;
        boolean hasJar = false;
        for (File f : files) {
            if (f.getName().endsWith(lastUpdated_File_extension)) {
                hasAbnormal = true;
            }
            if (f.getName().endsWith(jar_File_extension)) {
                hasJar = true;
            }
            fileName.add(f.getName());
        }
        return hasAbnormal && !hasJar;
    }
}
