package com.example.futurityfood.ui.detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.food.FoodTopping;
import com.example.futurityfood.ui.base.BaseAdapter;
import com.example.futurityfood.ui.base.BaseItemViewHolder;
import com.example.futurityfood.view.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ExtrasAdapter extends BaseAdapter<ExtraItem> {
    public List<ExtraItem> toppingToExtras(List<FoodTopping> toppingList) {
        List<ExtraItem> result = new ArrayList<>();
        for (FoodTopping topping : toppingList) result.add(new ExtraItem(topping));
        return result;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(createView(viewGroup, R.layout.item_food_detail_extra));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.bindData(data.get(i));
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                data.get(i).isCheck = !data.get(i).isCheck;
                notifyDataSetChanged();
            }
        });
    }

    public class ItemViewHolder extends BaseItemViewHolder<ExtraItem> {


        @BindView(R.id.textTitle)
        TextView textTitle;
        @BindView(R.id.textPrice)
        TextView textPrice;
        @BindView(R.id.imageCheck)
        public ImageView imageCheck;

        ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView() {
        }

        @Override
        public void bindData(ExtraItem data) {
            textTitle.setText(data.topping.name);
            textPrice.setText(itemView.getResources().getString(R.string.extra_price, data.topping.price).replace(",","."));
            imageCheck.setVisibility(data.isCheck ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
