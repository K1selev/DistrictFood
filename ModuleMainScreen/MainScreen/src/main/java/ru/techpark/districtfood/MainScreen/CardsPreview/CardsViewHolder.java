package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import ru.techpark.districtfood.R;

public class CardsViewHolder extends RecyclerView.ViewHolder {
    protected ImageView img;
    protected ImageView click_for_transition;
    protected boolean mIsLike;
    protected ImageView mLikeBtn;
    protected ImageButton mMapBtn;
    protected TextView mMiddleReceipt;
    protected TextView mDescription;
    protected TextView mScore;
    protected TextView mName;
    protected String url_image;

    public CardsViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.photo_restaurant);
        click_for_transition = itemView.findViewById(R.id.click_for_transition);
        mName = itemView.findViewById(R.id.named_restaurant);
        mLikeBtn = itemView.findViewById(R.id.like);
        mMiddleReceipt = itemView.findViewById(R.id.middle_receipt);
        mDescription = itemView.findViewById(R.id.description);
        mScore = itemView.findViewById(R.id.score_preview);
        mMapBtn = itemView.findViewById(R.id.route);

    }

    public void bind(){
        if (url_image != null) {
            Picasso.get().load(url_image).resize(275, 150).centerCrop().into(img);
        }

        if (mIsLike){
            mLikeBtn.setBackgroundResource(R.drawable.like_true);
        }else {
            mLikeBtn.setBackgroundResource(R.drawable.like);
        }

    }

    public void IsLike(boolean mIsLike){
        this.mIsLike = mIsLike;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

}
