package com.example.futurityfood.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.futurityfood.R;
import com.example.futurityfood.ui.base.BaseFragment;
import com.example.futurityfood.ui.home.checkouttab.CheckoutFragment;
import com.example.futurityfood.ui.home.maintab.MainFragment;
import com.example.futurityfood.ui.home.ordertab.OrderFragment;
import com.example.futurityfood.ui.home.profiletab.ProfileFragment;
import com.example.futurityfood.view.bottombar.BottomTab;
import com.example.futurityfood.view.bottombar.ItemTab;

import java.lang.ref.SoftReference;

import butterknife.BindView;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.mainTabNavigation)
    BottomTab mainTabNavigation;

    int currentTabPosition = 0;
    private SoftReference<MainFragment> main;
    private SoftReference<OrderFragment> order;
    private SoftReference<ProfileFragment> profile;
    private SoftReference<CheckoutFragment> checkout;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupViews(@NonNull View view) {
        mainTabNavigation.addTab(new ItemTab(getContext())
                .icon(R.drawable.ic_home_select, R.drawable.ic_home)
                .color(R.color.colorAccent, R.color.colorDisable)
                .title(getString(R.string.tab_main))
                .build()
        );
        mainTabNavigation.addTab(new ItemTab(getContext())
                .icon(R.drawable.ic_order_select, R.drawable.ic_order)
                .color(R.color.colorAccent, R.color.colorDisable)
                .title(getString(R.string.tab_order))
                .build()
        );
        mainTabNavigation.addTab(new ItemTab(getContext())
                .icon(R.drawable.ic_profile_select, R.drawable.ic_profile)
                .color(R.color.colorAccent, R.color.colorDisable)
                .title(getString(R.string.tab_profile))
                .build()
        );
        mainTabNavigation.addTab(new ItemTab(getContext())
                .icon(R.drawable.ic_checkout_select, R.drawable.ic_checkout)
                .color(R.color.colorAccent, R.color.colorDisable)
                .title(getString(R.string.tab_checkout))
                .build()
        );
        mainTabNavigation.build();
        mainTabNavigation.setTabSelectedListener(i -> {
            currentTabPosition = i;
            goToTab(currentTabPosition);
        });
    }

    @Override
    protected void beginFlow(@NonNull View view) {
    }

    @Override
    protected void setStatusBarColor() {
        super.setStatusBarColor();
    }

    private void goToTab(int tabPosition) {
        BaseFragment goFragment = null;
        switch (tabPosition) {
            case 0:
                if (main == null || main.get() == null)
                    main = new SoftReference<>(MainFragment.newInstance());
                goFragment = main.get();
                break;
            case 1:
                if (order == null || order.get() == null)
                    order = new SoftReference<>(OrderFragment.newInstance());
                goFragment = order.get();
                break;
            case 2:
                if (profile == null || profile.get() == null)
                    profile = new SoftReference<>(ProfileFragment.newInstance());
                goFragment = profile.get();
                break;
            case 3:
                if (checkout == null || checkout.get() == null)
                    checkout = new SoftReference<>(CheckoutFragment.newInstance());
                goFragment = checkout.get();
                break;
        }
        if (goFragment != null)
            addContent(R.id.mainPager, goFragment);
    }
    @Override
    protected void onViewStart() {
        super.onViewStart();
        mainTabNavigation.selectTab(currentTabPosition);
    }

    @Override
    protected boolean onBackPressed() {
        if (currentTabPosition != 0) {
            try {
                currentTabPosition = 0;
                mainTabNavigation.selectTab(currentTabPosition);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return super.onBackPressed();
    }
    @Override
    protected Bundle setSaveData() {
        Bundle bundle = new Bundle();
        bundle.putInt("CURRENT_TAB", currentTabPosition);
        return bundle;
    }

    @Override
    protected void onLoadSaveData(Bundle data) {
        super.onLoadSaveData(data);
        if (data == null) return;
        currentTabPosition = data.getInt("CURRENT_TAB");
    }
}
