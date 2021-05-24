package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface CardApi {

    class CardPlain {
        public int id;
        public boolean like;
        public int middle_receipt;
        public String name;
        public float score;
        public boolean tag_fast_food;
        public boolean tag_sale;
        public boolean tag_with_itself;
        public String url_image;
        public float x_coordinate;
        public float y_coordinate;
        public String z_description;
    }

    @GET("/Restaurant.json")
    Call<List<CardPlain>> getAll();

    //сообщает о нажатии на лайк
    class Like {
        public boolean like;

        public Like(boolean like) {
            this.like = like;
        }
    }

    @PATCH("/Restaurant/{id}.json")
    Call<Like> like(@Path("id") int id, @Body Like like);

}
