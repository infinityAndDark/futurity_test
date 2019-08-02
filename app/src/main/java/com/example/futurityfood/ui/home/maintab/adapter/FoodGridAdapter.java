package com.example.futurityfood.ui.home.maintab.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.food.Food;
import com.example.futurityfood.ui.base.BaseAdapter;
import com.example.futurityfood.ui.base.BaseItemViewHolder;
import com.example.futurityfood.util.ImageUtils;
import com.example.futurityfood.view.ImageView1div1;
import com.example.futurityfood.view.OnClickListener;

import butterknife.BindView;

public class FoodGridAdapter extends BaseAdapter<Food> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(createView(viewGroup, R.layout.item_main_food_grid));
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

    public class ItemViewHolder extends BaseItemViewHolder<Food> {

        @BindView(R.id.imageContent)
        ImageView imageContent;
        @BindView(R.id.textType)
        TextView textType;
        @BindView(R.id.textName)
        TextView textName;
        @BindView(R.id.textDelivery)
        TextView textDelivery;

        ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView() {
        }

        @Override
        public void bindData(Food data) {
            textName.setText(data.name);
            textType.setText(data.type);
            textDelivery.setText(itemView.getResources().getString(R.string.delivery_grid, String.valueOf(data.price), String.valueOf(data.orderMinTime), data.person));
            ImageUtils.loadImageRoundCorner(imageContent, data.imageResource);
        }
    }
}
