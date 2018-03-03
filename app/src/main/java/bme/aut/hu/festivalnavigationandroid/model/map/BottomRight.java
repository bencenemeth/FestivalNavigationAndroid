package bme.aut.hu.festivalnavigationandroid.model.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ben23 on 2018-02-25.
 */

public class BottomRight implements Parcelable {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;

    /**
     * No args constructor for use in serialization
     */
    public BottomRight() {
    }

    /**
     * @param lon
     * @param lat
     */
    public BottomRight(double lat, double lon) {
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

    protected BottomRight(Parcel in) {
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
    public static final Parcelable.Creator<BottomRight> CREATOR = new Parcelable.Creator<BottomRight>() {
        @Override
        public BottomRight createFromParcel(Parcel in) {
            return new BottomRight(in);
        }

        @Override
        public BottomRight[] newArray(int size) {
            return new BottomRight[size];
        }
    };
}
