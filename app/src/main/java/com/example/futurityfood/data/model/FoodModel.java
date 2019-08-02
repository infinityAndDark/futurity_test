package com.example.futurityfood.data.model;


import android.support.annotation.NonNull;

import com.example.futurityfood.data.entity.FoodSearch;
import com.example.futurityfood.data.entity.food.Food;
import com.example.futurityfood.data.entity.food.FoodCategory;

import java.util.ArrayList;
import java.util.List;

public class FoodModel {

    List<Food> foods = new ArrayList<>();

    public List<Food> getListByCategory(@NonNull String category) {
        List<Food> result = new ArrayList<>();
        if (foods.isEmpty()) getFoods();
        for (Food food : foods) {
            if (food.containCategory(category))
                result.add(food);
        }

        return result;
    }

    public List<FoodSearch> searchByKey(@NonNull String key) {
        List<FoodSearch> result = new ArrayList<>();
        List<FoodSearch> items = new ArrayList<>();
        List<FoodSearch> videos = new ArrayList<>();

        if (foods.isEmpty()) getFoods();
        for (Food food : foods) {
            if (food.nameMatchKey(key)) {
                if (food.hasVideo()) videos.add(new FoodSearch(true, food));
                else items.add(new FoodSearch(false, food));
            }

        }
        if (!items.isEmpty()) {
            items.add(0, new FoodSearch(false, null));
            result.addAll(items);
        }
        if (!videos.isEmpty()) {
            videos.add(0, new FoodSearch(true, null));
            result.addAll(videos);
        }
        return result;
    }


    public String[] getCategories() {
        return new String[]{FoodCategory.VEGETARIAN, FoodCategory.HEALTHY};
    }

    private void getFoods() {
        foods.clear();
        foods.add(FoodBuilder.build("Ackee and saltfish"));
        foods.add(FoodBuilder.build("Bacalaíto"));
        foods.add(FoodBuilder.build("Bacalhau à Brás"));
        foods.add(FoodBuilder.build("Crappit heid"));
        foods.add(FoodBuilder.build("Cullen skink"));
        foods.add(FoodBuilder.build("Fish and brewis"));
        foods.add(FoodBuilder.build("Fish ball"));
        foods.add(FoodBuilder.build("Fishcake"));
        foods.add(FoodBuilder.build("Fish finger"));
        foods.add(FoodBuilder.build("Fried fish"));
        foods.add(FoodBuilder.build("Pescado frito"));
        foods.add(FoodBuilder.build("Taramosalata"));
        foods.add(FoodBuilder.build("Evaporated milk"));
        foods.add(FoodBuilder.build("Instant soup"));
        foods.add(FoodBuilder.build("Apple chips"));
        foods.add(FoodBuilder.build("Dried apricot"));
        foods.add(FoodBuilder.build("Black lime"));
    }
}
