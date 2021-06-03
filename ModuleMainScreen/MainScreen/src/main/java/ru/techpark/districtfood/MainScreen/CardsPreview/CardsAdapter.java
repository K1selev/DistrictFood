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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsViewHolder> {

    private CallBackListener callBackListener;
    private Context context;
    private List<Restaurant> mRestaurantAll = new ArrayList<>();

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_preview, parent, false);
        return new CardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {
        final Restaurant restaurantAll = mRestaurantAll.get(position);

        holder.setUrl_image(restaurantAll.getUrlImage());
        holder.mDescription.setText(restaurantAll.getZ_description());
        holder.mName.setText(restaurantAll.getName());
        holder.mMiddleReceipt.setText(String.valueOf(restaurantAll.getMiddleReceipt()));
        holder.mScore.setText(String.valueOf(restaurantAll.getScore()));
        holder.IsLike(restaurantAll.isLike());

        /*int line_of_text  = holder.mDescription.getLineCount();


        if (line_of_text > 3){
            holder.mDescription.setMaxLines(3);
            holder.open_full_text.setVisibility(View.VISIBLE);
        }*/

        holder.open_full_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mDescription.setMaxLines(100);
                holder.open_full_text.setVisibility(View.GONE);
            }
        });

        holder.mDescription.post(new Runnable() {
            @Override
            public void run() {
                int line_of_text  = holder.mDescription.getLineCount();
                Log.d("test", line_of_text + "");
                if (line_of_text > 3){
                    holder.mDescription.setMaxLines(3);
                    holder.open_full_text.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.mMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConnection(context) && callBackListener != null) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_MAP_TAB, restaurantAll);
                } else Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
            }
        });
        holder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConnection(context)) {
                    ApplicationModified.restaurantAllViewModel.like(ApplicationModified.restaurantDao,
                            ApplicationModified.cardList, restaurantAll);
                } else Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
            }
        });
        /*holder.click_for_transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_TAB, restaurantAll);
            }
        });*/
        
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_TAB, restaurantAll);
            }
        });
        holder.bind();

    }

    @Override
    public int getItemCount() {
        return mRestaurantAll.size();
    }


    //срабатывает обновление recyclerView
    public void setCards(CallBackListener callBackListener,
                         Context context,
                         List<Restaurant> restaurantAll) {
        this.callBackListener = callBackListener;
        this.context = context;

        this.mRestaurantAll = restaurantAll;

        notifyDataSetChanged();
    }
    public void setCards(List<Restaurant> restaurants) {
        this.mRestaurantAll = restaurants;
        notifyDataSetChanged();
    }

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        return activeNW != null && activeNW.isConnected();
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
