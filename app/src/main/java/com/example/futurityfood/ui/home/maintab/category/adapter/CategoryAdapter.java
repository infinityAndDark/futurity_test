package com.example.futurityfood.ui.home.maintab.category.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.ui.base.BaseAdapter;
import com.example.futurityfood.ui.base.BaseItemViewHolder;
import com.example.futurityfood.view.OnClickListener;

import butterknife.BindView;

public class CategoryAdapter extends BaseAdapter<String> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(createView(viewGroup, R.layout.item_food_category));
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

    public class ItemViewHolder extends BaseItemViewHolder<String> {

        @BindView(R.id.textTitle)
        TextView textTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView() {
        }

        @Override
        public void bindData(String data) {
            textTitle.setText(data);
        }
    }
}
