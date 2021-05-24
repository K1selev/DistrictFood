package ru.techpark.districtfood;

import ru.techpark.districtfood.CachingByRoom.Restaurant;

public interface CallBackListener {
    void onCallBack(String ACTION, Restaurant restaurant);
}
