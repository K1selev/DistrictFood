package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TagsViewModel extends AndroidViewModel {

    private CardRepo mRepo = new CardRepo(getApplication());
    private LiveData<List<Tags>> mCardsTags = mRepo.getTags();

    public TagsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Tags>> getTags() {
        return mCardsTags;
    }

    public void converterTags(Card card) {
        mRepo.converterTags(card);
    }

}
