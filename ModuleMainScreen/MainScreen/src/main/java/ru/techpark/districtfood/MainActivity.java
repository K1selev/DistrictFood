package ru.techpark.districtfood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.techpark.districtfood.MainScreen.CardsPreview.FragmentCards;

public class MainActivity extends AppCompatActivity implements CallBackListener{

    private BottomNavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                EditText location_filter_max = findViewById(R.id.location_filter_max);
                EditText middle_receipt_filter = findViewById(R.id.middle_receipt_filter);
                location_filter_max.clearFocus();
                middle_receipt_filter.clearFocus();
            }catch (Exception ignored){
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onCallBack(String ACTION, String name) {

        switch (ACTION) {
            case Constants.ACTION_OPEN_MAP_TAB: {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.NAME_RESTAURANT, name);

                navController.navigate(R.id.action_navigation_main_screen_to_navigation_map_route, bundle);
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_TAB: {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.NAME_RESTAURANT, name);

                navController.navigate(R.id.action_navigation_main_screen_to_navigation_card, bundle);
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.NAME_RESTAURANT, name);

                navController.navigate(R.id.action_navigation_bookmarks_to_navigation_card_from_bookmarks, bundle);
                break;
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

}