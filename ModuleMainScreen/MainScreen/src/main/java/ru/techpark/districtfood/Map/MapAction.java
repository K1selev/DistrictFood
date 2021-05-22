package ru.techpark.districtfood.Map;

import android.util.Log;
import android.widget.TextView;

import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainScreen.CardsPreview.CardsAdapter;

public class MapAction {

    private String string;

    public void Router(String ACTION, String name) {
        switch (ACTION) {
            case Constants.ACTION_RESTAURANT_AROUND:
                RestaurantAround(Constants.ACTION_RESTAURANT_AROUND);
                break;
            case Constants.ACTION_ROUTE_OF_RESTAURANT:
                RouteOfRestaurant(Constants.ACTION_ROUTE_OF_RESTAURANT, name);
                break;
        }
    }

    public void RestaurantAround(String string) {
        this.string = string;
    }

    public void RouteOfRestaurant(String string, String name) {
        this.string = string + "\n" + name;
    }

    public String action(){

        return this.string;
    }

    public static MapAction sInstance;
    public MapAction() {
    }
    public synchronized static MapAction getInstance(){
        if (sInstance == null) {
            sInstance = new MapAction();
        }
        return sInstance;
    }
}
