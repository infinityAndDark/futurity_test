package com.example.futurityfood.ui.search;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.FoodSearch;
import com.example.futurityfood.data.model.FoodModel;
import com.example.futurityfood.ui.base.BaseAdapter;
import com.example.futurityfood.ui.base.BaseFragment;
import com.example.futurityfood.ui.detail.FoodDetailFragment;
import com.example.futurityfood.ui.search.adapter.SearchFoodAdapter;
import com.example.futurityfood.util.ScreenUtils;
import com.example.futurityfood.view.OnClickListener;
import com.example.futurityfood.view.TextChangedListener;

import butterknife.BindView;

public class SearchFoodFragment extends BaseFragment {
    @BindView(R.id.statusPlaceholder)
    View statusPlaceholder;
    @BindView(R.id.editSearch)
    EditText editSearch;
    @BindView(R.id.buttonClear)
    ImageView buttonClear;
    @BindView(R.id.recyclerFood)
    RecyclerView recyclerFood;

    SearchFoodAdapter foodAdapter;
    FoodModel model = new FoodModel();

    private int DELAYED_TIME = 500;
    private String currentKey = "";
    Handler searchHandler = new Handler();
    Runnable searchRunnable;

    public static SearchFoodFragment newInstance() {
        return new SearchFoodFragment();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void setupViews(@NonNull View view) {
        statusPlaceholder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.getStatusBarHeight(myActivity())));
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                editSearch.setText("");
                showKeyboard(editSearch);
            }
        });
        editSearch.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onTextChanged(String textChanged) {
                if (textChanged.trim().length() == 0) buttonClear.setVisibility(View.INVISIBLE);
                else buttonClear.setVisibility(View.VISIBLE);
                currentKey = textChanged;
                if (currentKey.trim().length() == 0) {
                    if (foodAdapter != null) foodAdapter.clearData();
                } else
                    doSearch();
            }
        });
        setupList();
    }

    private void doSearch() {
        if (searchRunnable != null)
            searchHandler.removeCallbacks(searchRunnable);
        searchRunnable = () -> {
            if (currentKey.trim().length() > 0) {
                if (foodAdapter != null) foodAdapter.setData(model.searchByKey(currentKey));
            }
        };
        searchHandler.postDelayed(searchRunnable, DELAYED_TIME);
    }


    private void setupList() {
        foodAdapter = new SearchFoodAdapter();
        foodAdapter.setItemListener((position, itemView, item) -> goToScreen(FoodDetailFragment.newInstance(item.food)));
        recyclerFood.setLayoutManager(new LinearLayoutManager(recyclerFood.getContext()));
        recyclerFood.setAdapter(foodAdapter);
        recyclerFood.setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });
    }

    @Override
    protected void beginFlow(@NonNull View view) {
        showKeyboard(editSearch);
    }
}
