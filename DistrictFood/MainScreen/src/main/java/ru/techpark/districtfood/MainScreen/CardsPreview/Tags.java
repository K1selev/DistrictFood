package ru.techpark.districtfood.MainScreen.CardsPreview;

public class Tags {

    private boolean mTag_with_itself;
    private boolean mTag_fast_food;
    private boolean mTag_sale;

    //получение из CardApi значения переменных для каждого ресторана
    public Tags(boolean tag_fast_food, boolean tag_sale, boolean tag_with_itself) {
        mTag_fast_food = tag_fast_food;
        mTag_sale = tag_sale;
        mTag_with_itself = tag_with_itself;
    }

    public boolean getTagWithItself(){ return mTag_with_itself; }

    public boolean getTagFastFood(){ return mTag_fast_food; }

    public boolean getTagSale(){ return mTag_sale; }

}
