package ru.techpark.districtfood.Bookmarks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;
import ru.techpark.districtfood.MainScreen.CardsPreview.Card;

public class BookmarksViewModel extends AndroidViewModel {

    private BookmarksRepo mRepo = new BookmarksRepo();
    private LiveData<List<Restaurant>> mRestaurants = mRepo.getRestaurants();

    public BookmarksViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        return mRestaurants;
    }

    public void like(RestaurantDao restaurantDao, List<Card> cards) {
        mRepo.like(restaurantDao, cards);
    }

}
