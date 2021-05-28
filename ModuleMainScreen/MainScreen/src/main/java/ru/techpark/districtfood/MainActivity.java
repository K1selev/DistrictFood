package ru.techpark.districtfood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.MainScreen.CardsPreview.FragmentCards;

public class MainActivity extends AppCompatActivity implements CallBackListener{

    private BottomNavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationModified.context123 = this.getApplicationContext();

        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_bookmarks, R.id.navigation_main_screen,
                R.id.navigation_map, R.id.navigation_profile).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    protected void onResume() {
        super.onResume();
        navView.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(MenuItem item) {

                        if (item.getItemId() == R.id.navigation_main_screen &&
                                ApplicationModified.enabled_recyclerView) {
                            FragmentCards.getInstance().scrollToTop();
                        }

                    }
                });
    }

    //при нажатии на область вне клавиатуры, клавиатура закрывается и удаляется курсор
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() ==  MotionEvent.ACTION_DOWN){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            try {
                EditText search = findViewById(R.id.search);
                search.clearFocus();
            }catch (Exception ignored){
            }
            try {
                EditText location_filter_max = findViewById(R.id.location_filter_max);
                location_filter_max.clearFocus();
            }catch (Exception ignored){
            }
            try {
                EditText middle_receipt_filter = findViewById(R.id.middle_receipt_filter);
                middle_receipt_filter.clearFocus();
            }catch (Exception ignored){
            }
            try {
                EditText feedback = findViewById(R.id.feedback);
                feedback.clearFocus();
            }catch (Exception ignored){
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onCallBack(String ACTION, Restaurant restaurant) {

        switch (ACTION) {
            case Constants.ACTION_OPEN_MAP_TAB: {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ID, restaurant.getId());

                navController.navigate(R.id.action_navigation_main_screen_to_navigation_map_route, bundle);
                break;
            }
            case Constants.ACTION_OPEN_MAP_TAB_FROM_CARDS: {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ID, restaurant.getId());

                navController.navigate(R.id.action_navigation_card_to_navigation_map_route, bundle);
                break;
            }
            case Constants.ACTION_OPEN_MAP_TAB_FROM_BOOKMARKS: {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ID, restaurant.getId());

                navController.navigate(R.id.action_navigation_card_from_bookmarks_to_navigation_map_route, bundle);
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_TAB: {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.NAME_RESTAURANT, restaurant.getName());
                bundle.putString(Constants.DESCRIPTION, restaurant.getZ_description());
                bundle.putFloat(Constants.TEXT_MIDDLE_RECEIPT, restaurant.getMiddleReceipt());
                bundle.putFloat(Constants.SCORE, restaurant.getScore());
                bundle.putString(Constants.URL, restaurant.getUrlImage());
                bundle.putInt(Constants.ID, restaurant.getId());
                bundle.putString(Constants.ACTION_OPEN_RESTAURANT_TAB, Constants.ACTION_OPEN_RESTAURANT_TAB);

                navController.navigate(R.id.action_navigation_main_screen_to_navigation_card, bundle);
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS: {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.NAME_RESTAURANT, restaurant.getName());
                bundle.putString(Constants.DESCRIPTION, restaurant.getZ_description());
                bundle.putFloat(Constants.TEXT_MIDDLE_RECEIPT, restaurant.getMiddleReceipt());
                bundle.putFloat(Constants.SCORE, restaurant.getScore());
                bundle.putString(Constants.URL, restaurant.getUrlImage());
                bundle.putInt(Constants.ID, restaurant.getId());
                bundle.putString(Constants.ACTION_OPEN_RESTAURANT_TAB, Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS);

                navController.navigate(R.id.action_navigation_bookmarks_to_navigation_card_from_bookmarks, bundle);
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_FEEDBACKS_FROM_CARDS: {

                Bundle bundle = new Bundle();

                navController.navigate(R.id.action_navigation_card_to_fragmentFeedbacks_for_cards, bundle);
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_FEEDBACKS_FROM_BOOKMARKS: {

                Bundle bundle = new Bundle();

                navController.navigate(R.id.action_navigation_card_from_bookmarks_to_fragmentFeedbacks_for_bookmarks, bundle);
                break;
            }
        }

    }

    @Override
    public void onCallBack(String ACTION, Bundle bundle){
        switch (ACTION){
            case Constants.ACTION_EDIT_PROFILE_TO_PROFILE: {
                navController.navigate(R.id.action_fragmentEditProfile_to_fragmentProfile);
                break;
            }
            case Constants.ACTION_LOGIN_TO_PROFILE: {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentProfile);
                break;
            }
            case Constants.ACTION_LOGIN_TO_REGISTER: {
                navController.navigate(R.id.action_fragmentLogin_to_navigation_profile);
                break;
            }
            case Constants.ACTION_PROFILE_TO_EDIT_PROFILE: {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentEditProfile);
                break;
            }
            case Constants.ACTION_PROFILE_TO_LOGIN: {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentLogin);
                break;
            }
            case Constants.ACTION_REGISTER_TO_PROFILE: {
                navController.navigate(R.id.action_navigation_profile_to_fragmentProfile);
                break;
            }
            case Constants.ACTION_REGISTER_TO_LOGIN: {
                navController.navigate(R.id.action_navigation_profile_to_fragmentLogin);
                break;
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

}