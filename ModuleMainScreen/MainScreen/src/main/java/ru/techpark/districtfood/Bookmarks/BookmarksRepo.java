package ru.techpark.districtfood.Bookmarks;

import android.app.Application;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;
import ru.techpark.districtfood.CachingByRoom.RestaurantsDatabase;
import ru.techpark.districtfood.MainScreen.CardsPreview.Card;

public class BookmarksRepo {

    private final static MutableLiveData<List<Restaurant>> mRestaurants = new MutableLiveData<>();
    static {
        mRestaurants.setValue(Collections.emptyList());
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        RestaurantsDatabase db = ApplicationModified.getInstance().getRestaurantDatabase();
        RestaurantDao restaurantDao = db.getRestaurantDao();
        return restaurantDao.getRestaurant();
    }

    public void like(RestaurantDao restaurantDao, List<Card> cards) {

        if (cards.size() != 0) {
            Thread ThreadForGetRoomDatabase = new Thread(new Runnable() {
                @Override
                public void run() {
                    restaurantDao.deleteAll();
                    restaurantDao.insertRestaurants(completionRestaurant(cards));
                }
            });
            ThreadForGetRoomDatabase.start();
        }
    }

    private List<Restaurant> completionRestaurant(List<Card> cardsRoom) {

        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < cardsRoom.size(); i++){
            final Card cardRoom = cardsRoom.get(i);
            if (cardRoom.getIsLike()) {
                restaurants.add(new Restaurant(cardRoom.getId(), cardRoom.getIsLike(), cardRoom.getMiddle_receipt(),
                        cardRoom.getName(), cardRoom.getScore(), cardRoom.getTagFastFood(),
                        cardRoom.getTagSale(), cardRoom.getTagWithItself()));
            }
        }

        return restaurants;
    }

}
