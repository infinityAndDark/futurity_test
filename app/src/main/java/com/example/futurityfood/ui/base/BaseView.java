package com.example.futurityfood.ui.base;

import android.content.Context;
import android.widget.EditText;


public interface BaseView {

    void hideKeyboard();

    void showKeyboard(EditText editText);

    boolean isNetworkConnected();

    void restart();

    Context getContext();
}
