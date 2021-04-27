package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.techpark.districtfood.Network.ApiRepo;

public class TagsRepo {

    private final static MutableLiveData<List<Tags>> mCardsTags = new MutableLiveData<>();

    static {
        mCardsTags.setValue(Collections.emptyList());
    }

    private final Context mContext;
    private final CardApi mCardApi;

    public TagsRepo(Context context) {
        mContext = context;
        mCardApi = ApiRepo.from(mContext).getCardApi();
    }

    //уведомляет наблюдателей когда карточка изменяется

    public LiveData<List<Tags>> getTags() {
        return Transformations.map(mCardsTags, input -> {
            return input;
        });
    }


    //обновление данных карточек
    public void converterTags(final Card card) {
        mCardApi.getTags(card.getId()).enqueue(new Callback<List<CardApi.TagsPlain>>() {
            @Override
            public void onResponse(Call<List<CardApi.TagsPlain>> call,
                                   Response<List<CardApi.TagsPlain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mCardsTags.postValue(transformTags(response.body()));
                }
            }
            @Override
            public void onFailure(Call<List<CardApi.TagsPlain>> call, Throwable t) {
                Log.e("CardRepo", "Failed to load", t);
            }
        });

    }

    //получение и изменение данных карточек
    private static List<Tags> transformTags(List<CardApi.TagsPlain> plains) {
        List<Tags> result = new ArrayList<>();
        for (CardApi.TagsPlain tagsPlain : plains) {
            try {
                Tags tags = mapTags(tagsPlain);
                result.add(tags);
                //Log.e("CardRepo", "Loaded " + card.getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    private static Tags mapTags(CardApi.TagsPlain tagsPlain) throws ParseException {
        return new Tags(
                tagsPlain.tag_fast_food,
                tagsPlain.tag_sale,
                tagsPlain.tag_with_itself
        );
    }

}
