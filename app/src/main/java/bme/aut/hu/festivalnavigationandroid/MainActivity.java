package bme.aut.hu.festivalnavigationandroid;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import bme.aut.hu.festivalnavigationandroid.model.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.MyTime;

public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener {

    private CustomPageAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ArrayList<InterestPoint> pois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new CustomPageAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_map_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_format_list_bulleted_black_24dp);

        // POI list
        // TODO: SZERVERRŐL FOG JÖNNI
        pois = new ArrayList<>();

        // Test data
        pois.add(new InterestPoint(new LatLng(47.552595, 19.055235), "Nagyszínpad",
                new MyTime(23, 0), new MyTime(12, 0),
                InterestPoint.Category.Stage, "asd"));
        pois.add(new InterestPoint(new LatLng(47.550664, 19.054935), "A38",
                new MyTime(20, 0), new MyTime(21, 0),
                InterestPoint.Category.Stage, "asd"));
        pois.add(new InterestPoint(new LatLng(47.551551, 19.051904), "City Centre",
                new MyTime(0, 0), new MyTime(23, 59),
                InterestPoint.Category.Infopoint, "asd"));
    }

    // TODO MEGNÉZNI

    /**
     * Fragmenthez kell
     *
     * @param uri ??
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class CustomPageAdapter extends FragmentPagerAdapter {

        public CustomPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: newInstance
            switch (position) {
                case 0:
                    return MapsFragment.newInstance(pois);
                case 1:
                    return ListFragment.newInstance(pois);
                default:
                    return new ListFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        // TODO: REVIEW IF NECESSARY
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
