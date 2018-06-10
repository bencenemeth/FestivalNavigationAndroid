package bme.aut.hu.festivalnavigationandroid.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    Toolbar infoFragmentToolbar;
    ImageView poiToolbarBackButton;
    TextView tvInfoFragmentPointName;
    ImageView ivInfoFragmentPointImage;
    ImageView ivInfoFragmentPointCategory;
    TextView tvInfoFragmentPointCategory;
    TextView tvInfoFragmentPointOpenNow;
    TextView tvInfoFragmentPointDistanceTo;
    TextView tvInfoFragmentPointOpeningDays;
    TextView tvInfoFragmentPointOpeningHours;
    TextView tvInfoFragmentPointDescription;

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
        setUpToolbar(view);
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

    private void setUpToolbar(View view) {
        infoFragmentToolbar = view.findViewById(R.id.infoFragmentToolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(infoFragmentToolbar);
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        poiToolbarBackButton = view.findViewById(R.id.poiToolbarBackButton);
        poiToolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }


    private void fillDialog(View view) {
        tvInfoFragmentPointName = view.findViewById(R.id.tvInfoFragmentPointName);
        ivInfoFragmentPointImage = view.findViewById(R.id.ivInfoFragmentPointImage);
        ivInfoFragmentPointCategory = view.findViewById(R.id.ivInfoFragmentPointCategory);
        tvInfoFragmentPointCategory = view.findViewById(R.id.tvInfoFragmentPointCategory);
        tvInfoFragmentPointOpenNow = view.findViewById(R.id.tvInfoFragmentPointOpenNow);
        tvInfoFragmentPointDistanceTo = view.findViewById(R.id.tvInfoFragmentPointDistanceTo);
        tvInfoFragmentPointOpeningDays = view.findViewById(R.id.tvInfoFragmentPointOpeningDays);
        tvInfoFragmentPointOpeningHours = view.findViewById(R.id.tvInfoFragmentPointOpeningHours);
        tvInfoFragmentPointDescription = view.findViewById(R.id.tvInfoFragmentPointDescription);

        // Name
        tvInfoFragmentPointName.setText(interestPoint.getName());

        // Category
        ivInfoFragmentPointCategory.setImageResource(interestPoint.getCategory().getImageID());
        tvInfoFragmentPointCategory.setText(interestPoint.getCategory().getName());

        // Open now
        if (interestPoint.isOpenNow()) {
            tvInfoFragmentPointOpenNow.setText(R.string.open);
            tvInfoFragmentPointOpenNow.setTextColor(Color.GREEN);
        } else {
            tvInfoFragmentPointOpenNow.setText(R.string.closed);
            tvInfoFragmentPointOpenNow.setTextColor(Color.RED);
        }

        // Distance to
        setTvPoiRowDistanceTo(interestPoint.getDistanceTo());

        // Opening hours
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
        tvInfoFragmentPointOpeningDays.setText(builderDays.toString());
        tvInfoFragmentPointOpeningHours.setText(builderHours.toString());

        // Description
        tvInfoFragmentPointDescription.setText(interestPoint.getDescription());
    }

    public void setTvPoiRowDistanceTo(Float distanceTo) {
        tvInfoFragmentPointDistanceTo.setText(String.valueOf(Math.round(distanceTo)) + " m");
    }
}
