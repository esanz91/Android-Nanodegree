package com.esanz.nano.movies.utils;

import android.text.Spannable;
import android.text.style.URLSpan;

import com.esanz.nano.movies.ui.widget.NoUnderlineUrlSpan;

public class MovieStringUtils {

    public static Spannable removeUrlUnderline(Spannable span) {
        URLSpan[] urlSpans = span.getSpans(0, span.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            span.setSpan(new NoUnderlineUrlSpan(urlSpan.getURL()),
                    span.getSpanStart(urlSpan), span.getSpanEnd(urlSpan), 0);
        }
        return span;
    }
}
