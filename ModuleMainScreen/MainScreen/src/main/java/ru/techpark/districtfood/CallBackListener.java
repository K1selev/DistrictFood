package ru.techpark.districtfood;

import android.os.Bundle;

import ru.techpark.districtfood.CachingByRoom.Restaurant;

public interface CallBackListener{
    void onCallBack(String ACTION, Restaurant restaurant);
    void onCallBack(String ACTION, Bundle bundle);

}
