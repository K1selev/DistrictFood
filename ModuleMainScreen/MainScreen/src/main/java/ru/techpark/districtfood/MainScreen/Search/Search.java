package ru.techpark.districtfood.MainScreen.Search;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.CachingByRoom.Restaurant;

public class Search{

    private List<Restaurant> restaurants;

    public Search() { }

    public List<Restaurant> search(String name_restaurant){

        List<Restaurant> newRestaurants = new ArrayList<>();
        if (!name_restaurant.equals("")) {
            for (Restaurant restaurant : restaurants) {
                if (restaurant.getName().toLowerCase().contains(name_restaurant.toLowerCase())) {
                    newRestaurants.add(restaurant);
                }
            }
        } else newRestaurants = restaurants;

        return newRestaurants;


    }

    public static Search sInstance;

    public synchronized static Search getInstance(){
        if (sInstance == null) {
            sInstance = new Search();
        }
        return sInstance;
    }

    public void SetRestaurants(List<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

}
