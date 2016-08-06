package net.site400.sanjobnth.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SANJOY on 8/2/2016.
 */
public class Movie implements Parcelable {

    String posterUrl;
    String orgTitle;
    String synopsis;
    double rating;
    String releaseDate;

    public Movie(String posterUrl, String orgTitle, String synopsis, double rating, String releaseDate) {
        this.posterUrl = posterUrl;
        this.orgTitle = orgTitle;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        posterUrl = in.readString();
        orgTitle = in.readString();
        synopsis = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
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
        parcel.writeString(orgTitle);
        parcel.writeString(synopsis);
        parcel.writeDouble(rating);
        parcel.writeString(releaseDate);
    }
}
