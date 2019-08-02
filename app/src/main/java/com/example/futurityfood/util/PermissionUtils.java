package com.example.futurityfood.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

public class PermissionUtils {
    private PermissionUtils() {
    }

    /**
     * @param activity    .
     * @param permission  .
     * @param requestCode .
     * @return true if granted
     */
    public static boolean requestPermission(final Activity activity, String permission, int requestCode) {
        return requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public static boolean requestPermissions(final Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermissionGranted(activity, permissions)) {
                if (shouldShowPermissionSetting(activity, permissions)) {
                    // openSettingDialog(activity);
                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                }
                return false;
            }
        }
        return true;
    }

    private static boolean checkPermissionGranted(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private static boolean shouldShowPermissionSetting(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return true;
        }
        return false;
    }

    private static void openSettingDialog(final Activity activity) {
        if (activity == null) return;
        goToAppSetting(activity);
    }

    private static void goToAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public static boolean isGranted(int[] grantResults) {
        return grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
