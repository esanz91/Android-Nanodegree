package com.esanz.nano.ezbaking.respository;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface RecipeDetail {

    int TYPE_HEADER = 0;
    int TYPE_INGREDIENT = 1;
    int TYPE_STEP = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_HEADER, TYPE_INGREDIENT, TYPE_STEP})
    @interface Type {}

    @Type
    int getType();

    default String getTitle() {
        return null;
    }

    default boolean isHeader() {
        return false;
    }
}
