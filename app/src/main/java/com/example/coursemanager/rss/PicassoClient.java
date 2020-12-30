package com.example.coursemanager.rss;

import android.content.Context;
import android.widget.ImageView;

import com.example.coursemanager.R;
import com.squareup.picasso.Picasso;

public class PicassoClient {
    public static void downloadImage(Context context, String imageUrl, ImageView imageView) {
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.get().load(imageUrl).into(imageView);
        } else {
            Picasso.get().load(R.drawable.ic_news).into(imageView);
        }
    }
}
