package ru.techpark.districtfood.Bookmarks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;
import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainActivity;
import ru.techpark.districtfood.MainScreen.CardsPreview.Card;
import ru.techpark.districtfood.MainScreen.CardsPreview.CardsViewModel;
import ru.techpark.districtfood.R;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksViewHolder>{

    private List<Restaurant> mRestaurants = new ArrayList<>();
    private List<Card> mCards = new ArrayList<>();
    private CardsViewModel mCardsViewModel;
    private BookmarksViewModel mBookmarksViewModel;
    private CallBackListener callBackListener;
    private RestaurantDao restaurantDao;
    private Context context;

    @NonNull
    @Override
    public BookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_card_preview, parent, false);
        return new BookmarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksViewHolder holder, int position) {
        final Restaurant restaurant = mRestaurants.get(position);
        holder.mName.setText(restaurant.getName());
        holder.mMiddleReceipt.setText(String.valueOf(restaurant.getMiddleReceipt()));
        holder.mScore.setText(String.valueOf(restaurant.getScore()));
        holder.IsLike(restaurant.isLike());

        holder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasConnection(context)) {
                    Card card = null;
                    for (Card mCard : mCards) {
                        if (mCard.getName().equals(restaurant.getName())){
                            card = mCard;
                            break;
                        }
                    }
                    mCardsViewModel.likeFromBookmarks(card, mBookmarksViewModel, restaurantDao);
                }
                else  Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();

            }
        });
        holder.click_for_transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackListener != null) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS, restaurant.getName());
                }
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackListener != null) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS, restaurant.getName());
                }
            }
        });
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    //срабатывает обновление recyclerView
    public void setCardsAndRestaurants(List<Restaurant> restaurants,
                                       List<Card> cards,
                                       CardsViewModel cardsViewModel,
                                       BookmarksViewModel bookmarksViewModel,
                                       CallBackListener callBackListener,
                                       RestaurantDao restaurantDao,
                                       Context context)
    {
        this.callBackListener = callBackListener;
        this.mCardsViewModel = cardsViewModel;
        this.mBookmarksViewModel = bookmarksViewModel;
        this.mCards = cards;
        this.mRestaurants = restaurants;
        this.restaurantDao = restaurantDao;
        this.context = context;
        notifyDataSetChanged();
    }

    public static BookmarksAdapter sInstance;
    public BookmarksAdapter() {
    }
    public synchronized static BookmarksAdapter getInstance(){
        if (sInstance == null) {
            sInstance = new BookmarksAdapter();
        }
        return sInstance;
    }

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        if (activeNW != null && activeNW.isConnected()) {
            return true;
        }
        return false;
    }

}
