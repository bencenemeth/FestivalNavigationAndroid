package bme.aut.hu.festivalnavigationandroid.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bme.aut.hu.festivalnavigationandroid.adapter.InterestPointAdapter;
import bme.aut.hu.festivalnavigationandroid.model.point.ControlPoint;
import bme.aut.hu.festivalnavigationandroid.model.point.ControlPointContainer;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPointComparator;
import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPointContainer;
import bme.aut.hu.festivalnavigationandroid.model.time.OpeningHours;
import bme.aut.hu.festivalnavigationandroid.network.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bme.aut.hu.festivalnavigationandroid.ui.FestivalSelectActivity.NIGHTMODE;

public class MainActivity
        extends AppCompatActivity
        implements ListFragment.ListFragmentInteractionListener,
        MapsFragment.MapFragmentInteractionListener,
        SortDialogFragment.NoticeSortDialogListener,
        FilterDialogFragment.NoticeFilterDialogListener,
        InterestPointAdapter.AdapterChangeListener {

    /**
     * Fragments
     */
    private MapsFragment mapsFragment;
    private ListFragment listFragment;
    private InterestPointFragment interestPointFragment;

    private CustomPageAdapter adapter;

    /**
     * UI elements
     */
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar mainToolbar;
    private TextView tvFestivalName;
    private ImageView toolbarBackButton;
    private ImageView toolbarItemFilter;
    private ImageView toolbarItemSort;

    private static final String TAG = "MainActivity";

    private Map map;
    //private List<String> categoryNames;
    private boolean mapIsReady = false;

    /**
     * Filters
     */
    private Boolean filterOpenNow;
    private String filterCategoryName;

    /**
     * Sort mode
     */
    public static int SORT_MODE = -1;

    /**
     * Selected point's position in the list
     */
    public static int SELECTED_POINT_POSITION = -1;

    /**
     * Location
     */
    private FusedLocationProviderClient mFusedLocationClient;
    public static Location MY_LOCATION;

    /**
     *
     */
    private Handler handler;
    private int delay;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        handler = new Handler();
        delay = 2000;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        map = getIntent().getParcelableExtra("map");
        map.setInterestPoints(new ArrayList<InterestPoint>());
        // No need for provider
        MY_LOCATION = new Location("");

        setUpToolbar();
        //categoryNames = new ArrayList<>();
        instantiateFragments();
        setUpViewPager();
        if(NIGHTMODE)
            enableNightMode();
    }

    @Override
    protected void onResume() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocation();
                runnable = this;
                handler.postDelayed(runnable, delay);
            }
        }, delay);
        super.onResume();
        loadInterestPoints(map, null, null);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }


    /**
     * Setting up the Activity's toolbar.
     */
    private void setUpToolbar() {
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setting the back button
        toolbarBackButton = findViewById(R.id.toolbarBackButton);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });

        // Setting the name
        tvFestivalName = findViewById(R.id.tvFesttivalName);
        tvFestivalName.setText(map.getName());

        // Filter button
        toolbarItemFilter = findViewById(R.id.toolbarItemFilter);
        toolbarItemFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu();
            }
        });

        // Sort button
        toolbarItemSort = findViewById(R.id.toolbarItemSort);
        toolbarItemSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu();
            }
        });
    }

    /**
     * Instantiating the viewpager's fragments.
     */
    private void instantiateFragments() {
        mapsFragment = MapsFragment.newInstance(map);
        listFragment = ListFragment.newInstance(map);
    }

    /**
     * Setting up the viewpager.
     */
    private void setUpViewPager() {
        // Creating the adapter
        adapter = new CustomPageAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        // Setting the adapter
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        // Setting up the tablayout with the viewpager
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_map_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_format_list_bulleted_black_24dp);
    }

    public void enableNightMode() {
        setTheme(R.style.AppThemeNightMode);
        mainToolbar.setBackgroundColor(Color.parseColor("#000000"));
        tabLayout.setBackgroundColor(Color.parseColor("#212121"));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_map_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_format_list_bulleted_white_24dp);
    }

    /**
     * Getting the interest points from the server.
     */
    private void loadInterestPoints(Map map, Boolean openNow, String categoryName) {
        // Map is unavailable until the response.
        mapIsReady = false;
        // Call to the server.
        NetworkManager.getInstance().getInterestPoints(map.getId(), openNow, categoryName).enqueue(new Callback<InterestPointContainer>() {
            @Override
            public void onResponse(@NonNull Call<InterestPointContainer> call, @NonNull Response<InterestPointContainer> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    fillInterestPoints(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<InterestPointContainer> call, @NonNull Throwable t) {
                t.printStackTrace();
                //Toast.makeText(MainActivity.this, "Error in network request, check LOG", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Filling up map's interest point list.
     *
     * @param interestPoints InterestPointContainer from the server
     */
    private void fillInterestPoints(InterestPointContainer interestPoints) {
        // TODO: REVIEW
        map.getInterestPoints().clear();
        for (int i = 0; i < interestPoints.getPois().size(); i++) {
            InterestPoint temp = interestPoints.getPois().get(i);
            temp.create();
            setDistanceTo(temp);
            map.getInterestPoints().add(temp);
        }
        //mapsFragment.refreshMap(map);
        mapIsReady = true;
        sortInterestPoints(SORT_MODE);
        //listFragment.onCompletion();
        listFragment.stopRefreshingAnimation();
    }

    /**
     * Getting the last known location.
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        // Only if the map is loaded from the server
        if (mapIsReady) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        MY_LOCATION = location;
                        setDistanceTo(null);
                        mapsFragment.calculateNearestPoint();
                        // Calls notifyDataSetChanged
                        listFragment.onCompletion();
                    }
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    /*private void getLocation() {
        if (mapIsReady) {
            LocationManager mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            //boolean b = mLocationManager.getProvider(LocationManager.GPS_PROVIDER).supportsBearing();
            LocationListener mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    MY_LOCATION = location;
                    setDistanceTo(null);
                    // Calls notifyDataSetChanged
                    listFragment.onCompletion();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
    }*/

    /**
     * Setting the distance from own location to the points.
     *
     * @param interestPointFromMap      setting only 1 point's location
     */
    private void setDistanceTo(final InterestPoint interestPointFromMap) {
        Location temp = new Location("");
        if (interestPointFromMap != null) {
            temp.setLatitude(interestPointFromMap.getLat());
            temp.setLongitude(interestPointFromMap.getLon());
            interestPointFromMap.setDistanceTo(MY_LOCATION.distanceTo(temp));
        }
        // Looping through the interest points.
        for (InterestPoint interestPoint : map.getInterestPoints()) {
            // Create a location object from the point's lat and lon
            temp.setLatitude(interestPoint.getLat());
            temp.setLongitude(interestPoint.getLon());
            // Calculating the distance
            interestPoint.setDistanceTo(MY_LOCATION.distanceTo(temp));
        }
    }

    private void startNavigation(final InterestPoint destinationPoint) {
        // Map is unavailable until the response.
        if(destinationPoint == null) {
            //Toast.makeText(MainActivity.this, "Error: No destination point given!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Network call
        NetworkManager.getInstance().navigation(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude(), destinationPoint.getId()).enqueue(new Callback<ControlPointContainer>() {
            @Override
            public void onResponse(@NonNull Call<ControlPointContainer> call, @NonNull Response<ControlPointContainer> response) {
                if(response.isSuccessful()) {
                    mapIsReady = true;
                    mapsFragment.startNavigation(response.body(), destinationPoint);
                    mapsFragment.setSelectedPoint(destinationPoint);
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ControlPointContainer> call, @NonNull Throwable t) {
                t.printStackTrace();
                //Toast.makeText(MainActivity.this, "Error in network request, check LOG", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sorting the interest points.
     *
     * @param sortMode       sorting mode (0: alphabetical, 1: distance)
     */
    private void sortInterestPoints(int sortMode) {
        if(sortMode == -1) {
            listFragment.onCompletion();
            return;
        }
        SORT_MODE = sortMode;
        Collections.sort(map.getInterestPoints(), new InterestPointComparator(sortMode));
        listFragment.onCompletion();
    }

    /**
     * Showing the filter dialog.
     */
    public void showFilterMenu() {
        //FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance();
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.show(getSupportFragmentManager(), "Filter");
    }

    /**
     * Showing the sorting dialog.
     */
    private void showSortMenu() {
        SortDialogFragment sortDialogFragment = new SortDialogFragment();
        sortDialogFragment.show(getSupportFragmentManager(), "Sort");
    }

    /*private void refreshInterestPointFragment() {
        interestPointFragment = (InterestPointFragment) getSupportFragmentManager().findFragmentByTag("interestPointFragment");
        getLatLng(interestPointFragment.getInterestPoint());
    }*/

    /**
     * The ListFragment calls MainActivity to refresh the interest points.
     */
    @Override
    public void refreshInterestPoints() {
        // Null filters can be passed to server.
        getLocation();
        loadInterestPoints(map, filterOpenNow, filterCategoryName);
    }

    /**
     * The ListFragment calls MainActivity to switch to MapsFragment and start navigation.

     */
    @Override
    public void switchPage() {
        viewPager.setCurrentItem(0, true);
        // TODO: USE
        if(SELECTED_POINT_POSITION != -1)
            startNavigation(map.getInterestPoints().get(SELECTED_POINT_POSITION));
    }

    /**
     * The MapsFragment calls MainActivity to start the navigation.
     *
     * @param destinationPoint      destination point
     */
    public void callForNavigation(InterestPoint destinationPoint) {
        startNavigation(destinationPoint);
    }

    /**
     * The MapsFragment calls MainActivity to open the InterestPointFragment.
     *
     * @param interestPoint         the selected point
     */
    @Override
    public void openInterestPointFragment(InterestPoint interestPoint) {
        //getLatLng(interestPoint);
        setDistanceTo(interestPoint);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.llContainer, InterestPointFragment.newInstance(interestPoint), "interestPointFragment").addToBackStack(null).commit();
    }

    /**
     * Sorting dialog calls to sort the points.
     */
    @Override
    public void onSortDialogPositiveClick() {
        sortInterestPoints(SORT_MODE);
    }

    /**
     * Filtering dialog calls to filter the points.
     *
     * @param open                      the state of the point
     * @param category                  the category of the point
     */
    @Override
    public void onFilterDialogPositiveClick(Boolean open, String category) {
        // Store filters, used on refresh.
        filterOpenNow = open;
        filterCategoryName = category;
        refreshInterestPoints();
    }

    /**
     * Selecting a point in the list.
     *
     * @param selectedPointPosition     selected point's position in the list
     */
    @Override
    public void selectPoint(int selectedPointPosition) {
        listFragment.selectPoint(selectedPointPosition);
    }

    private class CustomPageAdapter extends FragmentPagerAdapter {

        public CustomPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: PLACE newInstance ELSEWHERE
            switch (position) {
                case 0:
                    return mapsFragment;
                case 1:
                    return listFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "List";
                default:
                    return "Error";
            }*/
            return null;
        }
    }
}
