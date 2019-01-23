package com.hl.java;

/**
 * 单例 7种写法
 */
public class Singleton {

    private static Singleton instance;

    private Singleton() {
    }

    // 简单，不支持多线程
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    // 懒汉 线程安全
    public static synchronized Singleton getInstance1() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    // 饿汉 线程安全
    private static Singleton instance3 = new Singleton();

    public static Singleton getInstance3() {
        return instance3;
    }

    // 饿汉变种
    private static Singleton instance4 = null;

    static {
        instance4 = new Singleton();
    }

    public static Singleton getInstance4() {
        return instance4;
    }

    // 静态内部类
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static final Singleton getInstance5() {
        return SingletonHolder.INSTANCE;
    }

    // 枚举
    public enum SingletonEnum {
        INSTANCE;;

        public void whateverMethod() {

        }
    }

    // 双重校验锁
    private volatile static Singleton singleton7;

    public static Singleton getInstance7() {
        if (singleton7 == null) {
            synchronized (Singleton.class) {
                if (singleton7 == null) {
                    singleton7 = new Singleton();
                }
            }
        }
        return singleton7;
    }
}

