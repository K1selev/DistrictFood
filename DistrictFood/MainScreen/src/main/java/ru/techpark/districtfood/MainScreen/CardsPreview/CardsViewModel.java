package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CardsViewModel extends AndroidViewModel {

    private CardRepo mRepo = new CardRepo(getApplication());
    private LiveData<List<Card>> mCards = mRepo.getCards();

    public CardsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Card>> getCards() {
        return mCards;
    }

    public void like(Card card) {
        mRepo.like(card);
    }

    public void getTags(Card card){
        mRepo.converterTags(card);
    }

    public void refresh() {
        mRepo.refresh();
    }
}
