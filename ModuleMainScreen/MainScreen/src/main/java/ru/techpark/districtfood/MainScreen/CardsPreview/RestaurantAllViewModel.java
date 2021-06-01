package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;

public class RestaurantAllViewModel extends AndroidViewModel {

    private RestaurantAllRepo mRepo = new RestaurantAllRepo();
    private LiveData<List<Restaurant>> mRestaurants = mRepo.getRestaurantsAll();

    public RestaurantAllViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Restaurant>> getRestaurantsAll() {
        return mRestaurants;
    }

    public void like(RestaurantDao restaurantDao, List<Card> cards, Restaurant restaurant) {
        mRepo.like(restaurantDao, cards, restaurant);
    }

    public void feedbacks(RestaurantDao restaurantDao, List<Card> cards, Restaurant restaurant, String feedback){
        mRepo.feedbacks(restaurantDao, cards, restaurant, feedback);
    }

}
