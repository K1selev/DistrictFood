package ru.techpark.districtfood;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.techpark.districtfood.MainScreen.FragmentMainScreen;
import ru.techpark.districtfood.MainScreen.Search.FragmentSearch;
import ru.techpark.districtfood.Map.FragmentMap;
import ru.techpark.districtfood.Map.MapAction;
import ru.techpark.districtfood.Panel.FragmentPanel;
import ru.techpark.districtfood.RestaurantTab.FragmentRestaurantTab;

public class MainActivity extends AppCompatActivity implements CallBackListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_main_screen) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_main_screen, FragmentMainScreen.getInstance())
                    .commit();
        }
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_panel) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_panel, new FragmentPanel())
                    .commit();
        }

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (ACTION) {
            case Constants.ACTION_OPEN_MAP_TAB: {
                Fragment main_screen_fragment = fragmentManager.findFragmentById(R.id.fragment_main_screen);

                if (main_screen_fragment != null && main_screen_fragment.isAdded()) {
                    transaction.remove(main_screen_fragment);
                    transaction.add(R.id.fragment_map, FragmentMap.getInstance(MapAction.ACTION_ROUTE_OF_RESTAURANT, name));
                }

                transaction.addToBackStack(null);
                transaction.commit();
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_TAB: {
                Fragment main_screen_fragment = fragmentManager.findFragmentById(R.id.fragment_main_screen);

                if (main_screen_fragment != null && main_screen_fragment.isAdded()) {
                    transaction.remove(main_screen_fragment);
                    transaction.add(R.id.fragment_restaurant_tab, FragmentRestaurantTab.getInstance());
                    FragmentRestaurantTab.getInstance().setName(name);
                }

                transaction.addToBackStack(null);
                transaction.commit();
                break;
            }
            case Constants.ACTION_OPEN_RESTAURANT_TAB_FROM_BOOKMARKS:
                Fragment bookmarks_fragment = fragmentManager.findFragmentById(R.id.fragment_bookmarks);

                if (bookmarks_fragment != null && bookmarks_fragment.isAdded()) {
                    transaction.remove(bookmarks_fragment);
                    transaction.add(R.id.fragment_restaurant_tab, FragmentRestaurantTab.getInstance());
                    FragmentRestaurantTab.getInstance().setName(name);
                }

                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
    /*
    BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_bookmarks, R.id.navigation_main_screen, R.id.navigation_map, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    <androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:weightSum="100">

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/nav_view"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintVertical_weight="@integer/weight_panel"
        android:layout_marginStart="0dp"
        android:layout_marginEnd="0dp"
        android:background="@color/orange"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/bottom_nav_menu" />

    <fragment
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:layout_constraintVertical_weight="@integer/weight_main_screen"
        app:layout_constraintBottom_toTopOf="@id/nav_view"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/mobile_navigation" />

</androidx.constraintlayout.widget.ConstraintLayout>
    * */
}