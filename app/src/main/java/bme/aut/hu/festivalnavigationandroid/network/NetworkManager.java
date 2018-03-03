package bme.aut.hu.festivalnavigationandroid.network;

import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.map.MapContainer;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPointContainer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ben23 on 2018-02-24.
 */

public class NetworkManager {

    // TODO: ENDPOINT
    private static final String ENDPOINT_ADDRESS = "http://10.0.2.2:3000/api/v1/";

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null)
            instance = new NetworkManager();
        return instance;
    }

    private Retrofit retrofit;
    private FestivalApi festivalApi;

    private NetworkManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        festivalApi = retrofit.create(FestivalApi.class);
    }

    public Call<MapContainer> getMaps(double lat, double lon) {
        return festivalApi.getMaps(lat, lon);
    }

    public Call<Map> getMap(String mapID) {
        return festivalApi.getMap(mapID);
    }

    /*public Call<InterestPointContainer> getInterestPoints(String mapID, boolean open, String type) {
        return festivalApi.getInterestPoints(mapID, open, type);
    }*/

    public Call<InterestPointContainer> getInterestPoints(String mapID) {
        return festivalApi.getInterestPoints(mapID);
    }
}
