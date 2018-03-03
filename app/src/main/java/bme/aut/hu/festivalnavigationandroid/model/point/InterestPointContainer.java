package bme.aut.hu.festivalnavigationandroid.model.point;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ben23 on 2018-02-26.
 */

public class InterestPointContainer {

    @SerializedName("interests")
    @Expose
    private List<InterestPoint> pois = null;

    public List<InterestPoint> getPois() {
        return pois;
    }

    public void setPois(List<InterestPoint> pois) {
        this.pois = pois;
    }
}
