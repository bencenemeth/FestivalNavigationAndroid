package bme.aut.hu.festivalnavigationandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.time.OpeningHours;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ben23 on 2018-02-17.
 */

/**
 * Adapter for displaying the model items.
 */
public class InterestPointAdapter extends RecyclerView.Adapter<InterestPointAdapter.InterestPointViewHolder> {

    private AdapterChangeListener mCallback;

    private List<InterestPoint> pois;
    private Context context;
    private int lastSelectedPosition = -1;

    private RecyclerView recyclerView;

    public InterestPointAdapter(List<InterestPoint> pois, Context context, RecyclerView recyclerView) {
        this.pois = pois;
        this.context = context;
        this.recyclerView = recyclerView;

        try {
            mCallback = (AdapterChangeListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public InterestPointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_row, parent, false);
        return new InterestPointViewHolder(view);
    }

    /**
     * Binding the view to the model.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final InterestPointViewHolder holder, final int position) {
        InterestPoint interestPoint = pois.get(position);
        // OTHER OPTION -> CREATE getImageResource METHOD
        // https://github.com/VIAUAC00/Android-labor/tree/master/Labor06
        holder.ivPoiRowIcon.setImageResource(interestPoint.getCategory().getImageID());
        holder.tvPoiRowName.setText(interestPoint.getName());
        setOpenNow(holder, position, interestPoint);
        setExpand(holder, position, interestPoint);
        setOpeningHours(holder, position, interestPoint);
        holder.tvPoiRowDistanceTo.setText(String.valueOf(Math.round(interestPoint.getDistanceTo())) + " m");
        holder.tvPoiRowDescription.setText(interestPoint.getDescription());
    }

    /**
     * @param holder
     * @param position
     * @param interestPoint
     */
    private void setOpenNow(final InterestPointViewHolder holder, final int position, InterestPoint interestPoint) {
        if (interestPoint.isOpenNow()) {
            holder.tvPoiRowOpen.setText(R.string.open);
            holder.tvPoiRowOpen.setTextColor(Color.GREEN);
        } else {
            holder.tvPoiRowOpen.setText(R.string.closed);
            holder.tvPoiRowOpen.setTextColor(Color.RED);
        }
    }

    /**
     * @param holder
     * @param position
     * @param interestPoint
     */
    private void setExpand(final InterestPointViewHolder holder, final int position, InterestPoint interestPoint) {
        final boolean isExpanded = (position == lastSelectedPosition);
        holder.llExtraInfo.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = isExpanded ? -1 : position;
                mCallback.selectPoint(lastSelectedPosition);
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
                // ??????????
            }
        });
    }

    /**
     * @param holder
     * @param position
     * @param interestPoint
     */
    private void setOpeningHours(final InterestPointViewHolder holder, final int position, InterestPoint interestPoint) {
        StringBuilder builderDays = new StringBuilder();
        StringBuilder builderHours = new StringBuilder();
        SimpleDateFormat sdfDays = new SimpleDateFormat("MM/dd", Locale.US);
        SimpleDateFormat sdfHours = new SimpleDateFormat("HH:mm", Locale.US);
        List<OpeningHours> openingHours = interestPoint.getOpeningHours();
        for (int i = 0; i < openingHours.size(); i++) {
            builderDays.append(sdfDays.format(openingHours.get(i).getOpenCal().getTime()));
            builderDays.append("\n");

            builderHours.append(sdfHours.format(openingHours.get(i).getOpenCal().getTime()));
            builderHours.append(" - ");
            builderHours.append(sdfHours.format(openingHours.get(i).getCloseCal().getTime()));
            builderHours.append("\n");
        }
        holder.tvPoiRowOpeningDays.setText(builderDays.toString());
        holder.tvPoiRowOpeningHours.setText(builderHours.toString());
    }

    /**
     * Method for getting the number of items on the list.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return pois.size();
    }

    public interface AdapterChangeListener {
        void selectPoint(int position);
    }

    /**
     * Class for the views of the list items.
     */
    public class InterestPointViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoiRowIcon;
        TextView tvPoiRowName;
        TextView tvPoiRowOpen;
        TextView tvPoiRowDistanceTo;
        LinearLayout llExtraInfo;
        TextView tvPoiRowOpeningDays;
        TextView tvPoiRowOpeningHours;
        TextView tvPoiRowDescription;

        public InterestPointViewHolder(View itemView) {
            super(itemView);
            ivPoiRowIcon = itemView.findViewById(R.id.ivPoiRowIcon);
            tvPoiRowName = itemView.findViewById(R.id.tvPoiRowName);
            tvPoiRowOpen = itemView.findViewById(R.id.tvPoiRowOpen);
            tvPoiRowDistanceTo = itemView.findViewById(R.id.tvPoiRowDistanceTo);
            llExtraInfo = itemView.findViewById(R.id.llExtraInfo);
            tvPoiRowOpeningDays = itemView.findViewById(R.id.tvPoiRowOpeningDays);
            tvPoiRowOpeningHours = itemView.findViewById(R.id.tvPoiRowOpeningHours);
            tvPoiRowDescription = itemView.findViewById(R.id.tvPoiRowDescription);
        }
    }
}
