package com.esanz.nano.movies.utils;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MovieDateUtils {

    @Nullable
    public static String reformatDateString(String date, SimpleDateFormat in, SimpleDateFormat out) {
        try {
            return out.format(in.parse(date));
        } catch (ParseException e) {
            return null;
        }
    }

}
