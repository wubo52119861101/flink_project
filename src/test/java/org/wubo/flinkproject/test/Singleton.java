package org.wubo.flinkproject.test;

/**
 * @Author: wubo
 * @Date: 2020/1/15 16:15
 */
public class Singleton {
    private static Singleton instance = null;

    private Singleton() {
    }

    public static Singleton getInstance() {
        synchronized (Singleton.class) {
            if (instance == null) {
                instance = new Singleton();
            }
        }
        return instance;
    }
}
