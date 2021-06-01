package ru.techpark.districtfood.RestaurantTab;

import android.annotation.SuppressLint;
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

    private View layout;
    private ImageView score_filter_star1;
    private ImageView score_filter_star2;
    private ImageView score_filter_star3;
    private ImageView score_filter_star4;
    private ImageView score_filter_star5;
    private int number_star = 0;
    private Button btnCancel, btnOk;

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

        score_filter_star1 = view.findViewById(R.id.icon_star_1);
        score_filter_star2 = view.findViewById(R.id.icon_star_2);
        score_filter_star3 = view.findViewById(R.id.icon_star_3);
        score_filter_star4 = view.findViewById(R.id.icon_star_4);
        score_filter_star5 = view.findViewById(R.id.icon_star_5);

        score_filter_star1.setOnClickListener(Filter_score);
        score_filter_star2.setOnClickListener(Filter_score);
        score_filter_star3.setOnClickListener(Filter_score);
        score_filter_star4.setOnClickListener(Filter_score);
        score_filter_star5.setOnClickListener(Filter_score);

        layout = view.findViewById(R.id.rate_restaurant);

        btnCancel = view.findViewById(R.id.cancel);
        btnOk = view.findViewById(R.id.ok);

        btnCancel.setOnClickListener(Button_Cancel);
        btnOk.setOnClickListener(Button_Ok);

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();

        id = arguments.getInt(Constants.ID);

        Restaurant restaurant = null;
        for (Restaurant restaurant_for: ApplicationModified.restaurantList) {
            if (restaurant_for.getId() == id) {
                restaurant = restaurant_for;
            }
        }


        name = arguments.getString(Constants.NAME_RESTAURANT);
        middle_receipt = arguments.getFloat(Constants.TEXT_MIDDLE_RECEIPT);
        score = restaurant.getScore();
        description = arguments.getString(Constants.DESCRIPTION);
        url = arguments.getString(Constants.URL);
        action = arguments.getString(Constants.ACTION_OPEN_RESTAURANT_TAB);

        text_name.setText(name);
        text_score.setText(String.valueOf(score));
        text_middle_receipt.setText(String.valueOf(middle_receipt));
        text_description.setText(description);

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
                    layout.setVisibility(View.VISIBLE);
                    RefreshFilterScore(0);
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

            if(ApplicationModified.restaurantList.size() != 0) {

                Restaurant restaurant = null;
                for (Restaurant mRestaurant : ApplicationModified.restaurantList) {
                    if (mRestaurant.getId() == id){
                        restaurant = mRestaurant;
                        break;
                    }
                }

                if (flag) {
                    like.setBackgroundResource(R.drawable.like_true);
                } else {
                    like.setBackgroundResource(R.drawable.like);
                }
                flag = !flag;

                if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB)) {
                    ApplicationModified.restaurantAllViewModel.like(ApplicationModified.restaurantDao,
                            ApplicationModified.cardList, restaurant);
                } else if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS)) {
                    ApplicationModified.bookmarksViewModel.like(ApplicationModified.restaurantDao,
                            ApplicationModified.cardList, restaurant);
                }


            }

        }
    };

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        return activeNW != null && activeNW.isConnected();
    }

    private final View.OnClickListener Filter_score = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.icon_star_1:
                    RefreshFilterScore(1);
                    break;
                case R.id.icon_star_2:
                    RefreshFilterScore(2);
                    break;
                case R.id.icon_star_3:
                    RefreshFilterScore(3);
                    break;
                case R.id.icon_star_4:
                    RefreshFilterScore(4);
                    break;
                case R.id.icon_star_5:
                    RefreshFilterScore(5);
                    break;
            }
        }
    };

    private void RefreshFilterScore(int number_star){
        score_filter_star1.setImageResource(R.drawable.icon_score_clear);
        score_filter_star2.setImageResource(R.drawable.icon_score_clear);
        score_filter_star3.setImageResource(R.drawable.icon_score_clear);
        score_filter_star4.setImageResource(R.drawable.icon_score_clear);
        score_filter_star5.setImageResource(R.drawable.icon_score_clear);
        switch (number_star) {
            case 0:
                this.number_star = 0;
                break;
            case 1:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                this.number_star = 1;
                break;
            case 2:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                this.number_star = 2;
                break;
            case 3:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                score_filter_star3.setImageResource(R.drawable.icons_score);
                this.number_star = 3;
                break;
            case 4:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                score_filter_star3.setImageResource(R.drawable.icons_score);
                score_filter_star4.setImageResource(R.drawable.icons_score);
                this.number_star = 4;
                break;
            case 5:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                score_filter_star3.setImageResource(R.drawable.icons_score);
                score_filter_star4.setImageResource(R.drawable.icons_score);
                score_filter_star5.setImageResource(R.drawable.icons_score);
                this.number_star = 5;
                break;
        }
    }

    private final View.OnClickListener Button_Cancel = new View.OnClickListener() {
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

                Restaurant restaurant = null;
                for (Restaurant mRestaurant : ApplicationModified.restaurantList) {
                    if (mRestaurant.getId() == id){
                        restaurant = mRestaurant;
                        break;
                    }
                }

                String text_feedback = String.valueOf(editText_feedback.getText());
                String feedback = "'" + text_feedback + "''" + "0" + "'" + "</1337/>";


                String feedbacks = restaurant.getFeedbacks();
                feedbacks += feedback;
                text_score.setText(String.valueOf(ConvertScoreCard(feedbacks)));

                restaurant.setScore(ConvertScoreCard(feedbacks));
                ApplicationModified.cardsViewModel.feedbacks(card, feedback);
                ApplicationModified.cardsViewModel.scores(card, feedbacks);

                if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB)) {
                    ApplicationModified.restaurantAllViewModel.feedbacks(ApplicationModified.restaurantDao,
                            ApplicationModified.cardList, restaurant, feedbacks);
                } else if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS)) {
                    ApplicationModified.bookmarksViewModel.feedbacks(ApplicationModified.restaurantDao,
                            ApplicationModified.cardList, restaurant, feedback);
                }
            }
            else  {
                Toast.makeText(requireContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();
            }

            layout.setVisibility(View.GONE);
            editText_feedback.setText("");
        }
    };

    private final View.OnClickListener Button_Ok = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(number_star != 0) {
                if(hasConnection(requireContext()) && ApplicationModified.cardList.size() != 0) {

                    Card card = null;
                    for (Card mCard : ApplicationModified.cardList) {
                        if (mCard.getId() == id){
                            card = mCard;
                            break;
                        }
                    }

                    Restaurant restaurant = null;
                    for (Restaurant mRestaurant : ApplicationModified.restaurantList) {
                        if (mRestaurant.getId() == id){
                            restaurant = mRestaurant;
                            break;
                        }
                    }

                    String text_feedback = String.valueOf(editText_feedback.getText());
                    String feedback = "'" + text_feedback + "''" + number_star + "'" + "</1337/>";

                    String feedbacks = restaurant.getFeedbacks();
                    feedbacks += feedback;
                    text_score.setText(String.valueOf(ConvertScoreCard(feedbacks)));

                    restaurant.setScore(ConvertScoreCard(feedbacks));
                    ApplicationModified.cardsViewModel.feedbacks(card, feedbacks);
                    ApplicationModified.cardsViewModel.scores(card, feedbacks);


                    if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB)) {
                        ApplicationModified.restaurantAllViewModel.feedbacks(ApplicationModified.restaurantDao,
                                ApplicationModified.cardList, restaurant, feedback);
                    } else if (action.equals(Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS)) {
                        ApplicationModified.bookmarksViewModel.feedbacks(ApplicationModified.restaurantDao,
                                ApplicationModified.cardList, restaurant, feedback);
                    }

                }
                else  {
                    Toast.makeText(requireContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();
                }

                layout.setVisibility(View.GONE);
                editText_feedback.setText("");
            }

        }
    };

    private float ConvertScoreCard(String feedbacks){

        float value = 0;
        float newScoreAll = 0;
        float newScore = 0;
        String[] strings = feedbacks.split("</1337/>");

        for (String string : strings) {

            char score = string.charAt(string.length() - 2);

            if (score != '0'){
                value++;
                newScoreAll += Integer.parseInt(String.valueOf(score));
            }
        }

        if(value != 0) {
            newScore = newScoreAll / value;
            double scale = Math.pow(10, 1);
            newScore = (float) (Math.ceil(newScore * scale) / scale);
        }

        return newScore;
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
