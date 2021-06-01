package ru.techpark.districtfood.MainScreen.CardsPreview;

public class Card {

    private int mId;
    private String mName;
    private int mMiddle_receipt;
    private float mScore;
    private boolean mTag_with_itself;
    private boolean mTag_fast_food;
    private boolean mTag_sale;
    private String mUrl_Image;
    private float mX_coordinate;
    private float mY_coordinate;
    private String mZ_description;
    private String mFeedbacks;

    //получение из CardApi значения переменных для каждого ресторана
    public Card(int id, int middle_receipt, String name, float score,
                boolean tag_fast_food, boolean tag_sale, boolean tag_with_itself, String url_image,
                float x_coordinate, float y_coordinate, String z_description, String feedbacks) {
        mId = id;
        mName = name;
        mMiddle_receipt = middle_receipt;
        mScore = score;
        mTag_fast_food = tag_fast_food;
        mTag_sale = tag_sale;
        mTag_with_itself = tag_with_itself;
        mUrl_Image = url_image;
        mX_coordinate = x_coordinate;
        mY_coordinate = y_coordinate;
        mZ_description = z_description;
        mFeedbacks = feedbacks;
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

    public int getMiddle_receipt() {
        return mMiddle_receipt;
    }

    public float getScore() {
        return mScore;
    }

    public String getUrlImage() {
        return mUrl_Image;
    }

    public float getX_coordinate() {
        return mX_coordinate;
    }

    public float getY_coordinate() {
        return mY_coordinate;
    }

    public String getZ_description() {
        return mZ_description;
    }

    public String getFeedbacks() {
        return mFeedbacks;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setMiddle_receipt(int middle_receipt){ mMiddle_receipt = middle_receipt; }

    public void setScore(float score){ mScore = score; }

    public void setFeedbacks(String feedbacks) {
        mFeedbacks = feedbacks;
    }

}
