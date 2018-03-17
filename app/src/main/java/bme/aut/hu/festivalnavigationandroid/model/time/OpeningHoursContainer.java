package bme.aut.hu.festivalnavigationandroid.model.time;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ben23 on 2018-03-10.
 */

public class OpeningHoursContainer {

    @SerializedName("all")
    @Expose
    private List<OpeningHours> openingHours = null;

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }
}
