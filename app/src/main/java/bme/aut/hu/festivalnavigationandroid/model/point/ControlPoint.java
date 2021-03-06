package bme.aut.hu.festivalnavigationandroid.model.point;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ben23 on 2018-02-14.
 */

/**
 * Class for storing the locations of points.
 */
public class ControlPoint implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;

    private LatLng latlng;

    private Location location;


    public ControlPoint() {
    }

    public ControlPoint(String id, double lat, double lon) {
        this.id = id;
        this.latlng = new LatLng(lat, lon);
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public LatLng getLatLng() {
        return latlng;
    }

    public Location getLocation() {
        return location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLatLng() {
        this.latlng = new LatLng(lat, lon);
    }

    public void setLocation() {
        this.location = new Location("");
        this.location.setLatitude(lat);
        this.location.setLongitude(lon);
    }

    /**
     * PARCELABLE
     */

    protected ControlPoint(Parcel in) {
        id = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        latlng = (LatLng) in.readValue(LatLng.class.getClassLoader());
        location = (Location) in.readValue(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeValue(latlng);
        dest.writeValue(location);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ControlPoint> CREATOR = new Parcelable.Creator<ControlPoint>() {
        @Override
        public ControlPoint createFromParcel(Parcel in) {
            return new ControlPoint(in);
        }

        @Override
        public ControlPoint[] newArray(int size) {
            return new ControlPoint[size];
        }
    };
}