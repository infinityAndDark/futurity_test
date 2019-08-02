package com.example.futurityfood.ui.home.maintab.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.food.Food;
import com.example.futurityfood.ui.base.BaseAdapter;
import com.example.futurityfood.ui.base.BaseItemViewHolder;
import com.example.futurityfood.util.ImageUtils;
import com.example.futurityfood.view.ImageView1div1;
import com.example.futurityfood.view.OnClickListener;

import butterknife.BindView;

public class FoodListAdapter extends BaseAdapter<Food> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(createView(viewGroup, R.layout.item_main_food_list));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.bindData(data.get(i));
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                if (itemListener != null) itemListener.onClick(i, v, data.get(i));
            }
        });
    }

    class ItemViewHolder extends BaseItemViewHolder<Food> {

        @BindView(R.id.imageContent)
        ImageView1div1 imageContent;
        @BindView(R.id.textTitle)
        TextView textTitle;
        @BindView(R.id.textCategory)
        TextView textCategory;
        @BindView(R.id.textDelivery)
        TextView textDelivery;
        @BindView(R.id.textScoreRate)
        TextView textScoreRate;

        ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView() {
        }

        @Override
        public void bindData(Food data) {
            textTitle.setText(data.name);
            textCategory.setText(data.getCategoryStr());
            textScoreRate.setText(itemView.getResources().getString(R.string.score_rate,data.scoreRate).replace(",","."));
            textDelivery.setText(itemView.getResources().getString(R.string.delivery_item, String.valueOf(data.price), String.valueOf(data.orderMinTime)));
            ImageUtils.loadImageRoundCorner(imageContent, data.imageResource);
        }
    }
}
