package main.manageDB;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import main.data.MovieContracts;
import main.models.Movie;
import main.moviesnews.ImageAdapter;

/**
 * Created by ahmed on 25/09/16.
 */
public class FetchFavouritesAsyncTask extends AsyncTask<Void, Void, List<Movie>>  {
    public ImageAdapter image;
    private static final String[] MOVIE_COLUMNS = {
            MovieContracts.MOVIES_TABLE._ID,
            MovieContracts.MOVIES_TABLE.COLUMN_TITLE,
            MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW,
            MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE,
            MovieContracts.MOVIES_TABLE.COLUMN_VOTE_AVERAGE,
            MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE
    };


    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_OVERVIEW = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_VOTE = 4;
    public static final int COL_DATE = 5;

    private Context mContext;
    private List<Movie> mMovies;

    public FetchFavouritesAsyncTask(Context context, ImageAdapter image,List<Movie> movies) {
        mContext = context;
        mMovies = movies;
        this.image=image;
    }

    private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        List<Movie> results = new ArrayList<>();
        mMovies.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setPoster(cursor.getString(COL_IMAGE));
                movie.setTitle(cursor.getString(COL_TITLE));
                movie.setDescription(cursor.getString(COL_OVERVIEW));
                movie.setRelease_date(cursor.getString(COL_DATE));
                movie.setRate(cursor.getFloat(COL_VOTE));
                movie.setId(cursor.getString(COL_ID));
                mMovies.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return mMovies;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MovieContracts.MOVIES_TABLE.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
        return getFavoriteMoviesDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null)
            if (movies.size() > 0) {
                image.updateAdapter(movies);
                image.notifyDataSetChanged();
            }
    }
}


