package com.maven.cleaner;

import com.maven.config.Const;
import com.maven.config.SystemConf;

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
 * Created by Intellij IDEA.
 *
 * @author Eric Cui
 * @since 2019-06-24 22:03
 */
@SuppressWarnings("all")
public class FileCleaner {

    /** maven仓库路径 */
    private static String MAVEN_ROOT_PATH =
            SystemConf.config(Const.FILE_SCHEMA, Const.MAVEN_ROOT_PATH);
    /** 将要删除的文件夹包含的文件 */
    private static final String lastUpdatedFile =
            SystemConf.config(Const.FILE_EXTENSION_LAST_UPDATED);
    /** jar包 */
    private static final String jarFile = SystemConf.config(Const.FILE_EXTENSION_JAR);

    private static final List<String> fileName = new ArrayList<>();
    /** 只要包含.lastUpdated文件就删除对应目录,默认为false */
    private static boolean cleanAll = false;

    private static boolean delete = false;

    public static void main(String[] args) throws URISyntaxException {
        loadArgs();
        System.out.println("MAVEN_ROOT_PATH:[" + MAVEN_ROOT_PATH + "]");
        System.out.println("cleanAll:" + cleanAll);
        System.out.println("delete:" + delete);
        File mvnRoot = Paths.get(new URI(MAVEN_ROOT_PATH)).toFile();
        File[] files = mvnRoot.listFiles();
        if (files == null) return;
        System.out.println(Arrays.asList(files));
        Arrays.stream(files).forEach(FileCleaner::cleanAbnormalDir);
    }

    private static void loadArgs() {
        String repo = System.getProperty("repo");
        File mvnRoot;
        if (null != repo && repo.length() > 0) {
            MAVEN_ROOT_PATH = SystemConf.config(Const.FILE_SCHEMA) + repo;
        }
        String all = System.getProperty("all");
        if (null != all) {
            cleanAll = true;
        }

        String del = System.getProperty("del");
        if (null != del) {
            delete = true;
        }
    }

    private static void cleanAbnormalDir(File file) {
        fileName.clear();
        if (file.isDirectory() && isAbnormalDir(file)) {
            if (delete) fileDelete(file);
            System.out.println("删除目录：" + file.getAbsolutePath() + ", " + fileName.toString());
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
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
        File[] files = file.listFiles();
        if (!file.isDirectory() || files == null) {
            return false;
        }
        if (files.length == 0) {
            return true;
        }

        boolean hasAbnormal = false;
        boolean hasJar = false;
        for (File f : files) {
            if (f.getName().endsWith(lastUpdatedFile)) {
                hasAbnormal = true;
            }
            if (f.getName().endsWith(jarFile)) {
                hasJar = true;
            }
            fileName.add(f.getName());
        }
        return hasAbnormal && (!hasJar || cleanAll);
    }
}
