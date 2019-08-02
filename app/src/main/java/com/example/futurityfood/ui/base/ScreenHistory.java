package com.example.futurityfood.ui.base;

import java.util.ArrayList;
import java.util.List;

public class ScreenHistory {
    public static class History {
        public History(String name, boolean statusBarLight) {
            this.name = name;
            this.statusBarLight = statusBarLight;
        }

        public String name;
        public boolean statusBarLight;
    }

    private static History previousScreenHistory = null;
    private static List<History> historyList = new ArrayList<>();

    public static void addHistory(Info info) {
        if (historyList.size() > 0) previousScreenHistory = historyList.get(historyList.size() - 1);
        historyList.add(new History(info.getScreenName(), info.isStatusBarLight()));

    }

    public static void updateHistory(Info info) {
        for (int i = historyList.size() - 1; i >= 0; i--) {
            if (info.getScreenName().equals(historyList.get(i).name))
                historyList.get(i).statusBarLight = info.isStatusBarLight();
        }
    }

    public static void clearHistory() {
        historyList.clear();
        previousScreenHistory = null;
    }

    public static void popHistoryTo(String screenName) {
        for (int i = historyList.size() - 1; i >= 0; i--) {
            if (!historyList.get(i).name.equals(screenName)) historyList.remove(i);
            else {
                previousScreenHistory = historyList.get(i);
                break;
            }
        }
    }

    public static void popHistory() {

        if (historyList.size() > 1) {
            previousScreenHistory = historyList.get(historyList.size() - 2);
            historyList.remove(historyList.size() - 1);
        }
    }

    public static History getPreviousScreen() {
        return previousScreenHistory;
    }

    public interface Info {
        String getScreenName();

        boolean isStatusBarLight();

    }
}
