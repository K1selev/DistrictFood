package ru.techpark.districtfood.MainScreen.CardsPreview;

public class Card {

    private int mId;
    private String mName;
    private int mMiddle_receipt;
    private float mScore;
    private boolean mIs_like;
    private boolean mTag_with_itself;
    private boolean mTag_fast_food;
    private boolean mTag_sale;

    //получение из CardApi значения переменных для каждого ресторана
    public Card(int id, boolean is_like, int middle_receipt, String name, float score,
                boolean tag_fast_food, boolean tag_sale, boolean tag_with_itself) {
        mId = id;
        mName = name;
        mMiddle_receipt = middle_receipt;
        mIs_like = is_like;
        mScore = score;
        mTag_fast_food = tag_fast_food;
        mTag_sale = tag_sale;
        mTag_with_itself = tag_with_itself;
    }

    public Card() {}

    public int getId() {
        return mId;
    }

    public boolean getTagWithItself(){ return mTag_with_itself; }

    public boolean getTagFastFood(){ return mTag_fast_food; }

    public boolean getTagSale(){ return mTag_sale; }

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
