package com.example.futurityfood.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.example.futurityfood.BuildConfig;

public final class AppUtils {
    private AppUtils() {
    }


    /**
     * Go to home of phone like press home button in navigation bar
     *
     * @param activity what activity want to ...
     */
    public static void goHome(@NonNull Activity activity) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(startMain);
    }

    public static void runOutOfApp(@NonNull Activity activity) {
        activity.moveTaskToBack(true);
    }

    public static String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    public static String getAppVersion(Context context) {
        String version;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = BuildConfig.VERSION_NAME;
        }
        return version;
    }

    public static String getOSName() {
        String osName = "Android";
        try {
            osName += "-" + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT + 1].getName();
        } catch (Exception ignored) {
        }
        return osName;
    }
}
