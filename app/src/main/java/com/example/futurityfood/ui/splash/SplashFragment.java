package com.example.futurityfood.ui.splash;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.example.futurityfood.R;
import com.example.futurityfood.ui.base.BaseFragment;
import com.example.futurityfood.ui.home.HomeFragment;

public class SplashFragment extends BaseFragment {
    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void setupViews(@NonNull View view) {
        view.setOnTouchListener((v, event) -> {
            try {
                makeNewScreenFlow(HomeFragment.newInstance());
            } catch (Exception e) {

            }
            return false;
        });
    }

    @Override
    protected void beginFlow(@NonNull View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    makeNewScreenFlow(HomeFragment.newInstance());
                } catch (Exception e) {

                }

            }
        }, 1500);
    }
}
