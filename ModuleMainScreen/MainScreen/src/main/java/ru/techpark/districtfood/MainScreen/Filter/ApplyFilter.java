package ru.techpark.districtfood.MainScreen.Filter;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.Constants;

public class ApplyFilter {

    private List<Restaurant> restaurants;

    public ApplyFilter() { }

    public List<Restaurant> apply(Bundle BundleKeyFilter){

        List<Restaurant> newCards_MR = new ArrayList<>();
        List<Restaurant> newCards_S = new ArrayList<>();
        List<Restaurant> newCards_TFF = new ArrayList<>();
        List<Restaurant> newCards_TS = new ArrayList<>();
        List<Restaurant> newCards_TWI = new ArrayList<>();

        boolean tag_with_itself = BundleKeyFilter.getBoolean(Constants.TAG_WITH_ITSELF, false);
        boolean tag_fast_food = BundleKeyFilter.getBoolean(Constants.TAG_FAST_FOOD, false);
        boolean tag_sale = BundleKeyFilter.getBoolean(Constants.TAF_SALE, false);
        float number_star = BundleKeyFilter.getFloat(Constants.NUMBER_STAR, 0);
        String text_middle_receipt = BundleKeyFilter.getString(Constants.TEXT_MIDDLE_RECEIPT, "");

        if (!text_middle_receipt.equals("")) {
            for (Restaurant card : restaurants) {
                if (card.getMiddleReceipt() <= Integer.parseInt(text_middle_receipt)) {
                    newCards_MR.add(card);
                }
            }
        }else newCards_MR = restaurants;

        if (number_star != 0) {
            for (Restaurant card : restaurants) {
                if (card.getScore() >= number_star) {
                    for (Restaurant card1 : newCards_MR) {
                        if (card == card1) {
                            newCards_S.add(card);
                            break;
                        }
                    }
                }
            }
        }else newCards_S = newCards_MR;

        if (tag_fast_food) {
            for (Restaurant card : restaurants) {
                if (card.isTagFastFood()) {
                    for (Restaurant card1 : newCards_S) {
                        if (card == card1) {
                            newCards_TFF.add(card);
                            break;
                        }
                    }
                }
            }
        }else newCards_TFF = newCards_S;

        if (tag_sale) {
            for (Restaurant card : restaurants) {
                if (card.isTagSale()) {
                    for (Restaurant card1 : newCards_TFF) {
                        if (card == card1) {
                            newCards_TS.add(card);
                            break;
                        }
                    }
                }
            }
        }else newCards_TS = newCards_TFF;

        if (tag_with_itself) {
            for (Restaurant card : restaurants) {
                if (card.isTagWithItself()) {
                    for (Restaurant card1 : newCards_TS) {
                        if (card == card1) {
                            newCards_TWI.add(card);
                            break;
                        }
                    }
                }
            }
        }else newCards_TWI = newCards_TS;

        return newCards_TWI;

    }

    public static ApplyFilter sInstance;

    public synchronized static ApplyFilter getInstance(){
        if (sInstance == null) {
            sInstance = new ApplyFilter();
        }
        return sInstance;
    }

    public void SetRestaurants(List<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

}
