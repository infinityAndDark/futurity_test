package com.example.futurityfood.view.bottombar;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 07/11/2017.
 */

public class BottomTab extends LinearLayout {

    private List<ItemTab> items;
    private OnTabSelectedListener tabSelectedListener;

    public BottomTab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public BottomTab(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void addTab(ItemTab tab) {
        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(tab);
    }

    public ItemTab get(int i) {
        if (items != null) return items.get(i);
        return null;
    }

    public void setTabSelectedListener(OnTabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }

    private boolean canChangeTab = true;

    public void build() {
        if (items != null && items.size() > 0) {
            deselectAll();

            for (int i = 0; i < items.size(); i++) {
                final ItemTab item = items.get(i);
                final int position = i;

                if (position == 0) {
                    doSelectItem(item);
                }

                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!canChangeTab) return;
                        delayChangeTab();
                        if (tabSelectedListener != null) {
                            deselectAll();
                            doSelectItem(item);

                            TypedValue outValue = new TypedValue();
                            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                            item.setBackgroundResource(outValue.resourceId);

                            tabSelectedListener.onTabSelected(position);
                        }
                    }
                });
                addView(item);
            }
        }
    }

    private void delayChangeTab() {
        if (canChangeTab) {
            canChangeTab = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canChangeTab = true;
                }
            }, 170);
        }
    }

    private void doSelectItem(ItemTab item) {
        item.select();
    }


    private void deselectAll() {
        for (ItemTab tab : this.items) {
            tab.deselect();
        }
    }

    public void selectTab(int newPosition) {
        if (tabSelectedListener != null) {

            if (items != null && items.size() > 0) {
                boolean found = false;

                for (int i = 0; i < items.size(); i++) {
                    if (newPosition == i) {
                        deselectAll();
                        found = true;
                        doSelectItem(items.get(newPosition));
                    }
                }

                if (found) {
                    tabSelectedListener.onTabSelected(newPosition);
                }
            }
        }
    }

    public interface OnTabSelectedListener {

        /**
         * Called when a tab click.
         *
         * @param position The position of the tab that was selected
         */
        void onTabSelected(int position);
    }
}
