package bme.aut.hu.festivalnavigationandroid.model.point;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ben23 on 2018-04-01.
 */

public class ControlPointContainer {

    @SerializedName("path")
    @Expose
    private List<ControlPoint> controlPoints = null;

    @SerializedName("length")
    @Expose
    private int length;

    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(List<ControlPoint> pois) {
        this.controlPoints = pois;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
