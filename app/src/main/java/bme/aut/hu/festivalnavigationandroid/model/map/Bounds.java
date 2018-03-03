package bme.aut.hu.festivalnavigationandroid.model.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ben23 on 2018-02-25.
 */

public class Bounds implements Parcelable {

    @SerializedName("top_left")
    @Expose
    private TopLeft topLeft;
    @SerializedName("bottom_right")
    @Expose
    private BottomRight bottomRight;

    /**
     * No args constructor for use in serialization
     */
    public Bounds() {
    }

    /**
     * @param bottomRight
     * @param topLeft
     */
    public Bounds(TopLeft topLeft, BottomRight bottomRight) {
        super();
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public TopLeft getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(TopLeft topLeft) {
        this.topLeft = topLeft;
    }

    public BottomRight getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(BottomRight bottomRight) {
        this.bottomRight = bottomRight;
    }

    /**
     * PARCELABLE
     */

    protected Bounds(Parcel in) {
        topLeft = (TopLeft) in.readValue(TopLeft.class.getClassLoader());
        bottomRight = (BottomRight) in.readValue(BottomRight.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(topLeft);
        dest.writeValue(bottomRight);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bounds> CREATOR = new Parcelable.Creator<Bounds>() {
        @Override
        public Bounds createFromParcel(Parcel in) {
            return new Bounds(in);
        }

        @Override
        public Bounds[] newArray(int size) {
            return new Bounds[size];
        }
    };
}
