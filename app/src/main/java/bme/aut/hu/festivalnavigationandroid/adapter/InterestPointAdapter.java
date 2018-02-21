package bme.aut.hu.festivalnavigationandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.InterestPoint;

/**
 * Created by ben23 on 2018-02-17.
 */

/**
 * Adapter for displaying the model items.
 */
public class InterestPointAdapter extends RecyclerView.Adapter<InterestPointAdapter.InterestPointViewHolder> {

    private List<InterestPoint> pois;
    private Context context;
    private LinearLayout llExtraInfo;
    private Button btnStartNavigation;

    public InterestPointAdapter(List<InterestPoint> pois, Context context) {
        this.pois = pois;
        this.context = context;
    }

    @Override
    public InterestPointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_row, parent, false);
        InterestPointViewHolder viewHolder = new InterestPointViewHolder(view);
        return viewHolder;
    }

    View previousView = null;

    /**
     * Binding the view to the model.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final InterestPointViewHolder holder, final int position) {
        InterestPoint poi = pois.get(position);
        // OTHER OPTION -> CREATE getImageResource METHOD
        // https://github.com/VIAUAC00/Android-labor/tree/master/Labor06
        holder.ivPoiRowIcon.setImageResource(poi.getCategory().getImageID());
        holder.tvPoiRowName.setText(poi.getName());
        //holder.tvPoiRowOpen.setText(poi.getName());
        //holder.tvPoiRowDistanceTo.setText(poi.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(previousView != view)
                    hidePoiExtra(previousView);
                showPoiExtra(view);
                previousView = view;
            }
        });
    }

    /**
     * Expanding or hiding the extra information of the InterestPoint on the list view.
     *
     * @param view the view of the InterestPoint in ListFragment's RecyclerView
     */
    public void showPoiExtra(View view) {
        llExtraInfo = view.findViewById(R.id.llExtraInfo);
        if (llExtraInfo.getVisibility() == View.VISIBLE) {
            llExtraInfo.setVisibility(View.GONE);
        } else if (llExtraInfo.getVisibility() == View.GONE) {
            llExtraInfo.setVisibility(View.VISIBLE);
        }
    }

    public void hidePoiExtra(View view) {
        if (view != null) {
            llExtraInfo = view.findViewById(R.id.llExtraInfo);
            llExtraInfo.setVisibility(View.GONE);
        }
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

    /**
     * Class for the views of the list items.
     */
    public class InterestPointViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoiRowIcon;
        TextView tvPoiRowName;
        TextView tvPoiRowOpen;
        TextView tvPoiRowDistanceTo;

        public InterestPointViewHolder(View itemView) {
            super(itemView);
            ivPoiRowIcon = itemView.findViewById(R.id.ivPoiRowIcon);
            tvPoiRowName = itemView.findViewById(R.id.tvPoiRowName);
            tvPoiRowOpen = itemView.findViewById(R.id.tvPoiRowOpen);
            tvPoiRowDistanceTo = itemView.findViewById(R.id.tvPoiRowDistanceTo);
        }
    }
}
