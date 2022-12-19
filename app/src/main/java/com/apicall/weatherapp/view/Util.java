package com.apicall.weatherapp.view;

import android.widget.ImageView;

import com.apicall.weatherapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Util {

    public static void loadImage(ImageView view, String url) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(view.getContext())
                .load(url)
                .apply(options)
                .into(view);
    }
}

