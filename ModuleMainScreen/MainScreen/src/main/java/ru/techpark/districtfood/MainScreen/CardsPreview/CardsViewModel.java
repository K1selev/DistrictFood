package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ru.techpark.districtfood.Bookmarks.BookmarksViewModel;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;

public class CardsViewModel extends AndroidViewModel {

    private CardRepo mRepo = new CardRepo(getApplication());
    private RestaurantDao restaurantDao;
    private LiveData<List<Card>> mCards = mRepo.getCards(restaurantDao);


    public CardsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Card>> getCards(RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
        return mCards;
    }

    public void like(Card card) {
        mRepo.like(card);
    }

    public void likeFromBookmarks(Card card, BookmarksViewModel bookmarksViewModel,
                                  RestaurantDao restaurantDao) {
        mRepo.likeFromBookmarks(card, bookmarksViewModel, restaurantDao);
    }

    public void refresh() {
        mRepo.refresh();
    }
}
