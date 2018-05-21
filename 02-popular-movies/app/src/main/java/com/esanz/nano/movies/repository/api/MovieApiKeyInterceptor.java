package com.esanz.nano.movies.repository.api;

import android.content.Context;

import com.esanz.nano.movies.R;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MovieApiKeyInterceptor implements Interceptor {

    private static final String QUERY_API_KEY = "api_key";

    private final String apiKey;

    MovieApiKeyInterceptor(Context context) {
        apiKey = context.getString(R.string.movie_api_key);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl httpUrl = request.url();

        HttpUrl newUrl = httpUrl.newBuilder()
                .addQueryParameter(QUERY_API_KEY, apiKey)
                .build();

        Request newRequest = request.newBuilder()
                .url(newUrl)
                .build();

        return chain.proceed(newRequest);
    }

}
