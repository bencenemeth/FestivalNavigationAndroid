package bme.aut.hu.festivalnavigationandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;

import org.w3c.dom.Text;

import java.util.List;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;

/**
 * Created by ben23 on 2018-03-07.
 */

public class InterestClusterInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View interestClusterView;
    // Not a list, but a collection that stores InterestPoints
    private Cluster<InterestPoint> clickedCluster;

    public InterestClusterInfoWindowAdapter(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        interestClusterView = layoutInflater.inflate(R.layout.interestcluster_infowindow_content, null);
    }

    public void setClickedCluster(Cluster<InterestPoint> clickedCluster) {
        this.clickedCluster = clickedCluster;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if(clickedCluster != null) {
            TextView tvClusterCount = interestClusterView.findViewById(R.id.tvClusterCount);
            tvClusterCount.setText(String.valueOf(clickedCluster.getSize()));
            return interestClusterView;
        }
        return null;
    }
}
