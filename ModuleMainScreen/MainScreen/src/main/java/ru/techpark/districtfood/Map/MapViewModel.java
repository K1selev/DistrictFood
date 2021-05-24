package ru.techpark.districtfood.Map;

import android.app.Notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Map Fragment");
    }

    public LiveData<String> getSomething() {
        return mText;
    }

}
