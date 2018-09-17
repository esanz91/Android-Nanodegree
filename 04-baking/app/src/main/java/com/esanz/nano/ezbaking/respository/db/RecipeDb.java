package com.esanz.nano.ezbaking.respository.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.esanz.nano.ezbaking.respository.model.Ingredient;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.respository.model.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class},
        version = 1,
        exportSchema = false)
public abstract class RecipeDb extends RoomDatabase {

    private static final String DATABASE_NAME = "recipes";

    public static RecipeDb INSTANCE;

    public static RecipeDb getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDb.class, RecipeDb.DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public abstract RecipeDao recipeDao();

}
