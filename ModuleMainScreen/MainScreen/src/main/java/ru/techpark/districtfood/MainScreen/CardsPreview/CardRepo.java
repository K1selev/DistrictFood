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
import ru.techpark.districtfood.Bookmarks.BookmarksViewModel;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;
import ru.techpark.districtfood.Network.ApiRepo;

public class CardRepo {

    private final static MutableLiveData<List<Card>> mCards = new MutableLiveData<>();
    private List<Card> cardList;
    private BookmarksViewModel bookmarksViewModel;
    private RestaurantDao restaurantDao;
    
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
    public LiveData<List<Card>> getCards(RestaurantDao restaurantDao) {

        return Transformations.map(mCards, input -> {
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

    public void likeFromBookmarks(final Card card, BookmarksViewModel bookmarksViewModel,
                                  RestaurantDao restaurantDao) {
        this.bookmarksViewModel = bookmarksViewModel;
        this.restaurantDao = restaurantDao;
        mCardApi.like(card.getId(), new CardApi.Like(!card.getIsLike())).enqueue(new Callback<CardApi.Like>() {
            @Override
            public void onResponse(Call<CardApi.Like> call,
                                   Response<CardApi.Like> response) {
                if (response.isSuccessful()) {
                    refreshFromBookmarks();
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

    public void refreshFromBookmarks() {
        mCardApi.getAll().enqueue(new Callback<List<CardApi.CardPlain>>() {
            @Override
            public void onResponse(Call<List<CardApi.CardPlain>> call,
                                   Response<List<CardApi.CardPlain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mCards.postValue(transformFromBookmarks(response.body()));
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

    private List<Card> transformFromBookmarks(List<CardApi.CardPlain> plains) {
        List<Card> result = new ArrayList<>();
        for (CardApi.CardPlain cardPlain : plains) {
            try {
                Card card = map(cardPlain);
                result.add(card);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cardList = result;
        bookmarksViewModel.like(restaurantDao, cardList);
        return result;
    }

    private static Card map(CardApi.CardPlain cardPlain) throws ParseException {
        return new Card(
                cardPlain.id,
                cardPlain.like,
                cardPlain.middle_receipt,
                cardPlain.name,
                cardPlain.score,
                cardPlain.tag_fast_food,
                cardPlain.tag_sale,
                cardPlain.tag_with_itself,
                cardPlain.url_image
        );
    }
}
