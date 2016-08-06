package net.site400.sanjobnth.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SANJOY on 8/6/2016.
 */
public class UpdatePopularMoviesTask extends AsyncTask<Void, Void, Movie[]> {
    private MainActivityFragment mainActivityFragment;
    private final String LOG_TAG = UpdatePopularMoviesTask.class.getSimpleName();

    public UpdatePopularMoviesTask(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }

    private Movie[] getPopularMoviesDataFromJSON(String jsonData) throws JSONException {

        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";
        final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
        JSONObject moviesJSON = new JSONObject(jsonData);
        JSONArray movies = moviesJSON.getJSONArray("results");
        //String relativePathPosters = new String[movies.length()];
        Movie[] moviesArray = new Movie[movies.length()];
        JSONObject movieJSON;
        for (int i = 0; i < movies.length(); i++) {
            //relativePathPosters[i] = movies.getJSONObject(i).getString(POSTER_PATH);
            movieJSON = movies.getJSONObject(i);
            moviesArray[i] = new Movie(
                    BASE_URL + movieJSON.getString(POSTER_PATH),
                    movieJSON.getString(ORIGINAL_TITLE),
                    movieJSON.getString(OVERVIEW),
                    movieJSON.getDouble(VOTE_AVERAGE),
                    movieJSON.getString(RELEASE_DATE)
            );
        }
        return moviesArray;
    }


    @Override
    protected Movie[] doInBackground(Void... voids) {

        final String API_KEY = "api_key";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String popularMoviesJsonStr = null;


        try {
            Uri builtUri = Uri.parse("http://api.themoviedb.org/3/movie/popular?").buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MY_THEMOVIEDB_API_KEY)
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

        try {
            return getPopularMoviesDataFromJSON(popularMoviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if (movies != null) {
            mainActivityFragment.movieAdapter.clear();
            for (Movie movie : movies) {
                Log.v(LOG_TAG, movie.posterUrl);
                mainActivityFragment.movieAdapter.add(movie);
            }
        }
    }
}
