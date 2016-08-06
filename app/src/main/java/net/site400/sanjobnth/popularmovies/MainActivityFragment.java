package net.site400.sanjobnth.popularmovies;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;

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
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieList = new ArrayList<Movie> (Arrays.asList(movies));
        }
        else{
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh) {
            Log.v(LOG_TAG,"Refresh button clicked");
            updatePopularMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_main_activity, container, false);
        movieAdapter = new MovieAdapter(getActivity(), movieList);

        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(movieAdapter);
        return rootView;
    }

    private void updatePopularMovies(){
        UpdatePopularMoviesTask updatePopularMoviesTask = new UpdatePopularMoviesTask();
        updatePopularMoviesTask.execute();
    }


    public class UpdatePopularMoviesTask extends AsyncTask <Void, Void, String[]> {
        private final String LOG_TAG = UpdatePopularMoviesTask.class.getSimpleName();

        private String[] getPopularMoviesDataFromJSON(String jsonData) throws JSONException{

            JSONObject moviesJSON = new JSONObject(jsonData);
            JSONArray movies = moviesJSON.getJSONArray("results");
            String[] relativePathPosters = new String[movies.length()];
            for (int i = 0; i < movies.length(); i++) {
                relativePathPosters[i] = movies.getJSONObject(i).getString("poster_path");
            }
            return relativePathPosters;
        }


        @Override
        protected String[] doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String popularMoviesJsonStr = null;


            try {
                Uri builtUri = Uri.parse("http://api.themoviedb.org/3/movie/popular?").buildUpon()
                        .appendQueryParameter("api_key", BuildConfig.MY_THEMOVIEDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popularMoviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                //e.printStackTrace();
                return null;
            }finally {
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

            try {
               return getPopularMoviesDataFromJSON(popularMoviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] posters) {
            String baseURL = "http://image.tmdb.org/t/p/w185/";
            if(posters != null){
                movieAdapter.clear();
                for(String poster: posters){
                    Log.v(LOG_TAG, baseURL + poster);
                    movieAdapter.add(
                            new Movie(baseURL + poster)
                    );
                }
            }
        }
    }

}
