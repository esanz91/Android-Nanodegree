package com.esanz.nano.ezbaking.respository.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_MOVIE_URL = "http://go.udacity.com/";

    private static Retrofit RETROFIT_INSTANCE;

    private RetrofitClient() {
    }

    public static Retrofit getInstance(@NonNull final OkHttpClient okHttpClient) {
        if (null == RETROFIT_INSTANCE) {
            RETROFIT_INSTANCE = new Retrofit.Builder()
                    .baseUrl(BASE_MOVIE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory
                            .createWithScheduler(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)))
                    .client(okHttpClient)
                    .build();
        }
        return RETROFIT_INSTANCE;
    }

}
