package com.esanz.nano.movies.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    private static final String IMAGE_SECURE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE_PATH = "w342";
    private static final String BACKDROP_SIZE_PATH = "w500";

    @SerializedName("id")
    public int id;

    @SerializedName("video")
    public boolean hasVideo;

    @SerializedName("vote_average")
    public float voteAverage;

    @SerializedName("title")
    public String title;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("overview")
    public String overview;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("genre_ids")
    public Integer[] genreIds;

    protected Movie(Parcel in) {
        id = in.readInt();
        hasVideo = in.readByte() != 0;
        voteAverage = in.readFloat();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
        dest.writeFloat(voteAverage);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    public String getBackdropUrlString() {
        return IMAGE_SECURE_BASE_URL + BACKDROP_SIZE_PATH + backdropPath;
    }

    public String getPosterUrlString() {
        return IMAGE_SECURE_BASE_URL + POSTER_SIZE_PATH + posterPath;
    }

}