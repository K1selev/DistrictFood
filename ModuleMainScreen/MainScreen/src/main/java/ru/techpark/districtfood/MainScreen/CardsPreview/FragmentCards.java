package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;
import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainScreen.Filter.ApplyFilter;
import ru.techpark.districtfood.MainScreen.Search.Search;
import ru.techpark.districtfood.R;


public class FragmentCards implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    //private CardsViewModel cardsViewModel;
    private CallBackListener callBackListener;
    //private List<Card> cardList;
    private RestaurantDao restaurantDao;
    private Parcelable listState;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;
    private FragmentActivity activity;
    private LifecycleOwner lifecycleOwner;

    public void onCreateView(View view, Context context) {

        this.context = context;

        if (recyclerView == null) {
            recyclerView = view.findViewById(R.id.cards_feed);
        }

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

    }


    public void onResume(ProgressBar progressBar, FragmentActivity activity,
                         LifecycleOwner lifecycleOwner) {

        if (recyclerView == null) {
            recyclerView = ApplicationModified.recyclerView;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(CardsAdapter.getInstance());

        this.activity = activity;
        this.lifecycleOwner = lifecycleOwner;
        this.mProgressBar = progressBar;

        if (context instanceof CallBackListener) {
            this.callBackListener = (CallBackListener) context;
        }

        restaurantDao = ApplicationModified.from(context).
                getRestaurantDatabase().getRestaurantDao();

        ApplicationModified.cardsViewModel = new ViewModelProvider(this.activity)
                .get(CardsViewModel.class);
        ApplicationModified.cardsViewModel.refresh();
        ApplicationModified.cardsViewModel
                .getCards()
                .observe(this.lifecycleOwner, observer);

        if (ApplicationModified.bundleForSaveStateRecyclerView != null) {
            listState = ApplicationModified.bundleForSaveStateRecyclerView
                    .getParcelable(Constants.LIST_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }

        ApplicationModified.recyclerView = recyclerView;

        // если список нельзя прокрутить верх, разрешается работа swipe_refresh
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mSwipeRefreshLayout.setEnabled(!ApplicationModified.recyclerView
                        .canScrollVertically(-1));
            }
        });

    }

    Observer<List<Card>> observer = new Observer<List<Card>>() {
        @Override
        public void onChanged(List<Card> cards) {

            if (cards != null) {

                ApplicationModified.cardList = cards;
                //cardList = cards;

                if (hasConnection(context)) {
                    if (cards.size() != 0) {

                        Thread ThreadForGetRoomDatabase = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                restaurantDao.deleteAll();
                                restaurantDao.insertRestaurants(completionRestaurant(cards));
                                ApplicationModified.restaurantList = restaurantDao.getRestaurantAll();
                            }
                        });
                        ThreadForGetRoomDatabase.start();

                        try {
                            ThreadForGetRoomDatabase.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        hideProgress();

                        CardsAdapter.getInstance().setCards(cards, ApplicationModified.cardsViewModel,
                                callBackListener, context,
                                CheckOnHaveFiltersAndSearch());
                    }

                } else {
                    Thread ThreadForGetRoomDatabase = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ApplicationModified.restaurantList = restaurantDao.getRestaurantAll();
                        }
                    });
                    ThreadForGetRoomDatabase.start();

                    try {
                        ThreadForGetRoomDatabase.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    hideProgress();
                    Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();

                    CardsAdapter.getInstance().setCards(callBackListener, context,
                            CheckOnHaveFiltersAndSearch());
                }
            }
        }
    };

    @Override
    public void onRefresh() {

        if (hasConnection(context)) {

            ApplicationModified.cardsViewModel = null;
            restaurantDao = null;

            restaurantDao = ApplicationModified.from(context).
                    getRestaurantDatabase().getRestaurantDao();

            ApplicationModified.cardsViewModel = new ViewModelProvider(this.activity)
                    .get(CardsViewModel.class);
            ApplicationModified.cardsViewModel.refresh();
            ApplicationModified.cardsViewModel
                    .getCards()
                    .observe(lifecycleOwner, observer);

            mSwipeRefreshLayout.setRefreshing(false);

        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    private List<Restaurant> completionRestaurant(List<Card> cardsRoom) {

        List<Restaurant> restaurantAll = new ArrayList<>();

        for (int i = 0; i < cardsRoom.size(); i++){
            final Card cardRoom = cardsRoom.get(i);

            restaurantAll.add(new Restaurant(cardRoom.getId(),
                    cardRoom.getIsLike(), cardRoom.getMiddle_receipt(),
                    cardRoom.getName(), cardRoom.getScore(), cardRoom.getTagFastFood(),
                    cardRoom.getTagSale(), cardRoom.getTagWithItself(),
                    cardRoom.getUrlImage(), cardRoom.getX_coordinate(),
                    cardRoom.getY_coordinate(), cardRoom.getZ_description()));
        }

        return restaurantAll;
    }

    public void scrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    public void onStop() {
        ApplicationModified.bundleForSaveStateRecyclerView = new Bundle();
        ApplicationModified.bundleForSaveStateRecyclerView.putParcelable(Constants.LIST_STATE,
                recyclerView.getLayoutManager().onSaveInstanceState());
        recyclerView = null;
    }

    public void onViewStateRestored() {

    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();

        return activeNW != null && activeNW.isConnected();

    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            return wifiInfo.getNetworkId() != -1;
        } else {
            return false;
        }
    }


    public List<Restaurant> CheckOnHaveFiltersAndSearch(){
        List<Restaurant> newRestaurants;

        if (ApplicationModified.StringSearch != null && !ApplicationModified.StringSearch.equals("")){

            Search.getInstance().SetRestaurants(ApplicationModified.restaurantList);
            newRestaurants = Search.getInstance().search(ApplicationModified.StringSearch);

            ApplyFilter.getInstance().SetRestaurants(newRestaurants);
            newRestaurants = ApplyFilter.getInstance().apply(ApplicationModified.bundleFilter);
            ApplyFilter.getInstance().SetRestaurants(ApplicationModified.restaurantList);

        } else {
            ApplyFilter.getInstance().SetRestaurants(ApplicationModified.restaurantList);
            Search.getInstance().SetRestaurants(ApplicationModified.restaurantList);
            newRestaurants = ApplyFilter.getInstance().apply(ApplicationModified.bundleFilter);
        }

        return newRestaurants;
    }

    public static FragmentCards sInstance;
    public FragmentCards() {

    }
    public synchronized static FragmentCards getInstance(){
        if (sInstance == null) {
            sInstance = new FragmentCards();
        }
        return sInstance;
    }
    public CardsViewModel GetCardsViewModel() {
        return ApplicationModified.cardsViewModel;
    }
    public List<Card> getCardList() {
        return ApplicationModified.cardList;
    }
    public RestaurantDao getRestaurantDao() {
        return restaurantDao;
    }


}
