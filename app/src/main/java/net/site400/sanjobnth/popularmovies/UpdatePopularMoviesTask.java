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
public class UpdatePopularMoviesTask extends AsyncTask<Void, Void, String[]> {
    private MainActivityFragment mainActivityFragment;
    private final String LOG_TAG = UpdatePopularMoviesTask.class.getSimpleName();

    public UpdatePopularMoviesTask(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }

    private String[] getPopularMoviesDataFromJSON(String jsonData) throws JSONException {

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
    protected void onPostExecute(String[] posters) {
        String baseURL = "http://image.tmdb.org/t/p/w185/";
        if (posters != null) {
            mainActivityFragment.movieAdapter.clear();
            for (String poster : posters) {
                Log.v(LOG_TAG, baseURL + poster);
                mainActivityFragment.movieAdapter.add(
                        new Movie(baseURL + poster)
                );
            }
        }
    }
}
