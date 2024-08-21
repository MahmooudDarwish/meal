package com.example.mealapp.utils.resource_helper;

import android.content.Context;

public class ResourceHelper {
    private static Context ctx;

    public static void init(Context context) {
        ctx = context.getApplicationContext();
    }

    public static String getString(int resId) {
        return ctx.getString(resId);
    }
}
