package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.Map.FragmentMap;
import ru.techpark.districtfood.Map.MapAction;
import ru.techpark.districtfood.Map.MapViewModel;
import ru.techpark.districtfood.Panel.FragmentPanel;
import ru.techpark.districtfood.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsViewHolder> {

    private List<Card> mCards = new ArrayList<>();
    private List<Tags> mCardsTags = new ArrayList<>();
    private CardsViewModel mCardsViewModel;
    private TagsViewModel mTagsViewModel;
    private CallBackListener callBackListener;
    private CallBackListenerTags callBackListenerTags;

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_preview, parent, false);
        return new CardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {
        final Card card = mCards.get(position);
        final Tags cardsTags = mCardsTags.get(position);
        holder.mName.setText(card.getName());
        holder.mMiddleReceipt.setText(String.valueOf(card.getMiddle_receipt()));
        holder.mScore.setText(String.valueOf(card.getScore()));
        holder.IsLike(card.getIsLike());
        //callBackListenerTags.onCallBack(card);
        //holder.IsTagWithItself(cardsTags.getTagWithItself());
        //holder.IsTagFastFood(cardsTags.getTagFastFood());
        //holder.IsTadSale(cardsTags.getTagSale());
        holder.mMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackListener != null) {
                    callBackListener.onCallBack(card.getName());
                }
            }
        });
        holder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardsViewModel.like(card);
            }
        });
        holder.bind(mCards.size());
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    //срабатывает обновление recyclerView
    public void setCards(List<Card> cards, CardsViewModel mCardsViewModel, CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
        this.mCardsViewModel = mCardsViewModel;
        mCards = cards;
        notifyDataSetChanged();
    }
    public void setCards(List<Card> cards, CardsViewModel mCardsViewModel) {
        this.mCardsViewModel = mCardsViewModel;
        mCards = cards;
        notifyDataSetChanged();
    }

    public void setTags(List<Tags> cardsTags, TagsViewModel tagsViewModel, CallBackListenerTags callBackListenerTags) {
        this.mCardsTags = cardsTags;
        this.mTagsViewModel = tagsViewModel;
        this.callBackListenerTags = callBackListenerTags;
        notifyDataSetChanged();
    }

    public static CardsAdapter sInstance;
    public CardsAdapter() {

    }
    public synchronized static CardsAdapter getInstance(){
        if (sInstance == null) {
            sInstance = new CardsAdapter();
        }
        return sInstance;
    }

}
