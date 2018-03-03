package bme.aut.hu.festivalnavigationandroid.model.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ben23 on 2018-02-25.
 */

public class MapContainer {

    @SerializedName("maps")
    @Expose
    private List<Map> maps;

    public List<Map> getMaps() {
        return maps;
    }

    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }
}
