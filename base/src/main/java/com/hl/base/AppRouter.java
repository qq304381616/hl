package com.hl.base;

import android.content.Context;
import android.content.Intent;

public class AppRouter {

    public static void turnPage(Context c, String name) {
        try {
            Class clazz = Class.forName(name);
            c.startActivity(new Intent(c, clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
