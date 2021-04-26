package ru.techpark.districtfood.MainScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainScreenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MainScreenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MainScreen fragment");
    }

    public LiveData<String> getSomething() {
        return mText;
    }

}
