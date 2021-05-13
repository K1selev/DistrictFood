package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.Map.FragmentMap;
import ru.techpark.districtfood.Map.MapAction;
import ru.techpark.districtfood.Map.MapViewModel;
import ru.techpark.districtfood.Panel.FragmentPanel;
import ru.techpark.districtfood.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsViewHolder> {

    private List<Card> mCards = new ArrayList<>();
    private CardsViewModel mCardsViewModel;
    private CallBackListener callBackListener;
    private Context context;

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_preview, parent, false);
        return new CardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {
        final Card card = mCards.get(position);
        holder.mName.setText(card.getName());
        holder.mMiddleReceipt.setText(String.valueOf(card.getMiddle_receipt()));
        holder.mScore.setText(String.valueOf(card.getScore()));
        holder.IsLike(card.getIsLike());

        holder.mMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackListener != null) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_MAP_TAB, card.getName());
                }
            }
        });
        holder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasConnection(context)) {
                    mCardsViewModel.like(card);
                }
                else  Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
            }
        });
        holder.click_for_transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackListener != null) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_TAB, card.getName());
                }
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackListener != null) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_TAB, card.getName());
                }
            }
        });
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    //срабатывает обновление recyclerView
    public void setCards(List<Card> cards, CardsViewModel mCardsViewModel,
                         CallBackListener callBackListener, Context context) {
        this.callBackListener = callBackListener;
        this.mCardsViewModel = mCardsViewModel;
        mCards = cards;
        this.context = context;
        notifyDataSetChanged();
    }
    public void setCards(List<Card> cards, CardsViewModel mCardsViewModel) {
        this.mCardsViewModel = mCardsViewModel;
        mCards = cards;
        notifyDataSetChanged();
    }

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        if (activeNW != null && activeNW.isConnected()) {
            return true;
        }
        return false;
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
