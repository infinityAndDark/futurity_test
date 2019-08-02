package com.example.futurityfood.data.entity.food;

import android.support.annotation.NonNull;

import com.example.futurityfood.util.StringUtil;

import java.util.List;

public class Food {
    public String name;
    public String description;
    public int weightInGram;
    public String[] ingredients;
    public Nutrition nutrition;
    public String[] categories;
    public String type;
    public double scoreRate;
    public String paymentType;
    public int orderMinTime;
    public double price;
    public List<FoodTopping> toppings;
    public String videoUrl;
    public int imageResource;
    public int person;

    public boolean hasVideo() {
        return videoUrl != null && videoUrl.length() > 0;
    }

    public boolean containCategory(String category) {
        for (String _category : categories) {
            if (_category.toLowerCase().contains(category.trim().toLowerCase()))
                return true;
        }
        return false;
    }

    public String getIngedientsStr() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String ingredient : ingredients) stringBuilder.append(ingredient + ", ");
        stringBuilder.append("...");
        return stringBuilder.toString();
    }
    public String getCategoryStr() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String category : categories) stringBuilder.append(category + ", ");
        String str=stringBuilder.toString().trim();
        return str.substring(0,str.length()-1);
    }

    public boolean nameMatchKey(@NonNull String key) {
        String _name = StringUtil.removeAccent(name.toLowerCase()).trim();
        String _key = StringUtil.removeAccent(key.toLowerCase()).trim();
        return _name.contains(_key) || _key.contains(_name);
    }

    public static class Nutrition {
        public Nutrition(String calories, String protein, String totalFat, String totalCarbs) {
            this.calories = calories;
            this.protein = protein;
            this.totalFat = totalFat;
            this.totalCarbs = totalCarbs;
        }

        public String calories;
        public String protein;
        public String totalFat;
        public String totalCarbs;
    }
}
