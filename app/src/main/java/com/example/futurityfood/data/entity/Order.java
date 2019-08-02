package com.example.futurityfood.data.entity;

import android.support.annotation.NonNull;

import com.example.futurityfood.data.entity.food.Food;
import com.example.futurityfood.data.entity.food.FoodTopping;

import java.util.List;

public class Order {
    public Order(@NonNull List<FoodTopping> toppings, @NonNull Food food, int quantity) {
        this.toppings = toppings;
        this.food = food;
        this.quantity = quantity;
    }

    public List<FoodTopping> toppings;
    public Food food;
    public int quantity;

    private double getToppingsPrice() {
        double result = 0;
        for (FoodTopping topping : toppings) result += topping.price;
        return result;
    }

    public double getTotalPrice() {
        return quantity * (food.price + getToppingsPrice());
    }
}
