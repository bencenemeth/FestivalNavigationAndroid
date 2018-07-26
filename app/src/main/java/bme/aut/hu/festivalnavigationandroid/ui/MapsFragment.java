package bme.aut.hu.festivalnavigationandroid.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.HashMap;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.adapter.InterestClusterInfoWindowAdapter;
import bme.aut.hu.festivalnavigationandroid.adapter.InterestPointInfoWindowAdapter;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.point.ControlPoint;
import bme.aut.hu.festivalnavigationandroid.model.point.ControlPointContainer;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;

import static bme.aut.hu.festivalnavigationandroid.ui.FestivalSelectActivity.NIGHTMODE;
import static bme.aut.hu.festivalnavigationandroid.ui.MainActivity.MY_LOCATION;

/**
 * Created by ben23 on 2018-02-14.
 */

/**
 * Fragment for displaying the interest points in the map.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapsFragment.class.getSimpleName();
    private Context context;
    private MapFragmentInteractionListener mCallback;

    private GoogleMap mMap;
    private Map map;
    private InterestPoint selectedPoint;
    private ControlPointContainer path;

    private Button btnStartNavigation;
    private Button btnCancelNavigation;

    private ClusterManager<InterestPoint> mClusterManager;
    private DefaultClusterRenderer<InterestPoint> renderer;
    private InterestPointInfoWindowAdapter interestPointInfoWindowAdapter;
    private InterestClusterInfoWindowAdapter interestClusterInfoWindowAdapter;

    private boolean navigationOn;

    private CountDownTimer countDownTimer;

    // Override the Fragment.onAttach() method to instantiate the ListFragmentInteractionListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // Verify that the host activity implements the callback interface
        try {
            mCallback = (MapFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: ERROR HANDLING
        if (getArguments() != null)
            map = getArguments().getParcelable("map");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflating the view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Creating the map
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializing the navigation buttons
        initStartNavigationButton(view);
        initCancelNavigationButton(view);

        navigationOn = false;
        selectedPoint = null;

        // Creating the timer
        createTimer();

        return view;
    }

    public static MapsFragment newInstance(Map map) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putParcelable("map", map);
        fragment.setArguments(args);
        return fragment;
    }

    public void setSelectedPoint(InterestPoint selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Customise the styling of the base map using a JSON object defined
        // in a string resource file. First create a MapStyleOptions object
        // from the JSON styles string, then pass this to the setMapStyle
        // method of the GoogleMap object.
        boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));

        if (NIGHTMODE)
            enableNightMode();

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

        setUpClusterManager(mMap);
        setUpClusterRenderer(mMap);
        mClusterManager.setRenderer(renderer);

        // Setting the bounds of the camera
        moveCamera(map.getLatLngBounds().getCenter(), 15, 0, 0, 1000);
        mMap.setLatLngBoundsForCameraTarget(map.getLatLngBounds());
        mMap.setMinZoomPreference(14.85f);


        // Adding the POIs to the map as markers
        addItems();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Changing the navigation button visibility
                btnStartNavigation.setVisibility(View.GONE);
                selectedPoint = null;
            }
        });

        setOnCameraIdle();
    }

    public void refreshMap(Map map) {
        this.map = map;

        // Clearing the map and the cluster manager (no need for other points in navigation mode)
        mMap.clear();
        mClusterManager.clearItems();

        // Adding the POIs to the map as markers
        addItems();

        mClusterManager.cluster();
    }

    /**
     * Enable Night Mode on the map.
     */
    public void enableNightMode() {
        boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_nightmap_json)));

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }
    }

    /**
     * Setting up the cluster manager.
     *
     * @param mMap
     */
    private void setUpClusterManager(GoogleMap mMap) {
        mClusterManager = new ClusterManager<>(context, mMap);

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        setUpClusterItemAdapter();
        setUpClusterAdapter();

        mClusterManager.setAnimation(false);

        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<InterestPoint>() {
            @Override
            public void onClusterItemInfoWindowClick(InterestPoint interestPoint) {
                mCallback.openInterestPointFragment(interestPoint);
            }
        });
    }

    /**
     * Setting up the cluster item adapter, listener for cluster item click.
     */
    private void setUpClusterItemAdapter() {
        interestPointInfoWindowAdapter = new InterestPointInfoWindowAdapter(context);
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(interestPointInfoWindowAdapter);

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<InterestPoint>() {
            @Override
            public boolean onClusterItemClick(InterestPoint interestPoint) {
                interestPointInfoWindowAdapter.setClickedClusterItem(interestPoint);
                // Changing the navigation button visibility
                if(!navigationOn) {
                    btnStartNavigation.setVisibility(View.VISIBLE);
                    selectedPoint = interestPoint;
                }
                return false;
            }
        });
    }

    /**
     * Setting up the cluster  adapter, listener for cluster  click.
     */
    private void setUpClusterAdapter() {
        interestClusterInfoWindowAdapter = new InterestClusterInfoWindowAdapter(context);
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(interestClusterInfoWindowAdapter);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<InterestPoint>() {
            @Override
            public boolean onClusterClick(Cluster<InterestPoint> cluster) {
                interestClusterInfoWindowAdapter.setClickedCluster(cluster);
                return false;
            }
        });
    }

    /**
     * Setting up the cluster renderer.
     *
     * @param mMap
     */
    private void setUpClusterRenderer(GoogleMap mMap) {
        renderer = new DefaultClusterRenderer<>(context, mMap, mClusterManager);
        // If 2 markers overlap, they will be clustered.
        renderer.setMinClusterSize(2);
    }

    /**
     * Adding items to the cluster manager.
     */
    public void addItems() {
        for (InterestPoint poi : map.getInterestPoints()) {
            mClusterManager.addItem(poi);
        }
    }

    /**
     * Setting the camera movement.
     */
    private void setOnCameraIdle() {
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mClusterManager.onCameraIdle();
                if(navigationOn) {
                    countDownTimer.cancel();
                    countDownTimer.start();
                }
            }
        });
    }

    /**
     * Initializing the start navigation button.
     * @param view              fragment's inflated view
     */
    private void initStartNavigationButton(View view) {
        btnStartNavigation = view.findViewById(R.id.btnStartNavigation);
        btnStartNavigation.setVisibility(View.GONE);
        btnStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.callForNavigation(selectedPoint);
            }
        });
    }

    /**
     * Initializing the cancel navigation button.
     * @param view              fragment's inflated view
     */
    private void initCancelNavigationButton(View view) {
        btnCancelNavigation = view.findViewById(R.id.btnCancelNavigation);
        btnCancelNavigation.setVisibility(View.GONE);
        btnCancelNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNavigation();
            }
        });
    }

    /**
     * Timer for recentering the map to the current position while navigating to a point.
     */
    public void createTimer() {
        countDownTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Nothing happens on ticks
            }

            @Override
            public void onFinish() {
                followMyLocation(calculateNearestPoint());
                countDownTimer.start();
            }
        };
    }

    private void moveCamera(LatLng target, int zoom, int tilt, float bearing, int animationTime) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)
                .zoom(zoom)
                .tilt(tilt)
                .bearing(bearing)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), animationTime, null);
    }

    /**
     * Starting the navigation to the selected point.
     *
     * @param path              path to the point
     * @param interestPoint     destination point
     */
    public void startNavigation(ControlPointContainer path, InterestPoint interestPoint) {
        navigationOn = true;
        this.path = path;

        // Hide the start navigation button
        btnStartNavigation.setVisibility(View.GONE);

        // Show the cancel navigation button
        btnCancelNavigation.setVisibility(View.VISIBLE);

        // Clearing the map and the cluster manager (no need for other points in navigation mode)
        mMap.clear();
        mClusterManager.clearItems();

        // Putting up the destination point to the map
        mClusterManager.addItem(interestPoint);

        // Drawing the route to the destination
        PolylineOptions lineOptions = new PolylineOptions().color(Color.BLUE).width(5f);
        lineOptions.add(new LatLng(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude()));

        for(ControlPoint cp : path.getControlPoints()) {
            cp.setLocation();
            cp.setLatLng();
            lineOptions.add(cp.getLatLng());
        }
        mMap.addPolyline(lineOptions);

        mClusterManager.cluster();

        // Setting the camera position
        followMyLocation(path.getControlPoints().get(0));
    }

    /**
     * Recenter the view to the current location.
     */
    private void followMyLocation(ControlPoint point) {
        moveCamera(new LatLng(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude()), 19, 90, calculateBearing(point), 2000);
    }

    /**
     * Calculating the nearest point to the current location.
     */
    public ControlPoint calculateNearestPoint() {

        if(navigationOn) {
            // Points and distances to a HashMap
            // Only ID isn't enough because we need the point for calculating the camera bearing later
            java.util.Map<ControlPoint, Float> distances = new HashMap<>();

            // Calculating distances to the points
            for (ControlPoint point : path.getControlPoints())
                distances.put(point, MY_LOCATION.distanceTo(point.getLocation()));

            // Get the minimum to a map entry
            java.util.Map.Entry<ControlPoint, Float> min = null;
            for (java.util.Map.Entry<ControlPoint, Float> entry : distances.entrySet())
                if (min == null || min.getValue() > entry.getValue())
                    min = entry;

            if(distances.get(path.getControlPoints().get(path.getControlPoints().size()-1)) < 10)
                endNavigation();

            // If the distance to the nearest control point is further then 100 meters then we need to reroute
            if (min.getValue() > 20)
                mCallback.callForNavigation(selectedPoint);

            return min.getKey();

            //reDrawRoute();

            // külön kéne
            //followMyLocation(min.getKey());
        }
        else
            return null;
    }

    private void endNavigation() {
        Toast.makeText(context, "You have reached your destination!", Toast.LENGTH_SHORT).show();
        cancelNavigation();
    }

    private void reDrawRoute() {
        mMap.clear();
        // Drawing the route to the destination
        PolylineOptions lineOptions = new PolylineOptions().color(Color.BLUE).width(5f);
        lineOptions.add(new LatLng(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude()));
        for(ControlPoint cp : path.getControlPoints()) {
            //cp.setLocation();
            //cp.setLatLng();
            lineOptions.add(cp.getLatLng());
        }
        mMap.addPolyline(lineOptions);
    }

    private void cancelNavigation() {
        countDownTimer.cancel();
        navigationOn = false;
        path = null;
        selectedPoint = null;

        // Show the navigation button
        //btnStartNavigation.setVisibility(View.VISIBLE);

        // Hide the cancel navigation button
        btnCancelNavigation.setVisibility(View.GONE);

        // Clearing the map and the cluster manager
        mMap.clear();
        mClusterManager.clearItems();

        // Redrawing the markers
        addItems();
        mClusterManager.cluster();

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude()), 15));
        moveCamera(new LatLng(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude()), 15, 0, 0, 2000);
    }

    /**
     * Calculating the camera bearing to the given point
     * @param point             the nearest control point
     * @return                  camera bearing
     */
    private float calculateBearing(ControlPoint point) {
        return MY_LOCATION.bearingTo(point.getLocation());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface MapFragmentInteractionListener {
        // Opening the InterestPointFragment
        void openInterestPointFragment(InterestPoint interestPoint);

        // Starting navigation
        void callForNavigation(InterestPoint destinationPoint);
    }
}
