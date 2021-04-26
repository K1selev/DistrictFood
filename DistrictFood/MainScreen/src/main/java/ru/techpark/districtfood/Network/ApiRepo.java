package ru.techpark.districtfood.Network;

import android.content.Context;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.MainScreen.CardsPreview.CardApi;

public class ApiRepo {

    private final CardApi mCardApi;
    private final OkHttpClient mOkHttpClient;

    public ApiRepo() {
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(new HttpUrl.Builder().scheme("https")
                        .host("districtfood-f65b1-default-rtdb.firebaseio.com")
                        .build())
                .client(mOkHttpClient)
                .build();

        mCardApi = retrofit.create(CardApi.class);
    }

    public CardApi getCardApi() {
        return mCardApi;
    }

    public static ApiRepo from(Context context) {
        return ApplicationModified.from(context).getApis();
    }

}
