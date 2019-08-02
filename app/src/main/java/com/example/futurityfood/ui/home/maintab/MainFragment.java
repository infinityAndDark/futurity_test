package com.example.futurityfood.ui.home.maintab;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.food.Food;
import com.example.futurityfood.data.entity.food.FoodCategory;
import com.example.futurityfood.data.model.FoodModel;
import com.example.futurityfood.ui.base.BaseDialogFragment;
import com.example.futurityfood.ui.base.BaseFragment;
import com.example.futurityfood.ui.detail.FoodDetailFragment;
import com.example.futurityfood.ui.home.maintab.adapter.FoodGridAdapter;
import com.example.futurityfood.ui.home.maintab.adapter.FoodListAdapter;
import com.example.futurityfood.ui.home.maintab.adapter.SpacesItemDecoration;
import com.example.futurityfood.ui.home.maintab.category.CategoryDialogFragment;
import com.example.futurityfood.ui.search.SearchFoodFragment;
import com.example.futurityfood.util.ScreenUtils;
import com.example.futurityfood.view.OnClickListener;

import butterknife.BindView;

public class MainFragment extends BaseFragment {
    @BindView(R.id.statusPlaceholder)
    View statusPlaceholder;
    @BindView(R.id.textCategory)
    TextView textCategory;
    @BindView(R.id.viewCategory)
    View viewCategory;
    @BindView(R.id.buttonSearch)
    View buttonSearch;
    @BindView(R.id.buttonViewType)
    View buttonViewType;
    @BindView(R.id.recyclerFood)
    RecyclerView recyclerFood;
    private boolean isGrid = false;

    FoodModel model = new FoodModel();
    FoodListAdapter listAdapter;
    FoodGridAdapter gridAdapter;
    String currentCategory = FoodCategory.VEGETARIAN;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void setupViews(@NonNull View view) {
        statusPlaceholder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.getStatusBarHeight(myActivity())));
        textCategory.setText(getString(R.string.tab_main));

    }

    @Override
    protected void beginFlow(@NonNull View view) {
        textCategory.setText(model.getCategories()[0]);
        buttonSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                goToScreen(SearchFoodFragment.newInstance());
            }
        });
        buttonViewType.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                changeType((ImageView) v);
            }
        });
        viewCategory.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                showDialog(CategoryDialogFragment.newInstance((dismissType, data) -> {
                    if (dismissType == BaseDialogFragment.DISMISS_TYPE_ACTION_DONE) {
                        changeCategory((String) data);
                    }
                }));
            }
        });
        setAdapter(isGrid);
    }

    private void changeType(ImageView button) {
        isGrid = !isGrid;
        if (isGrid) {
            button.setImageResource(R.drawable.ic_grid);
        } else {
            button.setImageResource(R.drawable.ic_list);
        }
        setAdapter(isGrid);
    }

    private void doOnCLickItem(Food item) {
        goToScreen(FoodDetailFragment.newInstance(item));
    }

    private void setAdapter(boolean isGrid) {
        if (isGrid) {
            if (gridAdapter == null) {
                gridAdapter = new FoodGridAdapter();
                gridAdapter.setData(model.getListByCategory(currentCategory));
                gridAdapter.setItemListener((position, itemView, item) -> doOnCLickItem(item));
            }
            recyclerFood.setLayoutManager(new GridLayoutManager(recyclerFood.getContext(), 2));
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            recyclerFood.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerFood.getLayoutParams();
            params.setMargins(0, 0, spacingInPixels, 0);
            recyclerFood.setLayoutParams(params);
            recyclerFood.setAdapter(gridAdapter);
        } else {
            if (listAdapter == null) {
                listAdapter = new FoodListAdapter();
                listAdapter.setData(model.getListByCategory(currentCategory));
                listAdapter.setItemListener((position, itemView, item) -> doOnCLickItem(item));
            }
            recyclerFood.setLayoutManager(new LinearLayoutManager(recyclerFood.getContext()));
            try {
                recyclerFood.removeItemDecorationAt(0);
            } catch (Exception e) {

            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerFood.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            recyclerFood.setLayoutParams(params);
            recyclerFood.setAdapter(listAdapter);
        }
    }

    private void changeCategory(String category) {
        if (currentCategory.equals(category)) return;
        currentCategory = category;
        textCategory.setText(currentCategory);
        if (gridAdapter != null)
            gridAdapter.setData(model.getListByCategory(currentCategory));
        if (listAdapter != null)
            listAdapter.setData(model.getListByCategory(currentCategory));
    }
}
