package bme.aut.hu.festivalnavigationandroid;

/**
 * Created by ben23 on 2018-02-14.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Class for storing the points of interests. Places like entrances, stages, toilets.
 */
public class InterestPoint extends Point implements Parcelable {

    private String name;
    private MyTime open;
    private MyTime close;
    private Category category;
    private String extras;

    public enum Category {
        ENTRANCE, STAGE, TOPUPPOINT, TOILET, CAMPING, FOOD, DRINK, SHOP, INFOPOINT;
    }

    public InterestPoint(LatLng location) {
        super(location);
    }

    public InterestPoint(LatLng location, String name, MyTime open, MyTime close, Category category, String extras) {
        super(location);
        this.name = name;
        this.open = open;
        this.close = close;
        this.category = category;
        this.extras = extras;
    }

    public String getName() {
        return name;
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

    public String getCategoryName() {
        return category.toString();
    }

    public String getExtras() {
        return extras;
    }

    /**
     * Method for determining if a place is open in the given time.
     *
     * @param calendar the time right now
     * @return TRUE if open, FALSE if closed
     */
    public boolean isOpen(Calendar calendar) {
        MyTime temp = new MyTime(calendar.HOUR_OF_DAY, calendar.MINUTE);
        if (open.compareTo(temp) <= 0 && close.compareTo(temp) == 1 || open.compareTo(temp) >= 0 && close.compareTo(temp) == 1 && open.compareTo(close) == 1)
            return true;
        else
            return false;
    }

    protected InterestPoint(Parcel in) {
        name = in.readString();
        open = (MyTime) in.readValue(MyTime.class.getClassLoader());
        close = (MyTime) in.readValue(MyTime.class.getClassLoader());
        category = (Category) in.readValue(Category.class.getClassLoader());
        extras = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(open);
        dest.writeValue(close);
        dest.writeValue(category);
        dest.writeString(extras);
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
