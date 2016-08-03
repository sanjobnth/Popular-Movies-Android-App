package net.site400.sanjobnth.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by SANJOY on 8/2/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {


    public MovieAdapter(Context context, List<Movie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
        //imageView.setImageResource(movie.image);
        Picasso.with(getContext()).load(movie.posterUrl).into(imageView);

        return convertView;
    }
}
