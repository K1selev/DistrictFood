package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CachingByRoom.RestaurantDao;
import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.MainScreen.Filter.ApplyFilter;
import ru.techpark.districtfood.R;
import ru.techpark.districtfood.MainScreen.Search.Search;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class FragmentCards extends Fragment{

    private RecyclerView recyclerView;
    private CardsViewModel cardsViewModel;
    private CallBackListener callBackListener;
    private List<Card> cardList;
    private RestaurantDao restaurantDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (recyclerView == null) {
            recyclerView = getActivity().findViewById(R.id.cards_feed);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(CardsAdapter.getInstance());

        if (requireContext() instanceof CallBackListener) {
            this.callBackListener = (CallBackListener) requireContext();
        }

        restaurantDao = ApplicationModified.from(getContext()).
                getRestaurantDatabase().getRestaurantDao();

        Observer<List<Card>> observer = new Observer<List<Card>>() {
            @Override
            public void onChanged(List<Card> cards) {
                if (cards != null) {
                    cardList = cards;
                    CardsAdapter.getInstance().setCards(cards, cardsViewModel,
                            callBackListener, getContext());
                    if (cards.size() != 0) {
                        Thread ThreadForGetRoomDatabase = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Integer> restaurantBefore =
                                        CheckingForSimilarity(restaurantDao.getRestaurant1());
                                ArrayList<Integer> restaurantAfter =
                                        CheckingForSimilarity(completionRestaurant(cards));

                                if (!restaurantBefore.equals(restaurantAfter)) {
                                    restaurantDao.deleteAll();
                                    restaurantDao.insertRestaurants(completionRestaurant(cards));
                                }

                            }
                        });
                        ThreadForGetRoomDatabase.start();
                    }
                    Search.getInstance().SetCards(cards);
                    ApplyFilter.getInstance().SetCards(cards);
                }
            }
        };
        cardsViewModel = new ViewModelProvider(getActivity())
                .get(CardsViewModel.class);
        cardsViewModel.refresh();
        cardsViewModel
                .getCards()
                .observe(getViewLifecycleOwner(), observer);

    }

    private List<Restaurant> completionRestaurant(List<Card> cardsRoom) {

        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < cardsRoom.size(); i++){
            final Card cardRoom = cardsRoom.get(i);
            if (cardRoom.getIsLike()) {
                restaurants.add(new Restaurant(cardRoom.getId(), cardRoom.getIsLike(), cardRoom.getMiddle_receipt(),
                        cardRoom.getName(), cardRoom.getScore(), cardRoom.getTagFastFood(),
                        cardRoom.getTagSale(), cardRoom.getTagWithItself()));
            }
        }

        return restaurants;
    }

    private ArrayList<Integer> CheckingForSimilarity(List<Restaurant> restaurants) {

        ArrayList<Integer> arrayId = new ArrayList<>(restaurants.size());

        for (int i = 0; i < restaurants.size(); i++){
            arrayId.add(restaurants.get(i).getId());
        }

        return arrayId;
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView = null;
    }

    public static FragmentCards sInstance;
    public FragmentCards() {}
    public synchronized static FragmentCards getInstance(){
        if (sInstance == null) {
            sInstance = new FragmentCards();
        }
        return sInstance;
    }
    public CardsViewModel GetCardsViewModel() {
        return cardsViewModel;
    }
    public List<Card> getCardList() {
        return cardList;
    }
    public RestaurantDao getRestaurantDao() {
        return restaurantDao;
    }

}
