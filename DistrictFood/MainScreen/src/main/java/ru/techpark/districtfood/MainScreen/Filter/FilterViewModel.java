package ru.techpark.districtfood.MainScreen.Filter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class FilterViewModel extends AndroidViewModel {

    /*private FilterRepo mRepo = new FilterRepo(getApplication());
    private LiveData<List<Filter>> mFilters = mRepo.getFilter();*/

    public FilterViewModel(@NonNull Application application) {
        super(application);
    }



    /*public LiveData<List<Filter>> getLessons() {
        return mFilters;
    }

    public void like(Filter filter) {
        mRepo.like(filter);
    }

    public void refresh() {
        mRepo.refresh();
    }*/

}
