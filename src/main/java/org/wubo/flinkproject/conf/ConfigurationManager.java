package org.wubo.flinkproject.conf;

import java.io.InputStream;
import java.util.Properties;

/**
 * 2 * @Author: wubo
 * 3 * @Date: 2020/1/15 15:51
 * 4
 */
public class ConfigurationManager {
    private static Properties props = new Properties();

    static {
        try {
            InputStream in = ConfigurationManager.class
                    .getClassLoader().getResourceAsStream("my.properties");
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperties(String key) {
        return props.getProperty(key);
    }

    public static Integer getInteger(String key){
        return Integer.parseInt(props.getProperty(key));
    }
}
