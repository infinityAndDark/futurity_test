package com.example.futurityfood.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public final class ViewUtils {
    private ViewUtils() {
    }

    /**
     * @param px
     * @return
     */
    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    /**
     * @param dp
     * @return
     */
    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int spToPx(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
