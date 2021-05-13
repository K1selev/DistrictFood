package ru.techpark.districtfood.CachingByRoom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Restaurant.class}, version = 1)
public abstract class RestaurantsDatabase extends RoomDatabase {

    public abstract RestaurantDao getRestaurantDao();

}
