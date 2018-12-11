package com.hl.dotime.java;

import java.util.Date;

public class TestClass {

    private Date date;
    private String s;

    public TestClass() {

    }

    public TestClass(Date d) {
        this.date = d;
    }

    public TestClass(Date d, String ss) {
        this.date = d;
        s = ss;
    }

    public void test(String... arg) {
        TestClass t = new TestClass(new Date());
        TestClass tt = new TestClass(new Date(), "11");
    }
}
