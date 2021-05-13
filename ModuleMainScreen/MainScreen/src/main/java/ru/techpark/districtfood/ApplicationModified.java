package ru.techpark.districtfood;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import ru.techpark.districtfood.Network.ApiRepo;
import ru.techpark.districtfood.CachingByRoom.RestaurantsDatabase;

public class ApplicationModified extends Application {

    private ApiRepo mApiRepo;
    private RestaurantsDatabase mRestaurantDatabase;

    public static ApplicationModified Instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        mApiRepo = new ApiRepo();
        mRestaurantDatabase = Room.databaseBuilder(Instance, RestaurantsDatabase.class, "tabs_of_restaurants")
                .build();
    }

    public RestaurantsDatabase getRestaurantDatabase() {
        return mRestaurantDatabase;
    }

    public ApiRepo getApis() {
        return mApiRepo;
    }

    public static ApplicationModified from(Context context) {
        return (ApplicationModified) context.getApplicationContext();
    }

    public static ApplicationModified getInstance(){
        return Instance;
    }
}
