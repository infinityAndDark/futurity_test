package com.example.futurityfood.ui.detail.adapter;

import com.example.futurityfood.data.entity.food.FoodTopping;

public class ExtraItem {
    public ExtraItem(FoodTopping topping) {
        this.topping = topping;
    }

    public boolean isCheck;
    public FoodTopping topping;
}
