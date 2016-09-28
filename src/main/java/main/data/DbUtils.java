package main.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import main.models.Movie;

/**
 * Created by ahmed on 23/09/16.
 */
public final class DbUtils {
    public static ContentValues toContentValue(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContracts.MOVIES_TABLE._ID, movie.getId());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_TITLE, movie.getTitle());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW, movie.getDescription());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE, movie.getPoster());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE, movie.getRelease_date());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_VOTE_AVERAGE, movie.getRate());
        return movieValues;
    }

    public static boolean isFavourite(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContracts.MOVIES_TABLE.CONTENT_URI,
                null,   // projection
                MovieContracts.MOVIES_TABLE._ID + " = ?", // selection
                new String[]{id},   // selectionArgs
                null    // sort order
        );
        if (cursor != null) {
            int numRows = cursor.getCount();
            cursor.close();
            return (numRows > 0);
        }
        return false;
    }
}
