package com.example.futurityfood.ui.home.maintab.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.futurityfood.R;
import com.example.futurityfood.data.model.FoodModel;
import com.example.futurityfood.ui.base.BaseDialogFragment;
import com.example.futurityfood.ui.home.maintab.category.adapter.CategoryAdapter;
import com.example.futurityfood.view.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

public class CategoryDialogFragment extends BaseDialogFragment {
    FoodModel model = new FoodModel();
    @BindView(R.id.buttonCancel)
    View buttonCancel;
    @BindView(R.id.recyclerFood)
    RecyclerView recyclerFood;
    CategoryAdapter adapter = new CategoryAdapter();

    public static CategoryDialogFragment newInstance(BaseDialogFragment.DismissListener dismissListener) {
        CategoryDialogFragment fragment = new CategoryDialogFragment();
        fragment.setDismissListener(dismissListener);
        return fragment;
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_dialog_category;
    }

    @Override
    protected void setupViews(@NonNull View view) {
        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                hide(DISMISS_TYPE_CANCEL);
            }
        });
        adapter.setItemListener((position, itemView, item) -> {
            setDataBack(item);
            hide(DISMISS_TYPE_ACTION_DONE);
        });
        recyclerFood.setLayoutManager(new LinearLayoutManager(recyclerFood.getContext()));
        recyclerFood.setAdapter(adapter);
        adapter.setData(new ArrayList<>(Arrays.asList(model.getCategories())));
    }

    @Override
    protected void beginFlow(@NonNull View view) {

    }
}
