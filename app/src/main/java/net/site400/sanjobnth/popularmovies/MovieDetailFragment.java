package net.site400.sanjobnth.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {


    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            //String moviePoster = intent.getStringExtra(Intent.EXTRA_TEXT);
            Movie movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            TextView title = (TextView) rootView.findViewById(R.id.movie_detail_title);
            TextView releaseDate = (TextView) rootView.findViewById(R.id.movie_detail_release_date);
            TextView rating = (TextView) rootView.findViewById(R.id.movie_detail_rating);
            TextView synopsis = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_detail_image);
            //Picasso.with(getContext()).load(moviePoster).into(imageView);
            Picasso.with(getContext()).load(movie.posterUrl).into(imageView);
            title.setText(movie.orgTitle);
            releaseDate.setText(movie.releaseDate);
            rating.setText(String.valueOf(movie.rating));
            synopsis.setText(movie.synopsis);
        }
        return rootView;
    }

}
