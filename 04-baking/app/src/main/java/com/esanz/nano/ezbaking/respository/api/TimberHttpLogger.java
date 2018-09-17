package com.esanz.nano.ezbaking.respository.api;

import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class TimberHttpLogger implements HttpLoggingInterceptor.Logger {

    public TimberHttpLogger() {
    }

    @Override
    public void log(String message) {
        Timber.tag("OkHttp").d(message);
    }

}
