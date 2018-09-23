package com.esanz.nano.ezbaking.utils;

import android.content.Context;

public class ViewUtils {

    public static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

}
