package ru.techpark.districtfood.MainScreen.CardsPreview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface CardApi {

    class CardPlain {
        public int id;
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
        public String z_feedbacks;
    }

    @GET("/Restaurant.json")
    Call<List<CardPlain>> getAll();

    class Feedbacks {
        public String z_feedbacks;

        public Feedbacks(String z_feedbacks) {
            this.z_feedbacks = z_feedbacks;
        }
    }

    class Score {
        public float score;

        public Score(float score) {
            this.score = score;
        }
    }

    @PATCH("/Restaurant/{id}.json")
    Call<Feedbacks> z_feedbacks(@Path("id") int id, @Body Feedbacks z_feedbacks);

    @PATCH("/Restaurant/{id}.json")
    Call<Score> score(@Path("id") int id, @Body Score score);
}
