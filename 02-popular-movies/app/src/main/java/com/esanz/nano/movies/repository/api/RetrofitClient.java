package com.esanz.nano.movies.repository.api;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/";

    private static MovieApi MOVIE_API;
    private static Retrofit RETROFIT_INSTANCE;

    private RetrofitClient() {
    }

    public static MovieApi getMovieApi(@NonNull Context context) {
        if (null == MOVIE_API) {
            MOVIE_API = getRetrofit(context).create(MovieApi.class);
        }
        return MOVIE_API;
    }

    private static Retrofit getRetrofit(@NonNull Context context) {
        if (null == RETROFIT_INSTANCE) {
            RETROFIT_INSTANCE = new Retrofit.Builder()
                    .baseUrl(BASE_MOVIE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(createClient(context))
                    .build();
        }
        return RETROFIT_INSTANCE;
    }

    private static OkHttpClient createClient(@NonNull Context context) {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new MovieApiKeyInterceptor(context))
                .build();
    }

}
