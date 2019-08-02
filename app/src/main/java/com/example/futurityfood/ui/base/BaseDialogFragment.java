package com.example.futurityfood.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.futurityfood.R;
import com.example.futurityfood.util.AppLogger;
import com.example.futurityfood.util.FragmentUtils;
import com.example.futurityfood.util.ImageUtils;
import com.example.futurityfood.util.KeyboardUtils;
import com.example.futurityfood.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseDialogFragment extends DialogFragment implements BaseView {
    public static final int DISMISS_TYPE_CANCEL = 2;
    public static final int DISMISS_TYPE_ACTION_DONE = 1;
    private static final int DELAYED_TIME_FOR_PREPARE_DIALOG = 170;
    public List<String> childTags = new ArrayList<>();
    Unbinder unbinder;
    private SoftReference<View> fragmentView;
    private boolean isLowMemory = false;
    /*NETWORK STATUS LISTENING*/

    /*DIALOG*/
    private boolean cancelOnTouchOutside = true;
    private Bundle mySavedInstanceState;
    private boolean needDoOnViewCreatedInResume;

    protected Activity myActivity() {
        try {
            return getActivity();
        } catch (Exception ignored) {
        }
        return null;
    }

    /*FRAGMENT LIFECYCLE*/
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doOnCreateFragment();
    }

    private void doOnCreateFragment() {
        if (isFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        if (fragmentView != null && fragmentView.get() != null) {
            view = fragmentView.get();
        } else
            view = inflater.inflate(provideLayout(), container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            onLoadSaveData(savedInstanceState);
        mySavedInstanceState = savedInstanceState;
        if (getUserVisibleHint()) {
            needDoOnViewCreatedInResume = false;
            doOnViewCreated(getView(), savedInstanceState, DELAYED_TIME_FOR_PREPARE_DIALOG);
        } else needDoOnViewCreatedInResume = true;
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

    private void doBeginFlow(int time) {
        if (time <= 0) {
            try {
                if (getView() != null) {
                    AppLogger.lifecycle(BaseDialogFragment.this, "beginFlow:" + getUserVisibleHint());
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
                        AppLogger.lifecycle(BaseDialogFragment.this, "beginFlow:" + getUserVisibleHint());
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
        Bundle saveData = setSaveData();
        if (saveData != null) {
            if (outState != null)
                outState.putAll(saveData);
            else outState = saveData;
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needDoOnViewCreatedInResume && getUserVisibleHint()) {
            needDoOnViewCreatedInResume = false;
            doOnViewCreated(getView(), mySavedInstanceState, 0);
            onViewStart();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setupDialog();
        if (getUserVisibleHint())
            onViewStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        onViewStop();
        ImageUtils.clear(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        unbinder = null;
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
            doOnViewCreated(getView(), mySavedInstanceState, DELAYED_TIME_FOR_PREPARE_DIALOG);
        }
    }

    protected void onViewStop() {
        AppLogger.lifecycle(BaseDialogFragment.this, "onViewStop");
        // TODO: 14/09/2018 call when view is not visible, not call onStop
    }

    protected void onViewStart() {
        AppLogger.lifecycle(BaseDialogFragment.this, "onViewStart");
        handleDataSendingBetweenScreens();
        if (fragmentView == null || fragmentView.get() == null)
            doOnViewCreated(getView(), mySavedInstanceState, 0);
        // TODO: 14/09/2018 call when view is not visible, not call onStart
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        clearViews();
    }

    private void clearViews() {
        if (fragmentView != null)
            fragmentView.clear();
        isLowMemory = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        FragmentUtils.replaceChild(getChildFragmentManager(), containerLayoutResource, fragment);
    }

    public <F extends Fragment> void goToScreen(int containerLayoutResource,
                                                @NonNull F fragment,
                                                int actionFlag, View element) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).goToScreen(containerLayoutResource, fragment, actionFlag, element);
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
            List<BaseActivity.Request> requests = ((BaseActivity) getActivity()).getRequest(getTag());
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
        return (BaseFragment) getChildFragmentManager().findFragmentByTag(tag);
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

    protected void registerEventBus() {
        EventBus.getDefault().register(this);

    }

    protected void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * Change language --> restart app
     */
    protected void changeLanguage() {
        if (getActivity() != null) ((BaseActivity) getActivity()).changeLanguage();
    }

    protected void handleKeyboardScrollView(View view) {
        KeyboardUtils.handleKeyboardTouchView(view);
    }

    /*FOR DIALOG*/
    public boolean isShowing() {
        if (getDialog() == null) return false;
        return getDialog().isShowing();
    }

    private int dismissType;
    private Object dataBack;

    public void setDataBack(Object dataBack) {
        this.dataBack = dataBack;
    }

    public void hide(int dismissType) {
        if (BaseActivity.isLockBackPress()) return;
        if (!BaseActivity.canBackPress) return;
//        BaseActivity.delayBackPress(FragmentUtils.DELAYED_TIME_FOR_PREPARE_SCREEN);
        this.dismissType = dismissType;
        dismissAllowingStateLoss();
    }

    private boolean isFullScreen = true;

    protected void setFullScreen(boolean isFullscreen) {
        this.isFullScreen = isFullscreen;
    }

    public interface DismissListener<D> {

        void onDismiss(int dismissType, D data);
    }

    private DismissListener dismissListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                BaseDialogFragment.this.hide(DISMISS_TYPE_CANCEL);
            }

        };
    }

    public void setCancelOnTouchOutside(boolean cancelOnTouchOutside) {
        this.cancelOnTouchOutside = cancelOnTouchOutside;
    }

    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void setupDialog() {
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dismissListener != null) {
                    dismissListener.onDismiss(dismissType, dataBack);
                    dismissListener = null;
                }
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                hide(DISMISS_TYPE_CANCEL);
            }
        });
    }

    protected void setDialogSize() {
        Dialog dialog = getDialog();
        if (dialog == null) return;
        int defaultHeight = ScreenUtils.getScreenActiveHeight(dialog.getContext()) / 3 * 2;
        dialog.getWindow()
                .setLayout(
                        dialog.getWindow().getAttributes().width,
                        defaultHeight);
    }

    protected void setDialogHeight(int height) {
        Dialog dialog = getDialog();
        if (dialog == null) return;
        dialog.getWindow()
                .setLayout(
                        dialog.getWindow().getAttributes().width,
                        height);
    }

    protected void setDialogSize(int ratio1, int ratio2) {
        Dialog dialog = getDialog();
        if (dialog == null) return;
        int defaultHeight = ScreenUtils.getScreenActiveHeight(dialog.getContext()) / ratio1 * ratio2;
        dialog.getWindow()
                .setLayout(
                        dialog.getWindow().getAttributes().width,
                        defaultHeight);
    }
}