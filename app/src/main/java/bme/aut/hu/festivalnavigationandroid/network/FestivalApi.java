package bme.aut.hu.festivalnavigationandroid.network;

import java.util.List;

import bme.aut.hu.festivalnavigationandroid.model.ControlPoint;
import bme.aut.hu.festivalnavigationandroid.model.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ben23 on 2018-02-24.
 */

public interface FestivalApi {

    // TODO: CREATE MAP
    @GET("/maps")
    Call<List<Map>> getMaps(@Query("lat") double lat,
                            @Query("lon") double lng);

    @GET("/maps/{mapID}")
    Call<Map> getMap(@Path("mapID") String mapID);


    // TODO: THEORETICALLY USELESS
    @GET("maps/{mapID}/controls")
    Call<List<ControlPoint>> getControlPoints(@Path("mapID") String mapID);


    @GET("maps/{mapID}/interests")
    Call<List<InterestPoint>> getInterestPoints(@Path("mapID") String mapID,
                                                @Query("nowOpen") boolean open,
                                                @Query("type") String type);

    // TODO: DEVELOP ALGORITHM
    /*
    @GET("maps/{mapID}/navigation")
    Call<List<ControlPoint>> navigation(@Query("current-lat") double lat,
                                 @Query("current-lon") double lng,
                                 @Query("destination-point-id") String id);
                                 */
}
