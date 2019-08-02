package com.example.futurityfood.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.transition.TransitionInflater;
import android.view.View;

import com.example.futurityfood.R;
import com.example.futurityfood.ui.base.BaseDialogFragment;

public final class FragmentUtils {
    public static final int DELAYED_TIME_FOR_PREPARE_SCREEN = 120;
    public static final int CONTAINER_MAIN = android.R.id.content;
//    public static final int CONTAINER_MAIN = R.id.container_god;
    /**
     * Replace current fragment with new fragment and addData new fragment to back stack
     */
    public static final int FLAG_ADD = 0;
    /**
     * Replace current fragment with new one, not addData to back stack
     */
    public static final int FLAG_REPLACE = 1;
//    public static final int CONTAINER_MAIN = R.id.container_god;
    /**
     * Clear all fragments in back stack and addData new fragment
     */
    public static final int FLAG_NEW_TASK = 2;
    public static boolean sDisableFragmentAnimations = false;

    private FragmentUtils() {
    }

    public static void addNotification(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.animator.slide_left_enter,
                R.animator.slide_left_exit);
        fragmentTransaction.add(CONTAINER_MAIN, fragment, getName(fragment.getClass()));
        fragmentTransaction.addToBackStack(getName(fragment.getClass()));
        fragmentTransaction.commit();

    }

    public static void add(@NonNull FragmentManager fragmentManager,
                           int containerResource,
                           @NonNull Fragment fragment, boolean canBack, View element, boolean animate) {
        addOrReplace(fragmentManager, containerResource, fragment, element, canBack, false, animate);
    }

    public static void replace(@NonNull FragmentManager fragmentManager,
                               int containerResource,
                               @NonNull Fragment fragment,
                               boolean canBack,
                               View element, boolean animate) {
        addOrReplace(fragmentManager, containerResource, fragment, element, canBack, true, animate);
    }

    public static void replaceChild(@NonNull FragmentManager fragmentManager,
                                    int containerResource,
                                    @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      //  fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(containerResource, fragment, getName(fragment.getClass()));
        fragmentTransaction.commit();
    }


    private static void addOrReplace(@NonNull FragmentManager fragmentManager,
                                     int containerResource,
                                     @NonNull Fragment fragment,
                                     View element,
                                     boolean canBack,
                                     boolean isReplace, boolean animate) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (animate)
            setTransitionFragment(element, fragmentTransaction);
        if (isReplace)
            fragmentTransaction.replace(containerResource, fragment, getName(fragment.getClass()));
        else fragmentTransaction.add(containerResource, fragment, getName(fragment.getClass()));
        if (canBack)
            fragmentTransaction.addToBackStack(getName(fragment.getClass()));
        fragmentTransaction.commit();
    }

    private static void setTransitionFragment(View element, FragmentTransaction fragmentTransaction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && element != null) {
            try {
                fragmentTransaction.addSharedElement(element, ViewCompat.getTransitionName(element));
            } catch (Exception e) {
                setAnimationForTransitionFragment(fragmentTransaction);
            }

        } else {
            setAnimationForTransitionFragment(fragmentTransaction);
        }
    }

    private static void setAnimationForTransitionFragment(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.setCustomAnimations(
                R.animator.slide_left_enter,
                R.animator.slide_left_exit,
                R.animator.slide_right_enter,
                R.animator.slide_right_exit);
        //   fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    }

    /**
     * Clear all fragments in back stack
     *
     * @param fragmentManager .
     */
    public static void clearBackStack(@NonNull FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void backToFragment(@NonNull FragmentManager fragmentManager, @NonNull String fragmentName) {
        if (fragmentManager.getBackStackEntryCount() > 1)
            fragmentManager.popBackStack(fragmentName, 0);
    }

    public static void moveBack(@NonNull FragmentManager fragmentManager, Activity activity) {
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 1) {
            fragmentManager.popBackStack();
        } else {
            AppUtils.runOutOfApp(activity);
        }
    }

    public static String getName(@NonNull Class<?> classType) {
        return classType.getCanonicalName();
    }

    public static void remove(FragmentManager manager, String fragmentName) {
        Fragment fragment = manager.findFragmentByTag(fragmentName);
        if (fragment != null)
            manager.beginTransaction().remove(fragment).commit();
    }

    public static <D extends BaseDialogFragment> void showDialog(FragmentManager manager, D dialogFragment) {
        dialogFragment.show(manager, getName(dialogFragment.getClass()));
    }

    public static boolean isFragmentInBackstack(@NonNull FragmentManager fragmentManager, @NonNull Class<?> classType) {
        String fragmentName = getName(classType);
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            if (fragmentName.equalsIgnoreCase(fragmentManager.getBackStackEntryAt(i).getName())) {
                return true;
            }
        }
        return false;
    }

    public static Fragment getFragment(FragmentManager fragmentManager, Class className) {
        return fragmentManager.findFragmentByTag(getName(className));
    }

    public static void setSharedEnterTransition(Fragment fragment){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(TransitionInflater.from(fragment.getContext()).inflateTransition(android.R.transition.move));
        }
    }
}
