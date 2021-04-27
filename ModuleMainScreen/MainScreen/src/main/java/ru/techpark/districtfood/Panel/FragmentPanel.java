package ru.techpark.districtfood.Panel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.techpark.districtfood.Bookmarks.FragmentBookmarks;
import ru.techpark.districtfood.MainScreen.CardsPreview.CardsAdapter;
import ru.techpark.districtfood.MainScreen.FragmentMainScreen;
import ru.techpark.districtfood.MainScreen.Search.FragmentSearch;
import ru.techpark.districtfood.Map.FragmentMap;
import ru.techpark.districtfood.Map.MapAction;
import ru.techpark.districtfood.Map.MapViewModel;
import ru.techpark.districtfood.Profile.FragmentProfile;
import ru.techpark.districtfood.R;

public class FragmentPanel extends Fragment {

    private ImageButton BookMarks;
    private ImageButton Profile;
    private ImageButton MainScreen;
    private ImageButton Map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BookMarks = view.findViewById(R.id.bookmarks);
        BookMarks.setOnClickListener(BookMarks_OnClick);

        Profile = view.findViewById(R.id.person);
        Profile.setOnClickListener(Profile_OnClick);

        MainScreen = view.findViewById(R.id.main_screen);
        MainScreen.setOnClickListener(MainScreen_OnClick);

        Map = view.findViewById(R.id.map);
        Map.setOnClickListener(Map_OnClick);

    }

    private final View.OnClickListener BookMarks_OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Fragment profile_fragment = fragmentManager.findFragmentById(R.id.fragment_profile);
            Fragment main_screen_fragment = fragmentManager.findFragmentById(R.id.fragment_main_screen);
            Fragment map_fragment = fragmentManager.findFragmentById(R.id.fragment_map);
            Fragment bookmarks_fragment = fragmentManager.findFragmentById(R.id.fragment_bookmarks);

            if (profile_fragment != null && profile_fragment.isAdded()) {
                transaction.remove(profile_fragment);
            } else if (main_screen_fragment != null && main_screen_fragment.isAdded()) {
                transaction.remove(main_screen_fragment);
            } else if (map_fragment != null && map_fragment.isAdded()) {
                transaction.remove(map_fragment);
            } else if (bookmarks_fragment != null && bookmarks_fragment.isAdded()) {
                transaction.remove(bookmarks_fragment);
            }
            transaction.add(R.id.fragment_bookmarks, new FragmentBookmarks());

            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
    private final View.OnClickListener Profile_OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Fragment bookmarks_fragment = fragmentManager.findFragmentById(R.id.fragment_bookmarks);
            Fragment main_screen_fragment = fragmentManager.findFragmentById(R.id.fragment_main_screen);
            Fragment map_fragment = fragmentManager.findFragmentById(R.id.fragment_map);
            Fragment profile_fragment = fragmentManager.findFragmentById(R.id.fragment_profile);

            if (bookmarks_fragment != null && bookmarks_fragment.isAdded()) {
                transaction.remove(bookmarks_fragment);
            } else if (main_screen_fragment != null && main_screen_fragment.isAdded()) {
                transaction.remove(main_screen_fragment);
            } else if (map_fragment != null && map_fragment.isAdded()) {
                transaction.remove(map_fragment);
            } else if (profile_fragment != null && profile_fragment.isAdded()) {
                transaction.remove(profile_fragment);
            }
            transaction.add(R.id.fragment_profile, new FragmentProfile());

            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
    private final View.OnClickListener MainScreen_OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Fragment profile_fragment = fragmentManager.findFragmentById(R.id.fragment_profile);
            Fragment bookmarks_fragment = fragmentManager.findFragmentById(R.id.fragment_bookmarks);
            Fragment map_fragment = fragmentManager.findFragmentById(R.id.fragment_map);
            Fragment main_screen_fragment = fragmentManager.findFragmentById(R.id.fragment_map);

            if (profile_fragment != null && profile_fragment.isAdded()) {
                transaction.remove(profile_fragment);
            } else if (bookmarks_fragment != null && bookmarks_fragment.isAdded()) {
                transaction.remove(bookmarks_fragment);
            } else if (map_fragment != null && map_fragment.isAdded()) {
                transaction.remove(map_fragment);
            } else if (main_screen_fragment != null && main_screen_fragment.isAdded()) {
                transaction.remove(main_screen_fragment);
            }
            transaction.add(R.id.fragment_main_screen, new FragmentMainScreen());

            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
    private final View.OnClickListener Map_OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Fragment profile_fragment = fragmentManager.findFragmentById(R.id.fragment_profile);
            Fragment main_screen_fragment = fragmentManager.findFragmentById(R.id.fragment_main_screen);
            Fragment bookmarks_fragment = fragmentManager.findFragmentById(R.id.fragment_bookmarks);
            Fragment map_fragment = fragmentManager.findFragmentById(R.id.fragment_map);

            if (profile_fragment != null && profile_fragment.isAdded()) {
                transaction.remove(profile_fragment);
            } else if (main_screen_fragment != null && main_screen_fragment.isAdded()) {
                transaction.remove(main_screen_fragment);
            } else if (bookmarks_fragment != null && bookmarks_fragment.isAdded()) {
                transaction.remove(bookmarks_fragment);
            } else if (map_fragment != null && map_fragment.isAdded()) {
                transaction.remove(map_fragment);
            }
            Log.d("test", "ssdsdsds");
            transaction.add(R.id.fragment_map, FragmentMap.getInstance(MapAction.ACTION_RESTAURANT_AROUND, ""));

            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    @SuppressLint("StaticFieldLeak")
    public static FragmentPanel sInstance;
    public FragmentPanel() {

    }
    public synchronized static FragmentPanel getInstance(){
        if (sInstance == null) {
            sInstance = new FragmentPanel();
        }
        return sInstance;
    }

}
