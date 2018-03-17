package bme.aut.hu.festivalnavigationandroid.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.adapter.InterestClusterInfoWindowAdapter;
import bme.aut.hu.festivalnavigationandroid.adapter.InterestPointInfoWindowAdapter;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;

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

    private Map map;

    private Button btnStartNavigation;

    private ClusterManager<InterestPoint> mClusterManager;
    private DefaultClusterRenderer<InterestPoint> renderer;
    private InterestPointInfoWindowAdapter interestPointAdapter;
    private InterestClusterInfoWindowAdapter interestClusterAdapter;

    // Override the Fragment.onAttach() method to instantiate the ListFragmentInteractionListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        context = getContext();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnStartNavigation = view.findViewById(R.id.btnStartNavigation);
        return view;
    }

    public static MapsFragment newInstance(Map map) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putParcelable("map", map);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
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

        //mMap.setInfoWindowAdapter(new InterestPointInfoWindowAdapter(getContext()));

        // Setting the bounds of the camera
        // TODO: FROM SERVER
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(map.getLatLngBounds().getCenter(), 15));
        mMap.setLatLngBoundsForCameraTarget(map.getLatLngBounds());
        mMap.setMinZoomPreference(14.85f);

        setUpClusterManager(mMap);
        setUpClusterRenderer(mMap);
        mClusterManager.setRenderer(renderer);

        // Adding the POIs to the map as markers
        addItems(map);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Changing the navigation button visibility
                btnStartNavigation.setVisibility(View.GONE);
            }
        });

        /*PolylineOptions lineOptions = new PolylineOptions();
        Polyline polyline = mMap.addPolyline(lineOptions);
        polyline.setWidth(3f);
        polyline.setColor(Color.BLUE);*/
    }

    /**
     * Setting up the cluster manager.
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
        interestPointAdapter = new InterestPointInfoWindowAdapter(context);
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(interestPointAdapter);

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<InterestPoint>() {
            @Override
            public boolean onClusterItemClick(InterestPoint interestPoint) {
                interestPointAdapter.setClickedClusterItem(interestPoint);
                // Changing the navigation button visibility
                btnStartNavigation.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    /**
     * Setting up the cluster  adapter, listener for cluster  click.
     */
    private void setUpClusterAdapter() {
        interestClusterAdapter = new InterestClusterInfoWindowAdapter(context);
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(interestClusterAdapter);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<InterestPoint>() {
            @Override
            public boolean onClusterClick(Cluster<InterestPoint> cluster) {
                interestClusterAdapter.setClickedCluster(cluster);
                return false;
            }
        });
    }

    /**
     * Setting up the cluster renderer.
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
    public void addItems(Map map) {
        for (InterestPoint poi : map.getInterestPoints()) {
            mClusterManager.addItem(poi);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MapFragmentInteractionListener {
        void openInterestPointFragment(InterestPoint interestPoint);
    }
}
