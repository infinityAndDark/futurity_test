package com.example.futurityfood.view;

import android.content.Context;
import android.util.AttributeSet;

public class ImageView1div1 extends android.support.v7.widget.AppCompatImageView {

    public ImageView1div1(Context context) {
        super(context);
    }

    public ImageView1div1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView1div1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int newHeight = Math.round(width / (1f / 1f));
        // int size = width > height ? height : width;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));

    }
}
