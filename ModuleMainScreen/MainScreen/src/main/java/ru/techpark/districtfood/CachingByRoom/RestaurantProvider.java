package ru.techpark.districtfood.CachingByRoom;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;

public class RestaurantProvider extends ContentProvider {

    //private static final String TAG = RestaurantProvider.class.getSimpleName();
    public static final String AUTHORITY = "techpark.ru.districtfood.restaurantprovider";
    public static final String TABLE_RESTAURANT = "restaurant";
    public static final int RESTAURANT_TABLE_CODE = 100;
    public static final int RESTAURANT_ROW_CODE = 101;

    private RestaurantDao mRestaurantDao;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_RESTAURANT, RESTAURANT_TABLE_CODE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_RESTAURANT + "/*", RESTAURANT_ROW_CODE);
    }



    public RestaurantProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {

        switch (URI_MATCHER.match(uri)) {
            case RESTAURANT_TABLE_CODE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_RESTAURANT;
            case RESTAURANT_ROW_CODE:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_RESTAURANT;
            default:
                throw new UnsupportedOperationException("not yet implemented");
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {

        if (getContext() != null) {
            mRestaurantDao = Room.databaseBuilder(getContext().getApplicationContext(), RestaurantsDatabase.class, "tabs_of_restaurants")
                    .build()
                    .getRestaurantDao();
            return true;
        }

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int code = URI_MATCHER.match(uri);

        if (code != RESTAURANT_ROW_CODE || code != RESTAURANT_TABLE_CODE) return null;

        Cursor cursor;

        if (code == RESTAURANT_TABLE_CODE) {
            cursor = mRestaurantDao.getRestaurantsCursor();
        } else {
            cursor = mRestaurantDao.getRestaurantWithIdCursor((int) ContentUris.parseId(uri));
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}