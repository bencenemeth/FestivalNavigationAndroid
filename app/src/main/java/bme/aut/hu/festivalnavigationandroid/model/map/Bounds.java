package bme.aut.hu.festivalnavigationandroid.model.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ben23 on 2018-02-25.
 */

public class Bounds {

    @SerializedName("top-left")
    @Expose
    private TopLeft topLeft;
    @SerializedName("bottom-right")
    @Expose
    private BottomRight bottomRight;

    /**
     * No args constructor for use in serialization
     *
     */
    public Bounds() {
    }

    /**
     *
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
}
