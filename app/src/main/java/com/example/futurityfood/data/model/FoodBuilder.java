package com.example.futurityfood.data.model;

import com.example.futurityfood.R;
import com.example.futurityfood.data.entity.food.Food;
import com.example.futurityfood.data.entity.food.FoodCategory;
import com.example.futurityfood.data.entity.food.FoodTopping;
import com.example.futurityfood.data.entity.food.FoodType;
import com.example.futurityfood.data.entity.food.PaymentType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class FoodBuilder {
    static int[] dummyImage = {R.drawable.dummy_food, R.drawable.dummy_food_2, R.drawable.dummy_food_3, R.drawable.dummy_food_4, R.drawable.dummy_food_5};

    public static Food build(String name) {
        Food food = new Food();
        food.name = name;
        food.price = new Random().nextInt(10) + 1;
        food.scoreRate = new Random().nextInt(500) / 100.0f;
        food.orderMinTime = new Random().nextInt(85) + 5;
        food.categories = new String[]{FoodCategory.HEALTHY, FoodCategory.VEGETARIAN};
        food.description = "True cereals are the seeds of certain species of grass. Maize, wheat, and rice account for about half of the calories consumed by people every year. Grains can be ground into flour for bread, cake, noodles, and other food products. They can also be boiled or steamed, either whole or ground, and eaten as is";
        food.ingredients = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        food.nutrition = new Food.Nutrition("470", "40g", "25g", "12g");
        food.paymentType = PaymentType.NORMAL;
        food.toppings = new ArrayList<>();
        food.toppings.add(new FoodTopping("Add jumbo shrimp", 3.99));
        food.toppings.add(new FoodTopping("Add cheese", 3.99));
        food.toppings.add(new FoodTopping("Add white anchovies", 3.99));
        food.type = Calendar.getInstance().getTime().getTime() % 2 == 0 ? FoodType.SALAD : FoodType.MAIN_PLATES;
        food.weightInGram = (new Random().nextInt(5) + 1) * 100;
        food.videoUrl = Calendar.getInstance().getTime().getTime() % 2 == 0 ? "" : "http://nguyendangtho.com";
        food.imageResource = dummyImage[new Random().nextInt(4)];
        food.person = 2 + new Random().nextInt(4);
        return food;
    }
}
