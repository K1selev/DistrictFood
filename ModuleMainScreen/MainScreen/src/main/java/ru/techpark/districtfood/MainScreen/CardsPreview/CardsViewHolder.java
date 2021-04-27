package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.techpark.districtfood.R;

public class CardsViewHolder extends RecyclerView.ViewHolder {
    private ImageView img;

    protected TextView mName;
    protected boolean mIsLike;
    protected ImageButton mLikeBtn;
    protected TextView mMiddleReceipt;
    protected TextView mScore;
    protected ImageButton mMapBtn;
    protected boolean mTagsWithItself;
    protected boolean mTagsFastFood;
    protected boolean mTagsSale;

    public CardsViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.photo_restaurant);
        mName = itemView.findViewById(R.id.named_restaurant);
        mLikeBtn = itemView.findViewById(R.id.like);
        mMiddleReceipt = itemView.findViewById(R.id.description);
        mScore = itemView.findViewById(R.id.score_preview);
        mMapBtn = itemView.findViewById(R.id.route);
    }

    public void bind(int a){

        img.setImageResource(R.drawable.food);
        if (mIsLike){
            mLikeBtn.setImageResource(R.drawable.like_true);
        }else {
            mLikeBtn.setImageResource(R.drawable.like);
        }

    }

    public void IsLike(boolean mIsLike){
        this.mIsLike = mIsLike;
    }

    public void IsTagWithItself(boolean mTagsWithItself){
        this.mTagsWithItself = mTagsWithItself;
    }
    public void IsTagFastFood(boolean mTagsFastFood){
        this.mTagsFastFood = mTagsFastFood;
    }
    public void IsTadSale(boolean mTagsSale){
        this.mTagsSale = mTagsSale;
    }
}
