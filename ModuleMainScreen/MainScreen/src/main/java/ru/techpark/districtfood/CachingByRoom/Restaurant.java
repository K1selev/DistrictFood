package ru.techpark.districtfood.CachingByRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Restaurant {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "isLike")
    private boolean mIsLike;

    @ColumnInfo(name = "middleReceipt")
    private int mMiddleReceipt;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "score")
    private float mScore;

    @ColumnInfo(name = "tagFastFood")
    private boolean mTagFastFood;

    @ColumnInfo(name = "tagSale")
    private boolean mTagSale;

    @ColumnInfo(name = "tagWithItself")
    private boolean mTagWithItself;

    public Restaurant() {
    }

    public Restaurant(int mId, boolean mIsLike, int mMiddleReceipt, String mName, float mScore,
                      boolean mTagFastFood, boolean mTagSale, boolean mTagWithItself) {
        this.mId = mId;
        this.mIsLike = mIsLike;
        this.mMiddleReceipt = mMiddleReceipt;
        this.mName = mName;
        this.mScore = mScore;
        this.mTagFastFood = mTagFastFood;
        this.mTagSale = mTagSale;
        this.mTagWithItself = mTagWithItself;
    }

    public int getId() {
        return mId;
    }

    public boolean isLike() {
        return mIsLike;
    }

    public int getMiddleReceipt() {
        return mMiddleReceipt;
    }

    public String getName() {
        return mName;
    }

    public float getScore() {
        return mScore;
    }

    public boolean isTagFastFood() {
        return mTagFastFood;
    }

    public boolean isTagSale() {
        return mTagSale;
    }

    public boolean isTagWithItself() {
        return mTagWithItself;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setIsLike(boolean mIsLike) {
        this.mIsLike = mIsLike;
    }

    public void setMiddleReceipt(int mMiddleReceipt) {
        this.mMiddleReceipt = mMiddleReceipt;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setScore(float mScore) {
        this.mScore = mScore;
    }

    public void setTagFastFood(boolean mTagFastFood) {
        this.mTagFastFood = mTagFastFood;
    }

    public void setTagSale(boolean mTagSale) {
        this.mTagSale = mTagSale;
    }

    public void setTagWithItself(boolean mTagWithItself) {
        this.mTagWithItself = mTagWithItself;
    }
}
