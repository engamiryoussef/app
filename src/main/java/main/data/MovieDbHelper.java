package main.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmed on 23/09/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "popularmovie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_MOVIE = "CREATE TABLE " + MovieContracts.MOVIES_TABLE.TABLE_NAME + "(" +
                MovieContracts.MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                MovieContracts.MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieContracts.MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE_MOVIE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContracts.MOVIES_TABLE.TABLE_NAME);
        onCreate(db);

    }
}
