package com.example.futurityfood.view.bottombar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.futurityfood.R;


public class ItemTab extends LinearLayout {
    private ImageView imageIcon;
    private TextView textIcon;
    private MaterialBadgeTextView textBadge;
    private int iconSelectResource;
    private int iconNotSelectResource;
    private int colorSelectResource;
    private int colorNotSelectResource;

    private boolean isActive;

    public ItemTab(Context context) {
        super(context);
        inflateView(context);
    }

    public ItemTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
    }

    public ItemTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context);
    }

    private void inflateView(Context context) {
        inflate(context, R.layout.view_item_tab, this);
        LayoutParams param = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                1.0f
        );

        setLayoutParams(param);
        setGravity(Gravity.CENTER);
        imageIcon = findViewById(R.id.imageIcon);
        textIcon = findViewById(R.id.textIcon);
        textBadge = findViewById(R.id.textBadge);
        textBadge.setBackgroundColor(getResources().getColor(R.color.colorBadge));
    }

    public ItemTab icon(int iconResource) {
        this.iconNotSelectResource = iconResource;
        return this;
    }

    public ItemTab icon(int selectIconResource, int notSelectIconResource) {
        this.iconSelectResource = selectIconResource;
        this.iconNotSelectResource = notSelectIconResource;
        return this;
    }

    public ItemTab color(int selectColorResource, int notSelectColorResource) {
        this.colorSelectResource = selectColorResource;
        this.colorNotSelectResource = notSelectColorResource;
        return this;
    }

    public ItemTab title(String title) {
        textIcon.setText(title);
        return this;
    }

    public ItemTab size(Context context, int widthDP, int heightDP){
        float factor  =  context.getResources().getDisplayMetrics().density;
        imageIcon.getLayoutParams().width = (int)(widthDP * factor);
        imageIcon.getLayoutParams().height = (int)(heightDP * factor);
        return this;
    }

    public ItemTab build() {
        deselect();
        return this;
    }


    public void select() {
        isActive = true;
        if (iconSelectResource != 0) {
            imageIcon.setImageResource(iconSelectResource);
        } else {
            imageIcon.setImageResource(iconNotSelectResource);
            imageIcon.setColorFilter(getResources().getColor(colorSelectResource), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        textIcon.setTextColor(getResources().getColor(colorSelectResource));
        imageIcon.setAlpha(1.0f);
        textIcon.setAlpha(1.0f);
        doAnimationSelect();
    }

    public void deselect() {
        this.isActive = false;
        imageIcon.setImageResource(iconNotSelectResource);
        imageIcon.setColorFilter(null);
        textIcon.setTextColor(getResources().getColor(colorNotSelectResource));
        imageIcon.setAlpha(0.95f);
        textIcon.setAlpha(0.8f);
        post(new Runnable() {
            @Override
            public void run() {
                doAnimationDeselect();
            }
        });
    }

    private void doAnimationDeselect() {

    }

    private void doAnimationSelect() {

    }

    public void setBadge(int count) {
        if (textBadge != null) {
            textBadge.setBadgeCount(count);
        }
    }
}
