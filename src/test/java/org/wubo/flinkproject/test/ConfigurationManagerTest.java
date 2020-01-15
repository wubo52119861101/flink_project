package org.wubo.flinkproject.test;

import org.wubo.flinkproject.conf.ConfigurationManager;

/**
 * 2 * @Author: wubo
 * 3 * @Date: 2020/1/15 15:57
 * 4
 */
public class ConfigurationManagerTest {
    public static void main(String[] args) {
       String testkey1= ConfigurationManager.getProperties("testkey1");
        System.out.println(testkey1);
    }
}
