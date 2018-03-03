package bme.aut.hu.festivalnavigationandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;

/**
 * Created by ben23 on 2018-02-27.
 */

public class InterestPointInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View interestPointView;

    public InterestPointInfoWindowAdapter(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        interestPointView = layoutInflater.inflate(R.layout.infowindow_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        InterestPoint poi = (InterestPoint) marker.getTag();
        TextView tvPoiName = interestPointView.findViewById(R.id.tvPoiName);
        tvPoiName.setText(poi.getName());
        TextView tvCategory = interestPointView.findViewById(R.id.tvPoiCategory);
        tvCategory.setText(poi.getCategory().getName());
        ImageView ivPoiCategoryPicture = interestPointView.findViewById(R.id.ivPoiCategoryPicture);
        ivPoiCategoryPicture.setImageResource(poi.getCategory().getImageID());
        TextView tvPoiOpen = interestPointView.findViewById(R.id.tvPoiOpen);
        if (poi.isOpenNow()) {
            tvPoiOpen.setText(R.string.open);
            tvPoiOpen.setTextColor(Color.GREEN);
        } else {
            tvPoiOpen.setText(R.string.closed);
            tvPoiOpen.setTextColor(Color.RED);
        }
        return interestPointView;
    }
}
