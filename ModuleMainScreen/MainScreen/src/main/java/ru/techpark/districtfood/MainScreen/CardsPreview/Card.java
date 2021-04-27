package ru.techpark.districtfood.MainScreen.CardsPreview;

import java.util.List;

public class Card {

    private int mId;
    private String mName;
    private int mMiddle_receipt;
    private float mScore;
    private boolean mIs_like;
    private List<CardApi.TagsPlain> mTags;

    //получение из CardApi значения переменных для каждого ресторана
    public Card(int id, boolean is_like, int middle_receipt, String name, float score, List<CardApi.TagsPlain> tags) {
        mId = id;
        mName = name;
        mMiddle_receipt = middle_receipt;
        mIs_like = is_like;
        mScore = score;
        mTags = tags;
    }

    public Card() {}

    public int getId() {
        return mId;
    }

    public List<CardApi.TagsPlain> getTags() { return mTags; }

    public String getName() {
        return mName;
    }

    public boolean getIsLike() {
        return mIs_like;
    }

    public int getMiddle_receipt() {
        return mMiddle_receipt;
    }

    public float getScore() {
        return mScore;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setIsLike(boolean is_like) {
        mIs_like = is_like;
    }

    public void setMiddle_receipt(int middle_receipt){ mMiddle_receipt = middle_receipt; }

    public void setScore(float score){ mScore = score; }

}
