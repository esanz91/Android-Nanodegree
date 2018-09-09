package com.esanz.nano.movies.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MovieConstant {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SortType.RATING, SortType.POPULARITY, SortType.FAVORITES})
    public @interface SortTypeDef {}

    public static class SortType {
        public static final int RATING = 1;
        public static final int POPULARITY = 2;
        public static final int FAVORITES = 3;
    }

}
