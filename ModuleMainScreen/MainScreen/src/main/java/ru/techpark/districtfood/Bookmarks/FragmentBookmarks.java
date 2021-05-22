package ru.techpark.districtfood.Bookmarks;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.MainScreen.CardsPreview.FragmentCards;
import ru.techpark.districtfood.R;

public class FragmentBookmarks extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private CallBackListener callBackListener;
    private BookmarksViewModel bookmarksViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //LoaderManager.getInstance(this).initLoader(123, null, this);

        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        if (recyclerView == null) {
            recyclerView = getActivity().findViewById(R.id.bookmarks_feed);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(BookmarksAdapter.getInstance());

        if (requireContext() instanceof CallBackListener) {
            this.callBackListener = (CallBackListener) requireContext();
        }

        Observer<List<Restaurant>> observer = new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {

                if (restaurants.size() == 0){
                    TextView textView = view.findViewById(R.id.not_bookmarks);
                    textView.setText(R.string.not_bookmarks);
                } else {
                    TextView textView = view.findViewById(R.id.not_bookmarks);
                    textView.setText("");
                }

                BookmarksAdapter.getInstance().setCardsAndRestaurants(
                        restaurants,
                        FragmentCards.getInstance().getCardList(),
                        FragmentCards.getInstance().GetCardsViewModel(),
                        bookmarksViewModel,
                        callBackListener,
                        FragmentCards.getInstance().getRestaurantDao(),
                        getContext());
            }
        };

        bookmarksViewModel = new ViewModelProvider(getActivity())
                .get(BookmarksViewModel.class);
        bookmarksViewModel
                .getRestaurants()
                .observe(getViewLifecycleOwner(), observer);
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView = null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
        /*new CursorLoader(getActivity(),
                Uri.parse("content://techpark.ru.districtfood.restaurantprovider/restaurant"),
                null,
                null,
                null,
                null);*/
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        /*if (data != null && data.moveToFirst()) {
            RestaurantsDatabase db = ApplicationModified.getInstance().getRestaurantDatabase();
            RestaurantDao restaurantDao = db.getRestaurantDao();
            List<Restaurant> restaurants = restaurantDao.getRestaurant();

        }*/

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}
