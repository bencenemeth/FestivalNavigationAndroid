package bme.aut.hu.festivalnavigationandroid.model.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import bme.aut.hu.festivalnavigationandroid.model.InterestPoint;

/**
 * Created by ben23 on 2018-02-24.
 */

public class Map implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bounds")
    @Expose
    private Bounds bounds;
    @SerializedName("interests")
    @Expose
    private List<InterestPoint> interestPoints = null;

    private LatLngBounds latLngBounds;

    public Map() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public List<InterestPoint> getInterestPoints() {
        return interestPoints;
    }

    public void setInterestPoints(List<InterestPoint> interestPoints) {
        this.interestPoints = interestPoints;
    }

    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

    public void setLatLngBounds(LatLngBounds latLngBounds) {
        this.latLngBounds = latLngBounds;
    }

    public void create() {
        LatLng northeast = new LatLng(bounds.getTopLeft().getLat(), bounds.getBottomRight().getLon());
        LatLng southwest = new LatLng(bounds.getBottomRight().getLat(), bounds.getTopLeft().getLon());
        latLngBounds = new LatLngBounds(northeast, southwest);
    }

    /** PARCELABLE */

    protected Map(Parcel in) {
        id = in.readString();
        name = in.readString();
        bounds = (Bounds) in.readValue(Bounds.class.getClassLoader());
        if (in.readByte() == 0x01) {
            interestPoints = new ArrayList<InterestPoint>();
            in.readList(interestPoints, InterestPoint.class.getClassLoader());
        } else {
            interestPoints = null;
        }
        latLngBounds = (LatLngBounds) in.readValue(LatLngBounds.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeValue(bounds);
        if (interestPoints == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(interestPoints);
        }
        dest.writeValue(latLngBounds);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Map> CREATOR = new Parcelable.Creator<Map>() {
        @Override
        public Map createFromParcel(Parcel in) {
            return new Map(in);
        }

        @Override
        public Map[] newArray(int size) {
            return new Map[size];
        }
    };
}
