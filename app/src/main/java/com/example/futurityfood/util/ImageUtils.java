package com.example.futurityfood.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.futurityfood.R;
import com.example.futurityfood.view.GlideApp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class ImageUtils {
    private ImageUtils() {
    }

    public static <S> void loadImage(@NonNull ImageView imageView, S source) {
        loadImage(imageView, source, R.drawable.bg_place_hole_image, R.drawable.bg_place_hole_image);
    }

    public static <S> void loadImage(@NonNull ImageView imageView, S source, int defaultResource, int placeHole) {
        GlideApp.with(imageView.getContext()).
                load((source == null) ? defaultResource : source)
                .placeholder(placeHole)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .format(DecodeFormat.PREFER_RGB_565)
                .error(defaultResource)
                .fallback(defaultResource)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .fitCenter()
//                .thumbnail(0.2f)
                .into(imageView);
    }

    public static <S> void loadImageRoundCorner(@NonNull ImageView imageView, S source) {
        loadImageRoundCorner(imageView, source, R.drawable.bg_place_hole_image, R.drawable.bg_place_hole_image);
    }

    public static <S> void loadImageRoundCorner(@NonNull ImageView imageView, S source, int defaultResource, int placeHole) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions
                .transforms(new CenterCrop(), new RoundedCorners(ViewUtils.dpToPx(5)))
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(placeHole)
                .error(defaultResource)
                .fallback(defaultResource)
        ;
        GlideApp.with(imageView.getContext()).
                load((source == null) ? defaultResource
                        : source)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }

}
