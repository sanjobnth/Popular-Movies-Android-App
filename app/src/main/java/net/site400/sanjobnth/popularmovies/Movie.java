package net.site400.sanjobnth.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SANJOY on 8/2/2016.
 */
public class Movie implements Parcelable {
    //int image;
    String posterUrl;

    public Movie(String posterUrl) {
        //this.image = image;
        this.posterUrl = posterUrl;
    }

    protected Movie(Parcel in) {
        posterUrl = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterUrl);
    }
}
