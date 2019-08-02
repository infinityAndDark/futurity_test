package com.example.futurityfood.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseItemViewHolder<DT> extends RecyclerView.ViewHolder {

    public BaseItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupView();
    }

    public abstract void setupView();

    public abstract void bindData(DT data);
}
