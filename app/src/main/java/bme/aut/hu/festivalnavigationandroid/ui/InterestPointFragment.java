package bme.aut.hu.festivalnavigationandroid.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.time.OpeningHours;

/**
 * Created by ben23 on 2018-03-11.
 */

public class InterestPointFragment extends Fragment {

    private InterestPoint interestPoint;
    private ImageView ivPoiRowIcon;
    TextView tvPoiRowName;
    TextView tvPoiRowOpen;
    TextView tvPoiRowDistanceTo;
    TextView tvPoiRowOpeningDays;
    TextView tvPoiRowOpeningHours;
    TextView tvPoiRowDescription;

    public InterestPoint getInterestPoint() {
        return interestPoint;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: ERROR HANDLING
        if (getArguments() != null)
            interestPoint = getArguments().getParcelable("poi");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interestpoint, container, false);
        fillDialog(view);
        return view;
    }



    public static InterestPointFragment newInstance(InterestPoint interestPoint) {
        InterestPointFragment fragment = new InterestPointFragment();
        Bundle args = new Bundle();
        args.putParcelable("poi", interestPoint);
        fragment.setArguments(args);
        return fragment;
    }

    private void fillDialog(View view) {
        ivPoiRowIcon = view.findViewById(R.id.ivPoiRowIcon);
        tvPoiRowName = view.findViewById(R.id.tvPoiRowName);
        tvPoiRowOpen = view.findViewById(R.id.tvPoiRowOpen);
        tvPoiRowDistanceTo = view.findViewById(R.id.tvPoiRowDistanceTo);
        tvPoiRowOpeningDays = view.findViewById(R.id.tvPoiRowOpeningDays);
        tvPoiRowOpeningHours = view.findViewById(R.id.tvPoiRowOpeningHours);
        tvPoiRowDescription = view.findViewById(R.id.tvPoiRowDescription);

        ivPoiRowIcon.setImageResource(interestPoint.getCategory().getImageID());
        tvPoiRowName.setText(interestPoint.getName());
        if (interestPoint.isOpenNow()) {
            tvPoiRowOpen.setText(R.string.open);
            tvPoiRowOpen.setTextColor(Color.GREEN);
        } else {
            tvPoiRowOpen.setText(R.string.closed);
            tvPoiRowOpen.setTextColor(Color.RED);
        }
        StringBuilder builderDays = new StringBuilder();
        StringBuilder builderHours = new StringBuilder();
        SimpleDateFormat sdfDays = new SimpleDateFormat("MM/dd", Locale.US);
        SimpleDateFormat sdfHours = new SimpleDateFormat("HH:mm", Locale.US);
        List<OpeningHours> openingHours = interestPoint.getOpeningHours();
        for(int i = 0; i < openingHours.size(); i++) {
            builderDays.append(sdfDays.format(openingHours.get(i).getOpenCal().getTime()));
            builderDays.append("\n");

            builderHours.append(sdfHours.format(openingHours.get(i).getOpenCal().getTime()));
            builderHours.append(" - ");
            builderHours.append(sdfHours.format(openingHours.get(i).getCloseCal().getTime()));
            builderHours.append("\n");
        }
        tvPoiRowOpeningDays.setText(builderDays.toString());
        tvPoiRowOpeningHours.setText(builderHours.toString());
        setTvPoiRowDistanceTo(interestPoint.getDistanceTo());
        tvPoiRowDescription.setText(interestPoint.getDescription());
    }

    public void setTvPoiRowDistanceTo(Float distanceTo) {
        tvPoiRowDistanceTo.setText(String.valueOf(Math.round(distanceTo)) + " m");
    }
}
