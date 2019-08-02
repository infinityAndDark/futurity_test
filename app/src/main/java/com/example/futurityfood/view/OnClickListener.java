package com.example.futurityfood.view;

import android.os.SystemClock;
import android.view.View;

public abstract class OnClickListener implements View.OnClickListener {
    private int DELAYED_TIME = 700;
    private long mLastClickTime = 0;

    public OnClickListener() {
    }

    public OnClickListener(int DELAYED_TIME) {
        this.DELAYED_TIME = DELAYED_TIME;
    }

    @Override
    public void onClick(final View v) {
        if ((SystemClock.elapsedRealtime() - mLastClickTime) < DELAYED_TIME) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        onDelayedClick(v);
    }

    public abstract void onDelayedClick(View v);
}
