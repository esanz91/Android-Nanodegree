package com.esanz.nano.ezbaking.respository.api;

import android.support.test.espresso.IdlingRegistry;

import com.esanz.nano.ezbaking.BuildConfig;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClientProvider {

    private static String TAG = OkHttpClientProvider.class.getSimpleName();
    private static OkHttpClient OKHTTP_INSTANCE;

    public OkHttpClientProvider() {
    }

    public static OkHttpClient getInstance() {
        if (null == OKHTTP_INSTANCE) {
            OKHTTP_INSTANCE = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor(new TimberHttpLogger())
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        }

        if (BuildConfig.DEBUG) {
            IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create(TAG, OKHTTP_INSTANCE));
        }

        return OKHTTP_INSTANCE;
    }

}
