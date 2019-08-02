package com.example.futurityfood.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<DT> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<DT> data = new ArrayList<>();
    protected ItemListener<DT> itemListener;

    public synchronized void addData(List<DT> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }

    public List<DT> getData() {
        return data;
    }

    public synchronized void setData(List<DT> items) {
        clearData();
        addData(items);
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }


    protected View createView(ViewGroup parent, int layoutResource) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItemListener(ItemListener<DT> itemListener) {
        this.itemListener = itemListener;
    }

    public interface ItemListener<DT> {
        void onClick(int position, View itemView, DT item);
    }
}
