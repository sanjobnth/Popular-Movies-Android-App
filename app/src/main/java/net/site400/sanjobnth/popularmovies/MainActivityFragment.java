package net.site400.sanjobnth.popularmovies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private MovieAdapter movieAdapter;
    private Movie[] movies = {
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png"),
            new Movie("http://i.imgur.com/DvpvklR.png")
    };

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh)
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_main_activity, container, false);
        movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(movies));

        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(movieAdapter);
        return rootView;
    }

}
