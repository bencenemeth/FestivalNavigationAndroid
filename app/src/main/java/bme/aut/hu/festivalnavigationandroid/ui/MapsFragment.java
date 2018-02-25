package bme.aut.hu.festivalnavigationandroid.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;

/**
 * Created by ben23 on 2018-02-14.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapsFragment.class.getSimpleName();

    private Map map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: ERROR HANDLING
        if (getArguments() != null)
            map = getArguments().getParcelable("map");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    public static MapsFragment newInstance(Map map) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putParcelable("map", map);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        // TODO: CHECK PERMISSION
        mMap.setMyLocationEnabled(true);


        // Customise the styling of the base map using a JSON object defined
        // in a string resource file. First create a MapStyleOptions object
        // from the JSON styles string, then pass this to the setMapStyle
        // method of the GoogleMap object.
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

        // Setting the bounds of the camera
        // TODO: FROM SERVER
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(map.getLatLngBounds().getCenter(), 15));
        mMap.setLatLngBoundsForCameraTarget(map.getLatLngBounds());
        mMap.setMinZoomPreference(14.85f);

        PolylineOptions lineOptions = new PolylineOptions();

        // Adding the POIs to the map as markers
        for (InterestPoint poi : map.getInterestPoints()) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(poi.getLocation()).title(poi.getName()).snippet(poi.getDescription()));
            marker.setTag(poi);
            // TODO: FROM SERVER
            //lineOptions.add(poi.getLocation());
        }

        Polyline polyline = mMap.addPolyline(lineOptions);
        polyline.setWidth(3f);
        polyline.setColor(Color.BLUE);
    }
}
