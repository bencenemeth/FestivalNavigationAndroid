package bme.aut.hu.festivalnavigationandroid.model.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ben23 on 2018-02-25.
 */

public class TopLeft implements Parcelable {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;

    /**
     * No args constructor for use in serialization
     */
    public TopLeft() {
    }

    /**
     * @param lon
     * @param lat
     */
    public TopLeft(double lat, double lon) {
        super();
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * PARCELABLE
     */

    protected TopLeft(Parcel in) {
        lat = in.readDouble();
        lon = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TopLeft> CREATOR = new Parcelable.Creator<TopLeft>() {
        @Override
        public TopLeft createFromParcel(Parcel in) {
            return new TopLeft(in);
        }

        @Override
        public TopLeft[] newArray(int size) {
            return new TopLeft[size];
        }
    };
}
