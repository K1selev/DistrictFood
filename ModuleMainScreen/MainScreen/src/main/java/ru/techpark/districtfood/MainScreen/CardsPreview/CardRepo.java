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

public class CardRepo {

    private final static MutableLiveData<List<Card>> mCards = new MutableLiveData<>();
    private final static MutableLiveData<List<Tags>> mCardsTags = new MutableLiveData<>();
    
    static {
        mCards.setValue(Collections.emptyList());
        mCardsTags.setValue(Collections.emptyList());
    }

    private final Context mContext;
    private final CardApi mCardApi;

    public CardRepo(Context context) {
        mContext = context;
        mCardApi = ApiRepo.from(mContext).getCardApi();
    }

    //уведомляет наблюдателей когда карточка изменяется
    public LiveData<List<Card>> getCards() {
        return Transformations.map(mCards, input -> {

            return input;
        });
    }

    public LiveData<List<Tags>> getTags() {
        return Transformations.map(mCardsTags, input -> {
            return input;
        });
    }

    //обработчик нажатия на лайк
    public void like(final Card card) {
        mCardApi.like(card.getId(), new CardApi.Like(!card.getIsLike())).enqueue(new Callback<CardApi.Like>() {
            @Override
            public void onResponse(Call<CardApi.Like> call,
                                   Response<CardApi.Like> response) {
                if (response.isSuccessful()) {
                    refresh();
                    // todo refreshSingle() in production :)
                }
            }

            @Override
            public void onFailure(Call<CardApi.Like> call, Throwable t) {
                Log.d("Test", "Failed to like " + card.getName(), t);
                t.printStackTrace();
            }
        });
    }

    //обновление данных карточек
    public void refresh() {
        mCardApi.getAll().enqueue(new Callback<List<CardApi.CardPlain>>() {
            @Override
            public void onResponse(Call<List<CardApi.CardPlain>> call,
                                   Response<List<CardApi.CardPlain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mCards.postValue(transform(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<CardApi.CardPlain>> call, Throwable t) {
                Log.e("CardRepo", "Failed to load", t);
            }
        });
    }

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
    private static List<Card> transform(List<CardApi.CardPlain> plains) {
        List<Card> result = new ArrayList<>();
        for (CardApi.CardPlain cardPlain : plains) {
            try {
                Card card = map(cardPlain);
                result.add(card);
                Log.e("CardRepo", "Loaded " + card.getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

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

    private static Card map(CardApi.CardPlain cardPlain) throws ParseException {
        return new Card(
                cardPlain.id,
                cardPlain.like,
                cardPlain.middle_receipt,
                cardPlain.name,
                cardPlain.score,
                cardPlain.tags
        );
    }

    private static Tags mapTags(CardApi.TagsPlain tagsPlain) throws ParseException {
        return new Tags(
                tagsPlain.tag_fast_food,
                tagsPlain.tag_sale,
                tagsPlain.tag_with_itself
        );
    }
}
