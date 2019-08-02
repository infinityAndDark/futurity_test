package com.example.futurityfood.ui.home.ordertab;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.ui.base.BaseFragment;
import com.example.futurityfood.util.ScreenUtils;

import butterknife.BindView;

public class OrderFragment extends BaseFragment {
    @BindView(R.id.statusPlaceholder)
    View statusPlaceholder;
    @BindView(R.id.textHeader)
    TextView textHeader;
    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void setupViews(@NonNull View view) {
        statusPlaceholder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.getStatusBarHeight(myActivity())));
        textHeader.setText(getString(R.string.tab_order));
    }

    @Override
    protected void beginFlow(@NonNull View view) {

    }
}
