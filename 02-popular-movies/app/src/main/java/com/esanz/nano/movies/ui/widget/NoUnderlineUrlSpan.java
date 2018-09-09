package com.esanz.nano.movies.ui.widget;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class NoUnderlineUrlSpan extends URLSpan {

    public NoUnderlineUrlSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

}
