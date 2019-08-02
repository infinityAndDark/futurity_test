package com.example.futurityfood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.futurityfood.R;
import com.example.futurityfood.ui.base.BaseActivity;
import com.example.futurityfood.ui.splash.SplashFragment;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarContentBlack();
    }

    @Override
    protected int provideLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void beginFlow() {
        startScreens();
    }

    @Override
    public void restart() {
        startScreens();
    }

    @Override
    protected void reload() {
        startScreens();
    }

    private void startScreens() {
        makeNewScreenFlow(SplashFragment.newInstance());
    }
}
