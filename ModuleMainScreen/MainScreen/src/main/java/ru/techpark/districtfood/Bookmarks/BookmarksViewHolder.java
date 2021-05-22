package ru.techpark.districtfood.Bookmarks;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import ru.techpark.districtfood.R;

public class BookmarksViewHolder extends RecyclerView.ViewHolder{

    protected ImageView img;
    protected ImageView click_for_transition;
    protected boolean mIsLike;
    protected ImageButton mLikeBtn;
    protected TextView mMiddleReceipt;
    protected TextView mScore;
    protected TextView mName;
    protected String url_image;

    public BookmarksViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.photo_restaurant);
        click_for_transition = itemView.findViewById(R.id.click_for_transition);
        mName = itemView.findViewById(R.id.named_restaurant);
        mLikeBtn = itemView.findViewById(R.id.like);
        mMiddleReceipt = itemView.findViewById(R.id.description);
        mScore = itemView.findViewById(R.id.score_preview);
    }

    public void bind(){
        if (url_image != null) {
            Picasso.get().load(url_image).resize(275, 150).centerCrop().into(img);
        }

        if (mIsLike){
            mLikeBtn.setImageResource(R.drawable.like_true);
        }else {
            mLikeBtn.setImageResource(R.drawable.like);
        }

    }

    public void IsLike(boolean mIsLike){
        this.mIsLike = mIsLike;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

}
