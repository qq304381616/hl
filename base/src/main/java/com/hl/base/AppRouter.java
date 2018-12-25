package com.hl.base;

import android.content.Context;
import android.content.Intent;

public class AppRouter {

    public static void turnPage(Context c) {
        Intent intent = new Intent(c.getPackageName() + ".HomeActivity");
        c.startActivity(intent);
    }
}
