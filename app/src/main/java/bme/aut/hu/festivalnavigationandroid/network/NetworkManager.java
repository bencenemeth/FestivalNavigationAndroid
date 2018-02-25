package bme.aut.hu.festivalnavigationandroid.network;

import java.util.List;

import bme.aut.hu.festivalnavigationandroid.model.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ben23 on 2018-02-24.
 */

public class NetworkManager {

    // TODO: ENDPOINT
    private static final String ENDPOINT_ADDRESS = "valami";

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if(instance == null)
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

    public Call<List<Map>> getMaps(double lat, double lon) {
        return festivalApi.getMaps(lat, lon);
    }

    public Call<Map> getMap(String mapID) {
        return festivalApi.getMap(mapID);
    }

    public Call<List<InterestPoint>> getInterestPoints(String mapID, boolean open, String type) {
        return festivalApi.getInterestPoints(mapID, open, type);
    }

}
