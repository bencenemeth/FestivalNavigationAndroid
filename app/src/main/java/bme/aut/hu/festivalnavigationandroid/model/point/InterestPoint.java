package bme.aut.hu.festivalnavigationandroid.model.point;

/**
 * Created by ben23 on 2018-02-14.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.MyTime;

/**
 * Class for storing the points of interests. Places like entrances, stages, toilets.
 */
public class InterestPoint implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("open_now")
    @Expose
    private boolean openNow;

    private MyTime open;
    private MyTime close;
    private Category category;
    private LatLng location;


    public enum Category {
        // TODO: IMPLEMENT OTHERS
        //ENTRANCE, STAGE, TOPUPPOINT, TOILET, CAMPING, FOOD, DRINK, SHOP, INFOPOINT;
        Stage("Stage", R.drawable.ic_tv_black_24dp),
        Infopoint("Infopoint", R.drawable.ic_info_black_24dp),
        Food("Food", R.drawable.ic_local_pizza_black_24dp),
        Entrance("Entrance", R.drawable.ic_open_in_browser_black_24dp);

        // TODO: REVIEW CHANGE TO STRING NAME TO INT ID
        private String name;
        private int imageID;

        Category(String name, int imageID) {
            this.name = name;
            this.imageID = imageID;
        }

        public int getImageID() {
            return imageID;
        }

        public String getName() {
            return name;
        }
    }

    public InterestPoint() {
    }

    // TODO: REMOVE WHEN API IS READY
    public InterestPoint(String id, double lat, double lon, String name, MyTime open, MyTime close, Category category, String description) {
        this.id = id;
        this.location = new LatLng(lat, lon);
        this.name = name;
        this.open = open;
        this.close = close;
        this.category = category;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public MyTime getOpen() {
        return open;
    }

    public MyTime getClose() {
        return close;
    }

    public Category getCategory() {
        return category;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public void setOpen(MyTime open) {
        this.open = open;
    }

    public void setClose(MyTime close) {
        this.close = close;
    }

    // TODO
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void create() {
        switch (type) {
            case "stage":
                this.category = Category.Stage;
                break;
            case "infopoint":
                this.category = Category.Infopoint;
                break;
            case "entrance":
                this.category = Category.Entrance;
                break;
            case "food":
                this.category = Category.Food;
                break;
            default:
                this.category = Category.Stage;
                break;
        }
        this.location = new LatLng(lat, lon);
    }

    /**
     * PARCELABLE
     */

    protected InterestPoint(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        description = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        openNow = in.readByte() != 0x00;
        open = (MyTime) in.readValue(MyTime.class.getClassLoader());
        close = (MyTime) in.readValue(MyTime.class.getClassLoader());
        category = (Category) in.readValue(Category.class.getClassLoader());
        location = (LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeByte((byte) (openNow ? 0x01 : 0x00));
        dest.writeValue(open);
        dest.writeValue(close);
        dest.writeValue(category);
        dest.writeValue(location);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<InterestPoint> CREATOR = new Parcelable.Creator<InterestPoint>() {
        @Override
        public InterestPoint createFromParcel(Parcel in) {
            return new InterestPoint(in);
        }

        @Override
        public InterestPoint[] newArray(int size) {
            return new InterestPoint[size];
        }
    };
}
