package main.moviesnews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import main.models.Movie;

/**
 * Created by ahmed on 13/08/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int count;
    List<Movie> movie;
    public ImageAdapter(Context c, List<Movie> movie) {
        mContext = c;
        this.movie=movie;
    }

    public int getCount() {
        return movie.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    public void updateAdapter( List<Movie> movie){
        this.movie=movie;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new SquareImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(320, 400));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (SquareImageView) convertView;
        }
        String url="http://image.tmdb.org/t/p/w185/";
        Picasso.with(mContext) //
                .load(url+movie.get(position).getPoster()) //
                .into(imageView);
        return imageView;
    }

}
