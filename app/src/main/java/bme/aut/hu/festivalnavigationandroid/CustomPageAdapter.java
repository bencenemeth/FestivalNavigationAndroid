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

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
