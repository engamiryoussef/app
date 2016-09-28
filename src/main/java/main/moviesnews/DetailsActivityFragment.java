package main.moviesnews;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import main.manageDB.ManageFavouritesAsyncTask;
import main.models.Movie;
import main.models.Reviews;
import main.models.Trailer;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    List<Trailer> trailerList;
    List<Reviews> reviewsList;
    LinearLayout trailerContainer;
    LinearLayout reviewContainer;
    LinearLayout containerBtn;
    String forecastJsonStr = null;
    TextView txt;
    Button button;
    Button favButton;
    Movie movie;

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = (Movie) getArguments().getSerializable("movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        favButton = (Button) rootView.findViewById(R.id.addToFavourate);
        FetchTrailerTask fetchTrailerTask = new FetchTrailerTask();
        String params[] = new String[2];
        params[0] = movie.getId();
        fetchTrailerTask.execute(params);
        String url = "http://image.tmdb.org/t/p/w185/";
        ((TextView) rootView.findViewById(R.id.movie_title))
                .setText(movie.getTitle());
        ((TextView) rootView.findViewById(R.id.desc))
                .setText(movie.getDescription());
        ((TextView) rootView.findViewById(R.id.rleaseDate))
                .setText(movie.getRelease_date());
        ((RatingBar) rootView.findViewById(R.id.ratingBar))
                .setRating(movie.getRate() / 2);
        ImageView image = ((ImageView) rootView.findViewById(R.id.poster));
        Picasso.with(getContext()) //
                .load(url + movie.getPoster()) //
                .into(image);
        new ManageFavouritesAsyncTask(getActivity(), movie, false, favButton).execute();

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ManageFavouritesAsyncTask(getActivity(), movie, true, favButton).execute();
            }
        });
        trailerContainer = (LinearLayout) rootView.findViewById(R.id.trailersbuttons);
        reviewContainer = (LinearLayout) rootView.findViewById(R.id.reviews);
//        }
        return rootView;
    }

    public static DetailsActivityFragment newInstance(Movie movie) {
        DetailsActivityFragment detailsActivityFragment = new DetailsActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        detailsActivityFragment.setArguments(args);
        return detailsActivityFragment;
    }

    public void updateBtnList() {
        if (trailerList != null)
            for (Trailer trailerVar : trailerList) {
                containerBtn = new LinearLayout(this.getContext());
                containerBtn.setOrientation(LinearLayout.HORIZONTAL);
                txt = new TextView(this.getContext());
                button = new Button(this.getContext());
                button.setBackgroundResource(R.drawable.play);
                button.setOnClickListener(view -> {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerVar.getKey())));
                });
                txt.setText(trailerVar.getName());
                containerBtn.addView(button);
                containerBtn.addView(txt);
                trailerContainer.addView(containerBtn);
            }
    }

    public void updateReviewList() {
        for (Reviews reviews : reviewsList) {
            txt = new TextView(this.getContext());
            txt.setText(reviews.getAuthor() + ":-");
            reviewContainer.addView(txt);
            txt = new TextView(this.getContext());
            txt.setText(reviews.getContent());
            reviewContainer.addView(txt);
            txt = new TextView(this.getContext());
            txt.setText("___________________________________");
            reviewContainer.addView(txt);
        }
    }

    public class FetchTrailerTask extends AsyncTask<String, Void, List<Trailer>> {
        private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();

        private List<Trailer> getTrailerDataFromJson(String trailerJsonStr)
                throws JSONException {
            final String OWM_LIST = "results";
            final String OWM_LANGUAGE = "iso_639_1";
            final String OWM_COUNTRY = "iso_3166_1";
            final String OWM_KEY = "key";
            final String OWM_NAME = "name";
            final String OWM_SITE = "site";
            final String OWM_SIZE = "size";
            final String OWM_TYPE = "type";
            final String OWM_ID = "id";
            JSONObject forecastJson = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = forecastJson.getJSONArray(OWM_LIST);
            Trailer trailer_var;
            List<Trailer> result = new ArrayList<>();
            for (int i = 0; i < trailerArray.length(); i++) {
                trailer_var = new Trailer();
                JSONObject trailer = trailerArray.getJSONObject(i);
                trailer_var.setId(trailer.getString(OWM_ID));
                trailer_var.setCountry(trailer.getString(OWM_COUNTRY));
                trailer_var.setKey(trailer.getString(OWM_KEY));
                trailer_var.setLanguage(trailer.getString(OWM_LANGUAGE));
                trailer_var.setName(trailer.getString(OWM_NAME));
                trailer_var.setQuaility(trailer.getString(OWM_SIZE));
                trailer_var.setSite(trailer.getString(OWM_SITE));
                trailer_var.setType(trailer.getString(OWM_TYPE));
                result.add(trailer_var);
            }
            for (Trailer s : result) {
                Log.v(LOG_TAG, "Movie entry: " + s.toString());
            }
            return result;
        }

        private List<Reviews> getReviewDataFromJson(String reviewsJsonStr)
                throws JSONException {
            final String OWM_LIST = "results";
            final String OWM_AURTHOR = "author";
            final String OWM_CONTENT = "content";
            JSONObject forecastJson = new JSONObject(reviewsJsonStr);
            JSONArray reviews_Array = forecastJson.getJSONArray(OWM_LIST);
            Reviews reviews_var;
            List<Reviews> result = new ArrayList<>();
            for (int i = 0; i < reviews_Array.length(); i++) {
                reviews_var = new Reviews();
                JSONObject reviews = reviews_Array.getJSONObject(i);
                reviews_var.setAuthor(reviews.getString(OWM_AURTHOR));
                reviews_var.setContent(reviews.getString(OWM_CONTENT));
                result.add(reviews_var);
            }
            for (Reviews s : result) {
                Log.v(LOG_TAG, "Movie entry: " + s.toString());
            }
            return result;
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {
            callWebservice(params[0], 1);
            callWebservice(params[0], 2);
            return trailerList;
        }

        public void callWebservice(String params, int type) {
            HttpURLConnection urlConnection = null;
            HttpURLConnection urlConnection2 = null;
            BufferedReader reader = null;
            try {
                //region
                String BASE_URL;
                if (type == 1)
                    BASE_URL =
                            "http://api.themoviedb.org/3/movie/" + params + "/videos?";
                else
                    BASE_URL =
                            "http://api.themoviedb.org/3/movie/" + params + "/reviews?";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIES_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return;
                }
                forecastJsonStr = buffer.toString();
                if (type == 1)
                    trailerList = getTrailerDataFromJson(forecastJsonStr);
                else
                    reviewsList = getReviewDataFromJson(forecastJsonStr);
                Log.e(LOG_TAG, forecastJsonStr);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                return;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null)
                updateBtnList();
            if (reviewsList != null)
                updateReviewList();
        }
    }

}
