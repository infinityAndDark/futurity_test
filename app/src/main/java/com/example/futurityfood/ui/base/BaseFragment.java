package com.example.futurityfood.ui.base;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.futurityfood.BaseApplication;
import com.example.futurityfood.R;
import com.example.futurityfood.util.AppLogger;
import com.example.futurityfood.util.FragmentUtils;
import com.example.futurityfood.util.ImageUtils;
import com.example.futurityfood.util.KeyboardUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment implements BaseView, ScreenHistory.Info {

    public List<String> childTags = new ArrayList<>();
    Unbinder unbinder;
    private SoftReference<View> fragmentView;
    private boolean isLowMemory = false;

    private boolean needDoOnViewCreatedInResume = false;
    private Bundle mySavedInstanceState;
    public static int DELAYED_TIME_START_SCREEN = FragmentUtils.DELAYED_TIME_FOR_PREPARE_SCREEN;

    public static String getName(Class className) {
        return FragmentUtils.getName(className);
    }

    public String getName() {
        return getName(this.getClass());
    }

    protected BaseActivity myActivity() {
        try {
            return (BaseActivity) getActivity();
        } catch (Exception ignored) {
        }
        return null;
    }

    protected BaseApplication myApplication() {
        try {
            return myActivity().myApplication();
        } catch (Exception ignored) {
        }
        return null;
    }

    /*FRAGMENT LIFECYCLE*/
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.lifecycle(this, "onCreate:" + getUserVisibleHint());
        doOnCreateFragment();
    }

    private void doOnCreateFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AppLogger.lifecycle(this, "onCreateView:" + getUserVisibleHint());
        View view;
        if (fragmentView != null && fragmentView.get() != null) {
            view = fragmentView.get();
        } else {
            view = inflater.inflate(provideLayout(), container, false);
            view.setClickable(true);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideKeyboard();
                    return false;
                }
            });
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainBackground();
        mySavedInstanceState = savedInstanceState;
        AppLogger.lifecycle(this, "onActivityCreated:" + getUserVisibleHint());
        if (savedInstanceState != null)
            onLoadSaveData(savedInstanceState);
        if (getUserVisibleHint()) {
            needDoOnViewCreatedInResume = false;
            doOnViewCreated(getView(), savedInstanceState, DELAYED_TIME_START_SCREEN);
        } else needDoOnViewCreatedInResume = true;
    }

    protected void setMainBackground() {
        if (getView() != null)
            getView().setBackgroundResource(R.color.colorPrimary);
        // myActivity().getWindow().setBackgroundDrawableResource(R.color.colorBackground);
    }

    private boolean mStatusWhite = false;

    protected void setStatusBarContentWhite() {
        mStatusWhite = true;
        ScreenHistory.updateHistory(this);
        if (myActivity() != null) myActivity().setStatusBarContentWhite();
    }

    protected void setStatusBarContentBlack() {
        mStatusWhite = false;
        ScreenHistory.updateHistory(this);
        if (myActivity() != null) myActivity().setStatusBarContentBlack();
    }


    protected void setNavigationBarColor(int colorResource) {
        if (myActivity() == null)
            throw new NullPointerException("Call this inside method setupView() or beginFlow(), or when Fragment is resume");
        myActivity().getWindow().setNavigationBarColor(myActivity().getResources().getColor(colorResource));
    }

    private void doOnViewCreated(View view, Bundle savedInstanceState, int time) {
        if (fragmentView == null || fragmentView.get() == null || savedInstanceState != null) {
            fragmentView = new SoftReference<>(view);
            setupViews(view);
            if (savedInstanceState != null)
                onLoadSavedViewData(savedInstanceState);
            if (isLowMemory) isLowMemory = false;
            doBeginFlow(time);
        }
    }

    protected void onLoadSavedViewData(Bundle data) {

    }

    private boolean doBeginFlowSuccess;

    private void doBeginFlow(int time) {
        if (time <= 0) {
            try {
                if (getView() != null) {
                    doBeginFlowSuccess = true;
                    AppLogger.lifecycle(BaseFragment.this, "beginFlow:" + getUserVisibleHint());
                    beginFlow(getView());
                }
            } catch (Exception e) {
                logError("begin flow error");
            }
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getView() != null) {
                        doBeginFlowSuccess = true;
                        AppLogger.lifecycle(BaseFragment.this, "beginFlow:" + getUserVisibleHint());
                        beginFlow(getView());
                    }
                } catch (Exception e) {
                    logError("begin flow error");
                }
            }
        }, time);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        AppLogger.lifecycle(BaseFragment.this, "onSaveInstanceState");
        Bundle saveData = setSaveData();
        if (saveData != null) {
            if (outState != null)
                outState.putAll(saveData);
            else outState = saveData;
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        unbinder = null;
        AppLogger.lifecycle(BaseFragment.this, "onDestroy");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLogger.lifecycle(BaseFragment.this, "onHiddenChanged: " + hidden);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.lifecycle(BaseFragment.this, "onActivityResult: request:" + requestCode + " result:" + resultCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AppLogger.lifecycle(BaseFragment.this, "onRequestPermissionsResult: request:" + requestCode);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        AppLogger.lifecycle(BaseFragment.this, "onCreateAnimation");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        AppLogger.lifecycle(BaseFragment.this, "onCreateAnimator");
        return super.onCreateAnimator(transit, enter, nextAnim);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            doOnViewVisible();
            if (fragmentView != null && fragmentView.get() != null)
                onViewStart();
        }
        if (!getUserVisibleHint()) {
            if (fragmentView != null && fragmentView.get() != null)
                onViewStop();
        }
    }

    private void doOnViewVisible() {
        if (getView() == null) return;
        if (needDoOnViewCreatedInResume) {
            needDoOnViewCreatedInResume = false;
            doOnViewCreated(getView(), mySavedInstanceState, DELAYED_TIME_START_SCREEN);
        }
    }

    protected void onViewStop() {
        AppLogger.lifecycle(BaseFragment.this, "onViewStop");
        ScreenHistory.History history = ScreenHistory.getPreviousScreen();
        if (history != null) {
            if (history.statusBarLight) setStatusBarContentWhite();
            else setStatusBarContentBlack();
        }

        // TODO: 14/09/2018 call when view is not visible, not call onStop
    }

    private boolean isFirstViewStart = true;

    protected void onViewStart() {
        AppLogger.lifecycle(BaseFragment.this, "onViewStart");
        if (isFirstViewStart) isFirstViewStart = false;
        else if (!doBeginFlowSuccess) doBeginFlow(0);
        handleDataSendingBetweenScreens();
        setStatusBarColor();
        if (fragmentView == null || fragmentView.get() == null)
            doOnViewCreated(getView(), mySavedInstanceState, 0);
        // TODO: 14/09/2018 call when view is not visible, not call onStart
    }

    protected void setStatusBarColor() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needDoOnViewCreatedInResume && getUserVisibleHint()) {
            needDoOnViewCreatedInResume = false;
            doOnViewCreated(getView(), mySavedInstanceState, 0);
            onViewStart();
        }
        AppLogger.lifecycle(this, "onResume:" + getUserVisibleHint());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLogger.lifecycle(this, "onPause:" + getUserVisibleHint());
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLogger.lifecycle(this, "onStart:" + getUserVisibleHint());
        if (getUserVisibleHint())
            onViewStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        AppLogger.lifecycle(this, "onStop:" + getUserVisibleHint());
        onViewStop();
        ImageUtils.clear(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLogger.lifecycle(this, "onDestroyView:" + getUserVisibleHint());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppLogger.lifecycle(this, "onLowMemory:" + getUserVisibleHint());
        clearViews();
    }

    private void clearViews() {
        if (fragmentView != null) fragmentView.clear();
        fragmentView = null;
        isLowMemory = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AppLogger.lifecycle(this, "onAttach:" + getUserVisibleHint());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AppLogger.lifecycle(this, "onDetach:" + getUserVisibleHint());
    }

    @Override
    public void showKeyboard(EditText editText) {
        if (getActivity() != null) ((BaseActivity) getActivity()).showKeyboard(editText);
    }

    @Override
    public void hideKeyboard() {
        if (getActivity() != null) ((BaseActivity) getActivity()).hideKeyboard();
    }

    @Override
    public boolean isNetworkConnected() {
        return getActivity() != null && ((BaseActivity) getActivity()).isNetworkConnected();
    }


    /*REQUEST PERMISSION*/

    /**
     * Request permission for specify use case
     *
     * @param permission                permission
     * @param runAfterRequestPermission what to do
     */
    protected void requestPermission(@NonNull String permission, Runnable runAfterRequestPermission) throws Exception {
        if (getActivity() == null) throw new Exception();
        ((BaseActivity) getActivity()).requestPermission(permission, runAfterRequestPermission);
    }

    protected void requestPermission(@NonNull String[] permissions, Runnable runAfterRequestPermission) throws Exception {
        if (getActivity() == null) throw new Exception();
        ((BaseActivity) getActivity()).requestPermission(permissions, runAfterRequestPermission);
    }

    /*SCREEN MOVING*/
    public <F extends Fragment> void makeNewScreenFlow(@NonNull F fragment) {
        goToScreen(FragmentUtils.CONTAINER_MAIN, fragment, FragmentUtils.FLAG_NEW_TASK, null);
    }

    public <F extends Fragment> void goToScreen(@NonNull F fragment) {
        goToScreen(FragmentUtils.CONTAINER_MAIN, fragment, FragmentUtils.FLAG_ADD, null);
    }

    public <F extends Fragment> void addContent(int containerLayoutResource,
                                                @NonNull F fragment) {
        if (containerLayoutResource != FragmentUtils.CONTAINER_MAIN)
            if (!childTags.contains(FragmentUtils.getName(fragment.getClass())))
                childTags.add(FragmentUtils.getName(fragment.getClass()));
        DELAYED_TIME_START_SCREEN = FragmentUtils.DELAYED_TIME_FOR_PREPARE_SCREEN;
        FragmentUtils.replaceChild(getChildFragmentManager(), containerLayoutResource, fragment);
    }

    public <F extends Fragment> void goToScreen(int containerLayoutResource,
                                                @NonNull F fragment,
                                                int actionFlag, View element) {
        DELAYED_TIME_START_SCREEN = FragmentUtils.DELAYED_TIME_FOR_PREPARE_SCREEN;
        if (getActivity() != null)
            ((BaseActivity) getActivity()).goToScreen(containerLayoutResource, fragment, actionFlag, element);
    }

    public void moveBack() {
        if (BaseActivity.isLockBackPress()) return;
        if (!BaseActivity.canBackPress) return;
        if (onBackPressed()) return;
        BaseActivity.delayBackPress(DELAYED_TIME_START_SCREEN);
        if (getActivity() != null) ((BaseActivity) getActivity()).moveBack();
    }

    public void forceMoveBack() {
        if (BaseActivity.isLockBackPress()) return;
        if (!BaseActivity.canBackPress) return;
        BaseActivity.delayBackPress(DELAYED_TIME_START_SCREEN);
        if (getActivity() != null) ((BaseActivity) getActivity()).moveBack();
    }

    /**
     * Back to screen with it's name
     *
     * @param screenName name of screen: fragment.getClass().getSimpleName()
     */
    public void backToScreen(@NonNull String screenName) {
        if (getActivity() != null) ((BaseActivity) getActivity()).backToScreen(screenName);
    }

    /**
     * Start new Activity
     *
     * @param screenClass class of Activity
     */
    public void startActivity(@NonNull Class screenClass) {
        startActivity(new Intent(getActivity(), screenClass));
    }


    /*SCREEN CREATION*/

    /**
     * @return layout resource
     */
    protected abstract int provideLayout();

    protected abstract void setupViews(@NonNull View view);

    /**
     * @param view fragment's view
     */
    protected abstract void beginFlow(@NonNull View view);

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

    /*SENDING DATA BETWEEN SCREENS*/
    protected void sendDataToScreen(String screenName, Object data) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).putRequest(getTag(), screenName, data);
    }

    public void handleDataSendingBetweenScreens() {
        if (getActivity() != null) {
            List<BaseActivity.Request> requests = myActivity().getRequest(getTag());
            if (!requests.isEmpty()) {
                for (BaseActivity.Request request : requests) {
                    receiveData(request.fromScreen, request.data);
                }

            }
            if (!childTags.isEmpty()) {
                for (String tag : childTags) {
                    BaseFragment childFragment = getChildFragment(tag);
                    if (childFragment != null)
                        childFragment.handleDataSendingBetweenScreens();
                }
            }
        }
    }

    private BaseFragment getChildFragment(String tag) {
        try {
            return (BaseFragment) getChildFragmentManager().findFragmentByTag(tag);
        } catch (Exception e) {
            return null;
        }
    }

    protected void receiveData(String fromScreen, Object data) {

    }

    /*LOGGER*/
    protected <T> void logError(T message) {
        AppLogger.error(this.getClass().getSimpleName(), message);
    }

    protected <T> void logDebug(T message) {
        AppLogger.debug(this.getClass().getSimpleName(), message);
    }

    @Override
    public void restart() {
        if (getActivity() != null) ((BaseActivity) getActivity()).restart();
    }

    /*EVENT BUS*/
    protected void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    protected void postStickyEvent(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    protected void removeEvent(Object event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    protected void registerEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);

    }

    protected void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * Just use for one fragment in top of fragment's stack, not use for it's childs
     */
    protected boolean onBackPressed() {
//        if(getFragmentManager().getBackStackEntryCount()>1)
//        getFragmentManager().beginTransaction().remove(this).commitNowAllowingStateLoss();
        return false;
    }

    /**
     * Working with dialog
     */
    protected <D extends BaseDialogFragment> void showDialog(D dialogFragment) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showDialogFragment(dialogFragment);
    }

    /**
     * Change language --> restart app
     */
    protected void changeLanguage() {
        if (getActivity() != null) ((BaseActivity) getActivity()).changeLanguage();
    }

    protected void handleKeyboardTouchView(ScrollView scrollView) {
        KeyboardUtils.handleKeyboardTouchView(scrollView);
    }


    @Override
    public String getScreenName() {
        return getName();
    }

    @Override
    public boolean isStatusBarLight() {
        return mStatusWhite;
    }

}