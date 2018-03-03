package bme.aut.hu.festivalnavigationandroid.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPointContainer;
import bme.aut.hu.festivalnavigationandroid.network.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener {

    private MapsFragment mapsFragment;
    private ListFragment listFragment;

    private CustomPageAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private static final String TAG = "MainActivity";

    private Map map;
    //private ArrayList<InterestPoint> pois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = getIntent().getParcelableExtra("map");
        map.setInterestPoints(new ArrayList<InterestPoint>());

        adapter = new CustomPageAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_map_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_format_list_bulleted_black_24dp);


        //pois = new ArrayList<>();

        /** MOCK DATA */
        /*
        // Map
        map = new Map();
        map.setId("map01");
        map.setName("Sziget Fesztivál 2018");
        // POI list
        pois = new ArrayList<>();
        // Test data
        pois.add(new InterestPoint("0", 47.552595, 19.055235, "Nagyszínpad",
                new MyTime(23, 0), new MyTime(12, 0),
                InterestPoint.Category.Stage, "asd"));
        pois.add(new InterestPoint("1", 47.550664, 19.054935, "A38",
                new MyTime(20, 0), new MyTime(21, 0),
                InterestPoint.Category.Stage, "asd"));
        pois.add(new InterestPoint("2", 47.551551, 19.051904, "City Centre",
                new MyTime(0, 0), new MyTime(23, 59),
                InterestPoint.Category.Infopoint, "asd"));
        // Adding list to map
        map.setInterestPoints(pois);
        map.setLatLngBounds(new LatLngBounds(new LatLng(47.545672, 19.046436), new LatLng(47.560232, 19.062111)));
        */
        /** MOCK DATA */
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInterestPoints(map);
    }

    /**
     * Getting the map from the server.
     */
    private void loadMap() {
        Call<Map> a = NetworkManager.getInstance().getMap(map.getId());
        a.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Log.d(TAG, "onResponse: " + response.code());
                // OR response.code == 200
                if (response.isSuccessful()) {
                    map = response.body();
                    map.create();
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error in network request, check LOG", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Getting the interest points from the server.
     */
    /*private void loadInterestPoints() {
        // TODO: PASSING NULL TO OPENNOW
        Call<InterestPointContainer> a = NetworkManager.getInstance().getInterestPoints(mapId, true, "stage");
        a.enqueue(new Callback<InterestPointContainer>() {
            @Override
            public void onResponse(Call<InterestPointContainer> call, Response<InterestPointContainer> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    fillInterestPoints(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InterestPointContainer> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Error in network request, check LOG", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void loadInterestPoints(Map map) {
        NetworkManager.getInstance().getInterestPoints(map.getId()).enqueue(new Callback<InterestPointContainer>() {
            @Override
            public void onResponse(Call<InterestPointContainer> call, Response<InterestPointContainer> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    fillInterestPoints(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InterestPointContainer> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Error in network request, check LOG", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    private void loadInterestPoints(String mapId) {
        Call<InterestPointContainer> call = NetworkManager.getInstance().getInterestPoints(mapId);
        try {
            Response<InterestPointContainer> response = call.execute();
            Log.d(TAG, "onResponse: " + response.code());
            if(response.isSuccessful()) {
                fillInterestPoints(response.body());
            }
            else
                Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error in network request, check LOG", Toast.LENGTH_SHORT).show();
        }
    }*/

    /**
     * Passing the interest points to the List variable.
     *
     * @param interestPoints
     */
    private void fillInterestPoints(InterestPointContainer interestPoints) {
        // TODO: REVIEW
        map.getInterestPoints().clear();
        for (int i = 0; i < interestPoints.getPois().size(); i++) {
            InterestPoint temp = interestPoints.getPois().get(i);
            temp.create();
            map.getInterestPoints().add(temp);
        }
        map.create();
        listFragment.onCompletion();
        listFragment.stopRefreshingAnimation();
    }

    /**
     * The fragment calls MainActivity to refresh the interest points.
     */
    @Override
    public void onFragmentInteraction() {
        loadInterestPoints(map);
    }

    private class CustomPageAdapter extends FragmentPagerAdapter {

        public CustomPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mapsFragment = MapsFragment.newInstance(map);
                    return mapsFragment;
                case 1:
                    listFragment = ListFragment.newInstance(map);
                    return listFragment;
                default:
                    return null;
                //return ListFragment.newInstance(pois);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "List";
                default:
                    return "Error";
            }
        }
    }
}
