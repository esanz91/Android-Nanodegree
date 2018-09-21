package com.esanz.nano.ezbaking.utils;

import android.content.Intent;
import android.os.Bundle;

public class FragmentUtils {

    public static Bundle intentToArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (null == intent) {
            return arguments;
        }

        final Bundle extras = intent.getExtras();
        if (null != extras) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

}