package com.example.futurityfood.ui.search.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.FoodSearch;
import com.example.futurityfood.ui.base.BaseAdapter;
import com.example.futurityfood.ui.base.BaseItemViewHolder;
import com.example.futurityfood.util.ImageUtils;
import com.example.futurityfood.view.ImageView1div1;
import com.example.futurityfood.view.OnClickListener;

import butterknife.BindView;

public class SearchFoodAdapter extends BaseAdapter<FoodSearch> {
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_VIDEO = 2;
    private final int VIEW_TYPE_HEADER = 3;

    @Override
    public int getItemViewType(int position) {
        FoodSearch item = data.get(position);
        if (item.food == null) return VIEW_TYPE_HEADER;
        else {
            if (item.isVideo) return VIEW_TYPE_VIDEO;
            else return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_HEADER)
            return new HeaderViewHolder(createView(viewGroup, R.layout.view_header_item));
        else if (viewType == VIEW_TYPE_ITEM) {
            return new ItemViewHolder(createView(viewGroup, R.layout.item_search_food));
        } else if (viewType == VIEW_TYPE_VIDEO) {
            return new VideoViewHolder(createView(viewGroup, R.layout.item_search_video));
        }
        return new ItemViewHolder(null);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        BaseItemViewHolder<FoodSearch> holder = (BaseItemViewHolder<FoodSearch>) viewHolder;
        holder.bindData(data.get(i));
        if (holder instanceof ItemViewHolder || holder instanceof VideoViewHolder)
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    if (itemListener != null) itemListener.onClick(i, v, data.get(i));
                }
            });
    }

    class ItemViewHolder extends BaseItemViewHolder<FoodSearch> {

        @BindView(R.id.imageContent)
        ImageView1div1 imageContent;
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
        public void bindData(FoodSearch data) {
            ImageUtils.loadImageRoundCorner(imageContent, data.food.imageResource);
            textName.setText(data.food.name);
            textDelivery.setText(itemView.getResources().getString(R.string.search_delivery,
                    data.food.price, data.food.orderMinTime, data.food.person));
        }
    }

    class VideoViewHolder extends BaseItemViewHolder<FoodSearch> {

        @BindView(R.id.imageContent)
        ImageView1div1 imageContent;
        @BindView(R.id.textName)
        TextView textName;
        @BindView(R.id.textDelivery)
        TextView textDelivery;
        @BindView(R.id.textCategory)
        TextView textCategory;

        VideoViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView() {
        }

        @Override
        public void bindData(FoodSearch data) {
            ImageUtils.loadImageRoundCorner(imageContent, data.food.imageResource);
            textName.setText(data.food.name);
            textDelivery.setText(itemView.getResources().getString(R.string.search_delivery,
                    data.food.price, data.food.orderMinTime, data.food.person));
            textCategory.setText(data.food.getCategoryStr());
        }
    }

    class HeaderViewHolder extends BaseItemViewHolder<FoodSearch> {
        @BindView(R.id.textTitle)
        TextView textTitle;
        HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView() {

        }

        @Override
        public void bindData(FoodSearch data) {
            if (data.isVideo) {
                textTitle.setText("VIDEO RECIPES");
            } else {
                textTitle.setText("RECIPES");
            }
        }
    }
}
