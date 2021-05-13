package ru.techpark.districtfood.CachingByRoom;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRestaurants(List<Restaurant> restaurants);

    @Query("select * from restaurant")
    LiveData<List<Restaurant>> getRestaurant();

    @Query("select * from restaurant")
    List<Restaurant> getRestaurant1();

    @Query("select * from restaurant")
    Cursor getRestaurantsCursor();

    @Query("select * from restaurant where id = :restaurantId")
    Cursor getRestaurantWithIdCursor(int restaurantId);

    @Query("DELETE FROM restaurant")
    void deleteAll();
}
