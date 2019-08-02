package com.example.futurityfood.util;

import android.os.Bundle;

import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * Created by chu.thi.ngoc.huyen on 2/7/2018.
 */

public class DataFormatUtils {
    private DataFormatUtils() {
    }

    public static String getJsonPretty(Object data) {
        try {
            return new GsonBuilder().setPrettyPrinting().create().toJson(data);
        } catch (Exception ignored) {

        }
        return null;
    }

    public static Bundle mapToBundle(Map<String, String> map) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        return bundle;
    }
}
