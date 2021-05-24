package ru.techpark.districtfood.CachingByRoom;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
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

    @ColumnInfo(name = "url_image")
    private String mUrlImage;

    @ColumnInfo(name = "x_coordinate")
    private float mX_coordinate;

    @ColumnInfo(name = "y_coordinate")
    private float mY_coordinate;

    @ColumnInfo(name = "z_description")
    private String mZ_description;

    public Restaurant() {
    }

    public Restaurant(int mId, boolean mIsLike, int mMiddleReceipt, String mName, float mScore,
                      boolean mTagFastFood, boolean mTagSale, boolean mTagWithItself, String url_image,
                      float x_coordinate, float y_coordinate, String z_description) {
        this.mId = mId;
        this.mIsLike = mIsLike;
        this.mMiddleReceipt = mMiddleReceipt;
        this.mName = mName;
        this.mScore = mScore;
        this.mTagFastFood = mTagFastFood;
        this.mTagSale = mTagSale;
        this.mTagWithItself = mTagWithItself;
        this.mUrlImage = url_image;
        this.mX_coordinate = x_coordinate;
        this.mY_coordinate = y_coordinate;
        this.mZ_description = z_description;

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

    public String getUrlImage() {
        return mUrlImage;
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

    public void setUrlImage(String urlImage) {
        this.mUrlImage = urlImage;
    }

    public String getZ_description() {
        return mZ_description;
    }

    public void setZ_description(String mZ_description) {
        this.mZ_description = mZ_description;
    }

    public float getY_coordinate() {
        return mY_coordinate;
    }

    public void setY_coordinate(float mY_coordinate) {
        this.mY_coordinate = mY_coordinate;
    }

    public float getX_coordinate() {
        return mX_coordinate;
    }

    public void setX_coordinate(float mX_coordinate) {
        this.mX_coordinate = mX_coordinate;
    }
}
