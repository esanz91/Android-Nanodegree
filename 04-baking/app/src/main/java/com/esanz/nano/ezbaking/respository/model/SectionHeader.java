package com.esanz.nano.ezbaking.respository.model;

import com.esanz.nano.ezbaking.respository.RecipeDetail;

public class SectionHeader implements RecipeDetail {

    public String name;

    public SectionHeader(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return RecipeDetail.TYPE_HEADER;
    }

}