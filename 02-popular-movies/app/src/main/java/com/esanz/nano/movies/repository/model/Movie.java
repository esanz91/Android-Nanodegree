package com.esanz.nano.movies.repository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.esanz.nano.movies.utils.MovieDateUtils;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "movies", indices = {@Index("id")})
public class Movie {

    public static final int MAX_RATING = 10;
    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_ID = "id";

    private static final String IMAGE_SECURE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE_PATH = "w342";
    private static final String BACKDROP_SIZE_PATH = "w780";
    private static final SimpleDateFormat RELEASE_DATE_INPUT_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat RELEASE_DATE_OUTPUT_FORMAT =
            new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @ColumnInfo(name = "has_video")
    @SerializedName("video")
    public boolean hasVideo;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    public float voteAverage;

    @SerializedName("title")
    public String title;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("overview")
    public String overview;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    public String releaseDate;

    public String getBackdropUrlString() {
        return IMAGE_SECURE_BASE_URL + BACKDROP_SIZE_PATH + backdropPath;
    }

    public String getPosterUrlString() {
        return IMAGE_SECURE_BASE_URL + POSTER_SIZE_PATH + posterPath;
    }

    public String getReleaseDate() {
        return MovieDateUtils.reformatDateString(releaseDate, RELEASE_DATE_INPUT_FORMAT, RELEASE_DATE_OUTPUT_FORMAT);
    }

}