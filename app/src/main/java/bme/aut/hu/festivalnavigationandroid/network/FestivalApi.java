package bme.aut.hu.festivalnavigationandroid.network;

import java.util.List;

import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.map.MapContainer;
import bme.aut.hu.festivalnavigationandroid.model.point.ControlPoint;
import bme.aut.hu.festivalnavigationandroid.model.point.ControlPointContainer;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPointContainer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ben23 on 2018-02-24.
 */

public interface FestivalApi {

    @GET("maps")
    Call<MapContainer> getMaps(@Query("lat") double lat,
                               @Query("lon") double lng);

    @GET("maps/{mapID}")
    Call<Map> getMap(@Path("mapID") String mapID);

    // TODO: THEORETICALLY USELESS
    @GET("maps/{mapID}/controls")
    Call<List<ControlPoint>> getControlPoints(@Path("mapID") String mapID);


    @GET("maps/{mapID}/interests")
    Call<InterestPointContainer> getInterestPoints(@Path("mapID") String mapID,
                                                   @Query("nowOpen") Boolean open,
                                                   @Query("type") String type);

    @GET("maps/{mapID}/interests")
    Call<InterestPointContainer> getInterestPoints(@Path("mapID") String mapID);


    @GET("navigation")
    Call<ControlPointContainer> navigation(@Query("current_lat") double lat,
                                           @Query("current_lon") double lng,
                                           @Query("destination_point_id") String id);

}
