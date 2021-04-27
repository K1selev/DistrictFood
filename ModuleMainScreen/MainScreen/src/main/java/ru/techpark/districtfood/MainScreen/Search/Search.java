package ru.techpark.districtfood.MainScreen.Search;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.MainScreen.CardsPreview.Card;

public class Search{

    private List<Card> cards;

    public Search() { }

    public List<Card> search(String restaurant){

        List<Card> newCards = new ArrayList<>();
        if (!restaurant.equals("")) {
            for (Card card : cards) {
                if (card.getName().toLowerCase().contains(restaurant.toLowerCase())) {
                    newCards.add(card);
                }
            }
        } else newCards = cards;

        return newCards;

    }

    public static Search sInstance;

    public synchronized static Search getInstance(){
        if (sInstance == null) {
            sInstance = new Search();
        }
        return sInstance;
    }

    public void SetCards(List<Card> cards){
        this.cards = cards;
    }

}
