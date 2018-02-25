package bme.aut.hu.festivalnavigationandroid.model.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ben23 on 2018-02-25.
 */

public class TopLeft {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;

    /**
     * No args constructor for use in serialization
     *
     */
    public TopLeft() {
    }

    /**
     *
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
}
