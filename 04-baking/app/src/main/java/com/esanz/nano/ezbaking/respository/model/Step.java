package com.esanz.nano.ezbaking.respository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.esanz.nano.ezbaking.respository.RecipeDetail;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = Step.TABLE_NAME,
        indices = @Index(value = Step.COLUMN_FOREIGN_KEY, name = "idx_steps_recipe_id"),
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = Recipe.COLUMN_PRIMARY_KEY,
                childColumns = Step.COLUMN_FOREIGN_KEY,
                onDelete = ForeignKey.CASCADE))
public class Step implements RecipeDetail, Parcelable {

    public static final String TITLE = "Instructions";

    public static final String TABLE_NAME = "steps";
    public static final String COLUMN_FOREIGN_KEY = "recipe_id";
    public static final String COLUMN_PRIMARY_KEY = "step_id";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

    @ColumnInfo(name = COLUMN_FOREIGN_KEY)
    public int recipeId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_PRIMARY_KEY)
    public transient int id;

    @ColumnInfo(name = COLUMN_POSITION)
    @SerializedName("id")
    public int position;

    @SerializedName("description")
    public String description;

    @ColumnInfo(name = COLUMN_SHORT_DESCRIPTION)
    @SerializedName("shortDescription")
    public String shortDescription;

    @ColumnInfo(name = COLUMN_VIDEO_URL)
    @SerializedName("videoURL")
    public String videoURL;

    @ColumnInfo(name = COLUMN_THUMBNAIL_URL)
    @SerializedName("thumbnailURL")
    public String thumbnailURL;

    @Override
    public int getType() {
        return RecipeDetail.TYPE_STEP;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public Step() {
        // required
    }

    @Ignore
    protected Step(Parcel in) {
        recipeId = in.readInt();
        position = in.readInt();
        description = in.readString();
        shortDescription = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeInt(position);
        dest.writeString(description);
        dest.writeString(shortDescription);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }
}
