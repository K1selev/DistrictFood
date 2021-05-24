package ru.techpark.districtfood.RestaurantTab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.CallBackListener;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainActivity;
import ru.techpark.districtfood.MainScreen.CardsPreview.Card;
import ru.techpark.districtfood.MainScreen.CardsPreview.FragmentCards;
import ru.techpark.districtfood.R;

public class FragmentRestaurantTab extends Fragment {

    private TextView text_name;
    private TextView text_score;
    private TextView text_middle_receipt;
    private TextView text_description;
    private ImageView imageView;
    private EditText editText_feedback;
    private Button button_feedbacks;
    private CallBackListener callBackListener;
    private ImageButton route;
    private ImageView like;
    private boolean flag;

    private String name;
    private String action;
    private String description;
    private float middle_receipt;
    private float score;
    private String url;
    private int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (requireContext() instanceof CallBackListener){
            callBackListener = (CallBackListener) requireContext();
        }

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ApplicationModified.enabled_recyclerView = false;

        return inflater.inflate(R.layout.fragment_restaurant_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text_name = view.findViewById(R.id.named_restaurant_tab);
        text_score = view.findViewById(R.id.score_preview_tab);
        text_description = view.findViewById(R.id.description_tab);
        text_middle_receipt = view.findViewById(R.id.middle_receipt_tab);

        imageView = view.findViewById(R.id.photo_restaurant_tab);

        editText_feedback = view.findViewById(R.id.feedback);
        editText_feedback.setOnEditorActionListener(Feedback);

        button_feedbacks = view.findViewById(R.id.btn_feedback);
        button_feedbacks.setOnClickListener(ButtonFeedback);

        route = view.findViewById(R.id.route_cards);
        route.setOnClickListener(ButtonRoute);

        like = view.findViewById(R.id.like_cards);
        like.setOnClickListener(LikeButton);

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        name = arguments.getString(Constants.NAME_RESTAURANT);
        middle_receipt = arguments.getFloat(Constants.TEXT_MIDDLE_RECEIPT);
        score = arguments.getFloat(Constants.SCORE);
        description = arguments.getString(Constants.DESCRIPTION);
        url = arguments.getString(Constants.URL);
        id = arguments.getInt(Constants.ID);
        action = arguments.getString(Constants.ACTION_OPEN_RESTAURANT_TAB);

        text_name.setText(name);
        text_score.setText(String.valueOf(score));
        text_middle_receipt.setText(String.valueOf(middle_receipt));
        text_description.setText(description);

        Restaurant restaurant = null;
        for (Restaurant restaurant_for: ApplicationModified.restaurantList) {
            if (restaurant_for.getId() == id) {
                restaurant = restaurant_for;
            }
        }

        if (!restaurant.isLike()) {
            like.setBackgroundResource(R.drawable.like);
            flag = true;
        } else {
            flag = false;
            like.setBackgroundResource(R.drawable.like_true);
        }

        if (url != null) {
            Picasso.get().load(url).resize(275, 150).centerCrop().into(imageView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModified.enabled_recyclerView = true;
    }

    private final TextView.OnEditorActionListener Feedback = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow()
                        .getDecorView().getWindowToken(), 0); //убирает курсор в поле ввода отзыва
                v.clearFocus();

                if (hasConnection(requireContext())) {
                    editText_feedback.setText("");
                } else  Toast.makeText(requireContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();

                handled = true;
            }
            return handled;
        }
    };

    private final View.OnClickListener ButtonFeedback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hasConnection(requireContext())) {
                Restaurant restaurant = null;
                for (Restaurant restaurant_for : ApplicationModified.restaurantList) {
                    if (restaurant_for.getId() == id) {
                        restaurant = restaurant_for;
                    }
                }

                if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB)) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_FEEDBACKS_FROM_CARDS, restaurant);
                } else if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS)) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_RESTAURANT_FEEDBACKS_FROM_BOOKMARKS, restaurant);
                }
            }
            else  Toast.makeText(requireContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();

        }
    };

    private final View.OnClickListener ButtonRoute = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hasConnection(requireContext())) {
                Restaurant restaurant = null;
                for (Restaurant restaurant_for : ApplicationModified.restaurantList) {
                    if (restaurant_for.getId() == id) {
                        restaurant = restaurant_for;
                    }
                }

                if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB)) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_MAP_TAB_FROM_CARDS, restaurant);
                } else if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS)) {
                    callBackListener.onCallBack(Constants.ACTION_OPEN_MAP_TAB_FROM_BOOKMARKS, restaurant);
                }
            }
            else  Toast.makeText(requireContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();
        }
    };

    private final View.OnClickListener LikeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(hasConnection(requireContext()) && ApplicationModified.cardList.size() != 0) {

                Card card = null;
                for (Card mCard : ApplicationModified.cardList) {
                    if (mCard.getId() == id){
                        card = mCard;
                        break;
                    }
                }

                if (flag) {
                    like.setBackgroundResource(R.drawable.like_true);
                } else {
                    like.setBackgroundResource(R.drawable.like);
                }
                flag = !flag;

                //Log.d("test", card.getIsLike() + "1");

                if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB)) {
                    ApplicationModified.cardsViewModel.like(card);
                } else if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS)) {
                    ApplicationModified.cardsViewModel.likeFromBookmarks(card,
                            ApplicationModified.bookmarksViewModel, FragmentCards.getInstance().getRestaurantDao());
                }

                //Log.d("test", card.getIsLike() + "2");
            }
            else  Toast.makeText(requireContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();



        }
    };

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        return activeNW != null && activeNW.isConnected();
    }

    public FragmentRestaurantTab(){}
    public static FragmentRestaurantTab sInstance;
    public synchronized static FragmentRestaurantTab getInstance(){
        if (sInstance == null) {
            sInstance = new FragmentRestaurantTab();
        }
        return sInstance;
    }

    public void setName(String name){
        this.name = name;
    }


}
