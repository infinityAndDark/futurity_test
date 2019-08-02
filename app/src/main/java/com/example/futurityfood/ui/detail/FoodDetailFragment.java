package com.example.futurityfood.ui.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.food.Food;
import com.example.futurityfood.ui.base.BaseFragment;
import com.example.futurityfood.ui.detail.adapter.ExtrasAdapter;
import com.example.futurityfood.util.ImageUtils;
import com.example.futurityfood.util.ScreenUtils;
import com.example.futurityfood.view.OnClickListener;

import butterknife.BindView;

public class FoodDetailFragment extends BaseFragment {
    @BindView(R.id.statusPlaceholder)
    View statusPlaceholder;
    @BindView(R.id.imageContent)
    ImageView imageContent;
    @BindView(R.id.buttonBack)
    View buttonBack;
    @BindView(R.id.recyclerExtras)
    RecyclerView recyclerExtras;
    ExtrasAdapter extrasAdapter;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textPrice)
    TextView textPrice;
    @BindView(R.id.textWeight)
    TextView textWeight;
    @BindView(R.id.textDescription)
    TextView textDescription;
    @BindView(R.id.textIngredient)
    TextView textIngredient;
    @BindView(R.id.textCalories)
    TextView textCalories;
    @BindView(R.id.textProtein)
    TextView textProtein;
    @BindView(R.id.textTotalFat)
    TextView textTotalFat;
    @BindView(R.id.textCarbs)
    TextView textCarbs;

    private Food data;

    public static FoodDetailFragment newInstance(Food data) {
        return new FoodDetailFragment().setData(data);
    }

    private FoodDetailFragment setData(Food data) {
        this.data = data;
        return this;
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_food_detail;
    }

    @Override
    protected void setupViews(@NonNull View view) {
        statusPlaceholder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.getStatusBarHeight(myActivity())));
        buttonBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                moveBack();
            }
        });
        setupExtras();
    }

    private void setupExtras() {
        recyclerExtras.setLayoutManager(new LinearLayoutManager(recyclerExtras.getContext()));
        extrasAdapter = new ExtrasAdapter();
        recyclerExtras.setAdapter(extrasAdapter);

    }

    @Override
    protected void beginFlow(@NonNull View view) {
        if (data == null) return;
        ImageUtils.loadImage(imageContent, data.imageResource);
        textName.setText(data.name);
        textPrice.setText(getString(R.string.detail_price, data.price));
        textWeight.setText(getString(R.string.detail_weight, data.weightInGram));
        textDescription.setText(data.description);
        textIngredient.setText(data.getIngedientsStr());
        textCalories.setText(data.nutrition.calories);
        textProtein.setText(data.nutrition.protein);
        textTotalFat.setText(data.nutrition.totalFat);
        textCarbs.setText(data.nutrition.totalCarbs);
        extrasAdapter.setData(extrasAdapter.toppingToExtras(data.toppings));
    }
}
