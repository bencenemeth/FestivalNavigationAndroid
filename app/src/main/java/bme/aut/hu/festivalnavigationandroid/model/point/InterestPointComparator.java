package bme.aut.hu.festivalnavigationandroid.model.point;

import java.util.Comparator;

import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;

/**
 * Created by ben23 on 2018-03-16.
 */

public class InterestPointComparator implements Comparator<InterestPoint> {

    private int mode;

    public InterestPointComparator(int mode) {
        this.mode = mode;
    }

    @Override
    public int compare(InterestPoint o1, InterestPoint o2) {
        switch (mode) {
            case 0:
                return o1.getName().compareTo(o2.getName());
            case 1:
                return (o1.getDistanceTo()>o2.getDistanceTo()) ? 1 : -1;
            default:
                return o1.getName().compareTo(o2.getName());
        }
    }
}
