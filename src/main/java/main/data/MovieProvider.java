package main.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ahmed on 23/09/16.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbHelper dbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContracts.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContracts.MOVIES_TABLE.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MovieContracts.MOVIES_TABLE.TABLE_NAME + "/#", MOVIE_WITH_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        String type;
        switch (match) {
            case MOVIE:
                type = MovieContracts.MOVIES_TABLE.CONTENT_TYPE;
                break;
            case MOVIE_WITH_ID:
                type = MovieContracts.MOVIES_TABLE.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return type;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                retCursor = dbHelper.getReadableDatabase().query(
                        MovieContracts.MOVIES_TABLE.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                String movieIdSelection = MovieContracts.MOVIES_TABLE.TABLE_NAME + "." + MovieContracts.MOVIES_TABLE._ID + "= ?";
                String[] movieSelectionArgs = new String[]{MovieContracts.MOVIES_TABLE.getMovieIDFromUri(uri)};
                retCursor = dbHelper.getReadableDatabase().query(
                        MovieContracts.MOVIES_TABLE.TABLE_NAME,
                        projection,
                        movieIdSelection,
                        movieSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException();

        }
        if (getContext() != null && getContext().getContentResolver() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (match) {
            case MOVIE:
                long movieId = db.insert(MovieContracts.MOVIES_TABLE.TABLE_NAME, null, values);
                if (movieId != -1) {
                    returnUri = MovieContracts.MOVIES_TABLE.buildMovieUri(movieId);
                } else {
                    returnUri = null;
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(MovieContracts.MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            if (getContext() != null && getContext().getContentResolver() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        db.close();
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContracts.MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            if (getContext() != null && getContext().getContentResolver() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        db.close();
        return rowsUpdated;
    }

}
