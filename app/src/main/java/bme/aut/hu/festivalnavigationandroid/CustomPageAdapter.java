package bme.aut.hu.festivalnavigationandroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ben23 on 2018-02-14.
 */

public class CustomPageAdapter extends FragmentPagerAdapter {

    public CustomPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapsFragment();
            case 1:
                return new ListFragment();
            default:
                return new ListFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
