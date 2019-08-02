package com.example.futurityfood.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.futurityfood.AppConfigs;


public final class AppLogger {
    private static final int TYPE_DEBUG = 0;
    private static final int TYPE_ERROR = 1;
    private static final int TYPE_VERBOSE = 2;
    private static final int TYPE_INFO = 3;
    private static boolean lifeCycleLogOn = true;

    private AppLogger() {
    }


    public static <T> void error(T message) {
        showLog(TYPE_ERROR, AppConfigs.LOG_TAG, "", message);
    }

    public static <T> void error(Object object, T message) {
        error(object.getClass().getSimpleName(), message);
    }

    public static <T> void error(String prefix, T message) {
        showLog(TYPE_ERROR, AppConfigs.LOG_TAG, prefix, message);
    }

    public static <T> void debug(T message) {
        showLog(TYPE_DEBUG, AppConfigs.LOG_TAG, "", message);
    }

    public static <T> void debug(Object object, T message) {
        debug(object.getClass().getSimpleName(), message);
    }

    public static <T> void debug(String prefix, T message) {
        showLog(TYPE_DEBUG, AppConfigs.LOG_TAG, prefix, message);
    }

    public static <T> void network(Object object, String action, T message) {
        String prefix = "";
        if (object instanceof String) {
            prefix = (String) object;
        } else prefix = object.getClass().getSimpleName();
        showLog(TYPE_DEBUG, "MY_CLIENT", prefix + "(" + action + ")", message);
    }

    public static <T> void lifecycle(Object object, T message) {
        if (AppLogger.lifeCycleLogOn)
            showLog(TYPE_DEBUG, "MY_LIFECYCLE", object.getClass().getSimpleName(), message);
    }

    public static <T> void memory(T message) {
        showLog(TYPE_INFO, "MY_MEMORY", "", message);
    }

    private static <T> void showLog(int type, String tag, String prefix, T message) {
        String prefixString = "";
        if (prefix != null && prefix.length() > 0) prefixString = "[" + prefix + "] ";
        String messageString = getMessage(message);
        switch (type) {
            case TYPE_DEBUG:
                Log.d(tag, prefixString + messageString);
                break;
            case TYPE_ERROR:
                Log.e(tag, prefixString + messageString);
                break;
            case TYPE_VERBOSE:
                Log.e(tag, prefixString + messageString);
                break;
            case TYPE_INFO:
                Log.e(tag, prefixString + messageString);
                break;
            default:
                Log.e(tag, prefixString + messageString);
                break;
        }

    }

    private static <T> String getMessage(T message) {
        if (message == null) return "NULL!!!";
        String messageString = "Unknown!";
        if (message instanceof Exception) {
            messageString = "[" + ((Exception) message).getClass().getSimpleName() + "]"
                    + ((Exception) message).getMessage();
        } else if (message instanceof Throwable) {
            messageString = "[" + ((Throwable) message).getClass().getSimpleName() + "]"
                    + ((Throwable) message).getMessage();
        } else {
            if (message instanceof String
                    || message instanceof Boolean
                    || message instanceof Float
                    || message instanceof Integer
                    || message instanceof Long
                    ) {
                messageString = String.valueOf(message);
            } else if (message instanceof Intent) {
                IntentData intentData = new IntentData();
                intentData.setData((Intent) message);
                messageString = DataFormatUtils.getJsonPretty(intentData);
            } else if (message instanceof Bundle) {
                messageString = ((Bundle) message).toString();
            } else {
                try {
                    messageString = DataFormatUtils.getJsonPretty(message);
                } catch (Exception e) {
                    messageString = message.toString();
                }
            }
        }
        if (messageString == null) return "NULL!!!";
        return messageString;
    }

    public static class IntentData {
        public void setData(Intent intent) {
            setPath(intent.getData());
            Bundle data = intent.getExtras();
            if (data != null) bundleData = data.toString();
        }

        private void setPath(Uri data) {
            if (data != null) dataPath = data.getPath();
        }

        public String dataPath;
        public String bundleData;
    }
}
