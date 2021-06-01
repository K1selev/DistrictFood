package ru.techpark.districtfood.CachingByRoom;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRestaurants(List<Restaurant> restaurantForBookmarks);

    @Query("select * from Restaurant WHERE isLike = 1")
    LiveData<List<Restaurant>> getRestaurant();

    @Query("select * from Restaurant")
    LiveData<List<Restaurant>> getRestaurantAll();

    @Query("select * from Restaurant")
    List<Restaurant> getRestaurantAllWithoutLiveData();

    @Query("select * from Restaurant")
    Cursor getRestaurantsCursor();

    @Query("select * from Restaurant where id = :restaurantId")
    Cursor getRestaurantWithIdCursor(int restaurantId);

    @Query("DELETE FROM Restaurant")
    void deleteAll();
}
