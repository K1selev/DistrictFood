package ru.techpark.districtfood.Bookmarks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;
import ru.techpark.districtfood.CachingByRoom.RestaurantsDatabase;
import ru.techpark.districtfood.MainScreen.CardsPreview.Card;

public class BookmarksRepo {

    private final static MutableLiveData<List<Restaurant>> mRestaurants = new MutableLiveData<>();
    static {
        mRestaurants.setValue(Collections.emptyList());
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        RestaurantsDatabase db = ApplicationModified.getInstance().getRestaurantDatabase();
        RestaurantDao restaurantDao = db.getRestaurantDao();
        return restaurantDao.getRestaurant();
    }

    public void like(RestaurantDao restaurantDao, List<Card> cards, Restaurant restaurant) {

        if (cards.size() != 0) {
            Thread ThreadForGetRoomDatabase = new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Boolean> LikeAll = GetLikeAll(ApplicationModified.restaurantDao, restaurant);
                    restaurantDao.deleteAll();
                    restaurantDao.insertRestaurants(completionRestaurant(cards, LikeAll));
                }
            });
            ThreadForGetRoomDatabase.start();
        }
    }

    public void feedbacks(RestaurantDao restaurantDao, List<Card> cards, Restaurant restaurant, String feedback){
        if (cards.size() != 0) {
            Thread ThreadForGetRoomDatabase = new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Boolean> LikeAll = GetLikeAllFromFeedbacks(restaurantDao);
                    List<String> FeedbacksAll = GetFeedbacks(restaurantDao, restaurant, feedback);
                    List<Float> ScoresAll = GetScore(restaurantDao, restaurant,
                            FeedbacksAll.get(restaurant.getId()));
                    restaurantDao.deleteAll();
                    restaurantDao.insertRestaurants(completionRestaurantFromFeedbacks(cards, LikeAll,
                            FeedbacksAll, ScoresAll));
                }
            });
            ThreadForGetRoomDatabase.start();
        }
    }


    private String ConvertCardFeedback(Restaurant restaurant, String feedback){

        String feedbacks = restaurant.getFeedbacks();
        feedbacks += feedback;

        return feedbacks;
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

    private List<Boolean> GetLikeAllFromFeedbacks(RestaurantDao restaurantDao) {

        List<Boolean> LikeAll = new ArrayList<>(restaurantDao.getRestaurantAllWithoutLiveData().size());
        for (Restaurant restaurant1 : restaurantDao.getRestaurantAllWithoutLiveData()) {
            LikeAll.add(restaurant1.isLike());
        }
        return LikeAll;
    }

    private List<String> GetFeedbacks(RestaurantDao restaurantDao, Restaurant restaurant, String feedback){

        List<String> FeedbacksAll = new ArrayList<>(restaurantDao.getRestaurantAllWithoutLiveData().size());
        for (Restaurant restaurant1 : restaurantDao.getRestaurantAllWithoutLiveData()) {
            if (restaurant1.getId() == restaurant.getId()) {
                String newFeedbacks = ConvertCardFeedback(restaurant, feedback);
                FeedbacksAll.add(newFeedbacks);
            } else FeedbacksAll.add(restaurant1.getFeedbacks());
        }

        return FeedbacksAll;
    }

    private List<Float> GetScore(RestaurantDao restaurantDao, Restaurant restaurant, String feedback){

        List<Float> ScoresAll = new ArrayList<>(restaurantDao.getRestaurantAllWithoutLiveData().size());
        for (Restaurant restaurant1 : restaurantDao.getRestaurantAllWithoutLiveData()) {
            if (restaurant1.getId() == restaurant.getId()) {
                float newScore = ConvertScoreCard(feedback);
                ScoresAll.add(newScore);
            } else ScoresAll.add(restaurant1.getScore());
        }

        return ScoresAll;
    }

    private List<Restaurant> completionRestaurantFromFeedbacks(List<Card> cardsRoom, List<Boolean> LikeAll,
                                                               List<String> feedbacks, List<Float> scores) {

        List<Restaurant> restaurantAll = new ArrayList<>();

        for (int i = 0; i < cardsRoom.size(); i++) {
            final Card cardRoom = cardsRoom.get(i);

            boolean like = LikeAll.get(i);
            String feedback = feedbacks.get(i);
            float score = scores.get(i);

            restaurantAll.add(new Restaurant(cardRoom.getId(),
                    like, cardRoom.getMiddle_receipt(),
                    cardRoom.getName(), score, cardRoom.getTagFastFood(),
                    cardRoom.getTagSale(), cardRoom.getTagWithItself(),
                    cardRoom.getUrlImage(), cardRoom.getX_coordinate(),
                    cardRoom.getY_coordinate(), cardRoom.getZ_description(), feedback));
        }

        ApplicationModified.restaurantList = restaurantAll;
        return restaurantAll;
    }

    private List<Restaurant> completionRestaurant(List<Card> cardsRoom, List<Boolean> LikeAll) {

        List<Restaurant> restaurantForBookmarks = new ArrayList<>();

        for (int i = 0; i < cardsRoom.size(); i++) {
            final Card cardRoom = cardsRoom.get(i);

            boolean like = LikeAll.get(i);

            restaurantForBookmarks.add(new Restaurant(cardRoom.getId(),
                    like, cardRoom.getMiddle_receipt(),
                    cardRoom.getName(), cardRoom.getScore(), cardRoom.getTagFastFood(),
                    cardRoom.getTagSale(), cardRoom.getTagWithItself(),
                    cardRoom.getUrlImage(), cardRoom.getX_coordinate(),
                    cardRoom.getY_coordinate(), cardRoom.getZ_description(), cardRoom.getFeedbacks()));
        }


        return restaurantForBookmarks;
    }

    private List<Boolean> GetLikeAll (RestaurantDao restaurantDao, Restaurant restaurant) {

        List<Boolean> LikeAll = new ArrayList<>(restaurantDao.getRestaurantAllWithoutLiveData().size());
        for (Restaurant restaurant1 : restaurantDao.getRestaurantAllWithoutLiveData()) {
            if (restaurant1.getId() == restaurant.getId()) {
                LikeAll.add(!restaurant1.isLike());
            } else LikeAll.add(restaurant1.isLike());
        }
        return LikeAll;
    }

}
