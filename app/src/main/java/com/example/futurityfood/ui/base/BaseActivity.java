package com.example.futurityfood.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.futurityfood.AppConfigs;
import com.example.futurityfood.BaseApplication;
import com.example.futurityfood.util.AppLogger;
import com.example.futurityfood.util.AppUtils;
import com.example.futurityfood.util.FragmentUtils;
import com.example.futurityfood.util.ImageUtils;
import com.example.futurityfood.util.KeyboardUtils;
import com.example.futurityfood.util.NetworkUtils;
import com.example.futurityfood.util.PermissionUtils;
import com.example.futurityfood.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private static final int TIME_DELAYED_BACK_PRESS = 250;
    private static boolean lockBackPress = false;
    public static boolean canBackPress = true;
    /*MEMORY MANAGEMENT*/
    protected boolean needRestart = false;
    private boolean isFirstFragment = false;
    /*SENDING DATA BETWEEN SCREENS*/
    private List<Request> requests = new ArrayList<>();
    /**
     * Use for store runnable of request permission.
     */
    private Runnable runAfterRequestPermission;
    private boolean firstTimeStartApp = true;

    /**
     * Use for checking saved instance state to show dialog fragment
     */
    private boolean isSavedInstanceStateDone;

    @Override
    public Context getContext() {
        return this;
    }

    /*ACTIVITY LIFECYCLE*/
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppLogger.lifecycle(BaseActivity.this, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(provideLayout());
        detectFirstFragment();
        ButterKnife.bind(this);
        setupViews();
        onLoadSaveData(savedInstanceState);
        beginFlow();
    }

    public void setStatusBarContentWhite() {
        ScreenUtils.setOverlayStatusBar(getWindow(), true);
    }

    public void setStatusBarContentBlack() {
        ScreenUtils.setOverlayStatusBar(getWindow(), false);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        AppLogger.lifecycle(BaseActivity.this, "onSaveInstanceState");
        Bundle saveData = setSaveData();
        if (saveData != null) outState.putAll(saveData);
        super.onSaveInstanceState(outState);
        isSavedInstanceStateDone = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLogger.lifecycle(BaseActivity.this, "onStart");
        isSavedInstanceStateDone = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.lifecycle(BaseActivity.this, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLogger.lifecycle(BaseActivity.this, "onDestroy");
    }

    public static boolean isLockBackPress() {
        return lockBackPress;
    }

    public static void lockBackPress() {
        lockBackPress = true;
    }

    public static void unlockBackPress() {
        lockBackPress = false;
    }

    @Override
    public void onBackPressed() {
        AppLogger.lifecycle(BaseActivity.this, "onBackPressed");
        if (lockBackPress) return;
        if (!canBackPress) return;
        delayBackPress(TIME_DELAYED_BACK_PRESS);
        BaseFragment baseFragment = getTopFragment();
        if (baseFragment != null) {
            if (baseFragment.onBackPressed()) {
                return;
            }
        }
        if (isFirstFragment || isFirstFragment()) {
            AppUtils.runOutOfApp(this);
        } else {
            ScreenHistory.popHistory();
            try {
                super.onBackPressed();
            } catch (Exception e) {
                logError(e);
                reload();
            }
        }
    }

    public static void delayBackPress(int time) {
        canBackPress = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canBackPress = true;
            }
        }, time);
    }

    private BaseFragment getTopFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentByTag(getTopFragmentName());
    }

    protected String getTopFragmentName() {
        return getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
    }

    private boolean isFirstFragment() {
        try {
            return getSupportFragmentManager().getBackStackEntryCount() == 1;
        } catch (Exception ignored) {
            logError(ignored);
        }
        return false;
    }

    public int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    private void detectFirstFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                isFirstFragment = getSupportFragmentManager().getBackStackEntryCount() == 1;
            }
        });
    }

    @Override
    public void showKeyboard(EditText editText) {
        KeyboardUtils.showKeyboard(editText, this);
    }

    @Override
    public void hideKeyboard() {
        KeyboardUtils.hideKeyboard(this);
    }

    @Override
    public boolean isNetworkConnected() {
        return getApplicationContext() != null && NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    /**
     * @return layout resource
     */
    protected abstract int provideLayout();

    /**
     * Use for handle all action in views
     */
    protected abstract void setupViews();

    /**
     * Call at first time create an activity to start flow, put logic here if needed.
     */
    protected abstract void beginFlow();

    /**
     * save state of screen when leave screen
     *
     * @return data
     */
    protected Bundle setSaveData() {
        return null;
    }

    /**
     * Restore state for screen when come back
     *
     * @param data ...
     */
    protected void onLoadSaveData(Bundle data) {

    }

    public void putRequest(String fromScreen, String toScreen, Object data) {
        requests.add(new Request(fromScreen, toScreen, data));
    }

    public List<Request> getRequest(String toScreen) {
        List<Request> requests = new ArrayList<>();
        for (int i = 0; i < this.requests.size(); i++) {
            if (this.requests.get(i).toScreen.equals(toScreen)) {
                requests.add(this.requests.get(i));
                this.requests.remove(i);
                i--;
            }
        }
        return requests;
    }

    /*SCREENS MOVING*/
    public <F extends Fragment> void makeNewScreenFlow(@NonNull F fragment) {
        goToScreen(FragmentUtils.CONTAINER_MAIN, fragment, FragmentUtils.FLAG_NEW_TASK, null);
    }

    public <F extends Fragment> void goToScreen(@NonNull F fragment) {
        Fragment topFragment = getTopFragment();
        if (topFragment != null)
            if (BaseFragment.getName(fragment.getClass()).equals(BaseFragment.getName(topFragment.getClass())))
                return;
        goToScreen(FragmentUtils.CONTAINER_MAIN, fragment, FragmentUtils.FLAG_ADD, null);
    }

    public <F extends Fragment> void goToScreen(int containerLayoutResource,
                                                @NonNull F fragment,
                                                int actionFlag, View element) {
        BaseFragment.DELAYED_TIME_START_SCREEN = FragmentUtils.DELAYED_TIME_FOR_PREPARE_SCREEN;
        delayBackPress(BaseFragment.DELAYED_TIME_START_SCREEN);
        hideKeyboard();
        if (actionFlag == FragmentUtils.FLAG_ADD) {
            FragmentUtils.add(getSupportFragmentManager(), containerLayoutResource, fragment, true, element, true);
        } else if (actionFlag == FragmentUtils.FLAG_REPLACE) {
            FragmentUtils.replace(getSupportFragmentManager(), containerLayoutResource, fragment, false, element, false);
        } else if (actionFlag == FragmentUtils.FLAG_NEW_TASK) {
            clearAllScreens();
            ScreenHistory.clearHistory();
            FragmentUtils.replace(getSupportFragmentManager(), containerLayoutResource, fragment, true, element, false);
        }
        ScreenHistory.addHistory((ScreenHistory.Info) fragment);
    }

    public <D extends BaseDialogFragment> void showDialogFragment(D dialogFragment) {
        hideKeyboard();

        // To avoid Fatal Exception: java.lang.IllegalStateException
        // Can not perform fragment commit action after onSaveInstanceState
        if (isSavedInstanceStateDone()) return;

        FragmentUtils.showDialog(getSupportFragmentManager(), dialogFragment);
    }

    public void backToScreen(String screenName) {
        hideKeyboard();
        FragmentUtils.backToFragment(getSupportFragmentManager(), screenName);
        ScreenHistory.popHistoryTo(screenName);
    }

    public void moveBack() {
        hideKeyboard();
        ScreenHistory.popHistory();
        FragmentUtils.moveBack(getSupportFragmentManager(), this);
    }

    private void clearAllScreens() {
        FragmentUtils.clearBackStack(getSupportFragmentManager());
    }

    /**
     * Request permission for specify use case
     *
     * @param permission                what permission want to granted.
     * @param runAfterRequestPermission what logic doing after request permission.
     */
    public void requestPermission(@NonNull String permission, Runnable runAfterRequestPermission) {
        if (PermissionUtils.requestPermission(this, permission, AppConfigs.REQUEST_CODE_PERMISSION))
            runFlowAfterRequestPermission(runAfterRequestPermission);
        else this.runAfterRequestPermission = runAfterRequestPermission;

    }

    public void requestPermission(@NonNull String[] permissions, Runnable runAfterRequestPermission) {
        if (PermissionUtils.requestPermissions(this, permissions, AppConfigs.REQUEST_CODE_PERMISSION))
            runFlowAfterRequestPermission(runAfterRequestPermission);
        else this.runAfterRequestPermission = runAfterRequestPermission;

    }

    /**
     * Doing logic after request permission done and granted
     *
     * @param runnable what to do
     */
    private void runFlowAfterRequestPermission(Runnable runnable) {
        if (runnable != null) {
            new Handler().postDelayed(runnable, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        AppLogger.lifecycle(BaseActivity.this, "onRequestPermissionsResult: request:" + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.isGranted(grantResults)) {
            runFlowAfterRequestPermission(runAfterRequestPermission);
            runAfterRequestPermission = null;
        }
    }

    /*ACTIVITY RESULTS*/
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.lifecycle(BaseActivity.this, "onRequestPermissionsResult: request:" + requestCode + " result:" + resultCode);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        AppLogger.lifecycle(BaseActivity.this, "onTrimMemory:" + level);
        ImageUtils.clear(getApplicationContext());
        if (level == TRIM_MEMORY_COMPLETE) {
            AppLogger.memory("TRIM_MEMORY_COMPLETE=" + level);
        } else if (level == TRIM_MEMORY_UI_HIDDEN) {
            AppLogger.memory("TRIM_MEMORY_UI_HIDDEN=" + level);
        } else if (level == TRIM_MEMORY_BACKGROUND) {
            AppLogger.memory("TRIM_MEMORY_BACKGROUND=" + level);
        } else if (level == TRIM_MEMORY_MODERATE) {
            AppLogger.memory("TRIM_MEMORY_MODERATE=" + level);
        } else if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            AppLogger.memory("TRIM_MEMORY_RUNNING_CRITICAL=" + level);
        } else if (level == TRIM_MEMORY_RUNNING_LOW) {
            AppLogger.memory("TRIM_MEMORY_RUNNING_LOW=" + level);
        } else if (level == TRIM_MEMORY_RUNNING_MODERATE) {
            AppLogger.memory("TRIM_MEMORY_RUNNING_MODERATE=" + level);
        } else {
            AppLogger.memory("TRIM_MEMORY_UNKNOWN=" + level);
        }
        if (firstTimeStartApp) {
            firstTimeStartApp = false;
        } else {
//            if (level >= 80) {
//
//                needRestart = true;
//            } else if (level >= 20 && level < 80) {
//                System.gc();
//            }
        }

//        https://developer.android.com/topic/performance/memory.html
    }

    /*LOGGER*/
    protected <T> void logError(T message) {
        AppLogger.error(this.getClass().getSimpleName(), message);
    }

    protected <T> void logDebug(T message) {
        AppLogger.debug(this.getClass().getSimpleName(), message);
    }

    /*RESTART APP SCREEN*/
    @Override
    public abstract void restart();

    /*EVENT BUS*/
    protected void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    protected void postStickyEvent(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    protected void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    public BaseApplication myApplication() {
        try {
            return (BaseApplication) getApplication();
        } catch (Exception ignored) {

        }
        return null;
    }

    public static class Request {
        public Object data;
        String fromScreen;
        String toScreen;

        Request(String fromScreen, String toScreen, Object data) {
            this.fromScreen = fromScreen;
            this.toScreen = toScreen;
            this.data = data;
        }
    }

    public void changeLanguage() {
        reload();
    }

    protected abstract void reload();

    public void handleKeyboardTouchView(View view) {
        KeyboardUtils.handleKeyboardTouchView(view);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AppLogger.lifecycle(BaseActivity.this, "onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppLogger.lifecycle(BaseActivity.this, "onLowMemory");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLogger.lifecycle(BaseActivity.this, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSavedInstanceStateDone = false;
        AppLogger.lifecycle(BaseActivity.this, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppLogger.lifecycle(BaseActivity.this, "onRestart");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        AppLogger.lifecycle(BaseActivity.this, "onAttachedToWindow");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AppLogger.lifecycle(BaseActivity.this, "onDetachedFromWindow");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        AppLogger.lifecycle(BaseActivity.this, "onAttachFragment:" + fragment.getClass().getSimpleName());
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        AppLogger.lifecycle(BaseActivity.this, "onUserLeaveHint");
    }

    /**
     * Returns true if SavedInstanceState was done, and activity was not restarted or resumed yet.
     */
    public boolean isSavedInstanceStateDone() {
        return isSavedInstanceStateDone;
    }
}