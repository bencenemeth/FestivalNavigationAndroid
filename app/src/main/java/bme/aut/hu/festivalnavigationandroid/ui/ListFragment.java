package bme.aut.hu.festivalnavigationandroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.adapter.InterestPointAdapter;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.point.InterestPoint;

/**
 * Created by ben23 on 2018-02-14.
 */

/**
 * Fragment for displaying the POIs in a list.
 */
public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mCallback;

    private Map map;
    //private ArrayList<InterestPoint> pois;

    private PullRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private InterestPointAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: ERROR HANDLING
        if (getArguments() != null)
            map = getArguments().getParcelable("map");
        //pois = getArguments().getParcelableArrayList("pois");
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initRecyclerView(view);
        updateRecyclerView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    public static ListFragment newInstance(ArrayList<InterestPoint> pois) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("pois", pois);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListFragment newInstance(Map map) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelable("map", map);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializing the recyclerview.
     *
     * @param view
     */
    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new InterestPointAdapter(map.getInterestPoints(), this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Updating the recyclerview with a swipe.
     *
     * @param view
     */
    private void updateRecyclerView(View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            /**
             * Refreshing the recyclerview, by calling MainActivity.
             */
            @Override
            public void onRefresh() {
                mCallback.onFragmentInteraction();
            }
        });
    }

    /**
     * When the app recieved the data from the server, MainActivity calls this function
     * and reloads the content of the recyclerview.
     */
    public void onCompletion() {
        adapter.notifyDataSetChanged();
    }

    /**
     * Stopping the load animation.
     */
    public void stopRefreshingAnimation() {
        swipeRefresh.setRefreshing(false);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
