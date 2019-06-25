package com.maven.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Created by Intellij IDEA.
 *
 * @author 17893
 * @since 2019-06-25 10:28
 */
public final class SystemConf {
    private static Properties properties = new Properties();
    static {
        InputStream is = SystemConf.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(is);
            System.out.println("Property keys:" + properties.stringPropertyNames());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String config(String prefixKey, String valueKey) {
        return config(prefixKey) + config(valueKey);
    }

    public static String config(String key) {
        return properties.getProperty(key);
    }

    public static Integer intConfig(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static void main(String[] args) {
        System.out.println(SystemConf.config(Const.FILE_SCHEMA, Const.MAVEN_ROOT_PATH));
    }
}


