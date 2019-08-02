package com.example.futurityfood.data.entity;

import com.example.futurityfood.data.entity.food.Food;

public class FoodSearch {
    public FoodSearch(boolean isVideo, Food food) {
        this.isVideo = isVideo;
        this.food = food;
    }

    public boolean isVideo;
    public Food food;
}
