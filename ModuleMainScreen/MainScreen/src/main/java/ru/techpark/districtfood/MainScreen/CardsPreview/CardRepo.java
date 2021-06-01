package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
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
    
    static {
        mCards.setValue(Collections.emptyList());
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

    public void feedbacks(final Card card, String feedback) {

        mCardApi.z_feedbacks(card.getId(), new CardApi.Feedbacks(feedback)).enqueue(new Callback<CardApi.Feedbacks>() {
            @Override
            public void onResponse(@NonNull Call<CardApi.Feedbacks> call,
                                   @NonNull Response<CardApi.Feedbacks> response) {
                if (response.isSuccessful()) {
                    //score(card, newFeedbacks);
                    refresh();
                    // todo refreshSingle() in production :)
                }
            }

            @Override
            public void onFailure(@NonNull Call<CardApi.Feedbacks> call, @NonNull Throwable t) {
                Log.d("test", "Failed to like " + card.getName(), t);
                t.printStackTrace();
            }
        });
    }

    public void score(final Card card, String feedbacks) {

        float newScore = ConvertScoreCard(feedbacks);

        mCardApi.score(card.getId(), new CardApi.Score(newScore)).enqueue(new Callback<CardApi.Score>() {
            @Override
            public void onResponse(@NonNull Call<CardApi.Score> call,
                                   @NonNull Response<CardApi.Score> response) {
                if (response.isSuccessful()) {
                    refresh();
                    // todo refreshSingle() in production :)
                }
            }

            @Override
            public void onFailure(@NonNull Call<CardApi.Score> call, @NonNull Throwable t) {
                Log.d("test", "Failed to like " + card.getName(), t);
                t.printStackTrace();
            }
        });
    }

    private float ConvertScoreCard(String feedbacks){

        float value = 0;
        float newScoreAll = 0;
        float newScore = 0;
        String[] strings = feedbacks.split("</1337/>");

        for (String string : strings) {

            char score = string.charAt(string.length() - 2);

            if (score != '0'){
                value++;
                newScoreAll += Integer.parseInt(String.valueOf(score));
            }
        }

        if(value != 0) {
            newScore = newScoreAll / value;
            double scale = Math.pow(10, 1);
            newScore = (float) (Math.ceil(newScore * scale) / scale);
        }

        return newScore;
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

    private static Card map(CardApi.CardPlain cardPlain) throws ParseException {
        return new Card(
                cardPlain.id,
                cardPlain.middle_receipt,
                cardPlain.name,
                cardPlain.score,
                cardPlain.tag_fast_food,
                cardPlain.tag_sale,
                cardPlain.tag_with_itself,
                cardPlain.url_image,
                cardPlain.x_coordinate,
                cardPlain.y_coordinate,
                cardPlain.z_description,
                cardPlain.z_feedbacks
        );
    }
}
