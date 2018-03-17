package bme.aut.hu.festivalnavigationandroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baoyz.widget.PullRefreshLayout;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.adapter.CustomDividerItemDecoration;
import bme.aut.hu.festivalnavigationandroid.adapter.InterestPointAdapter;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;

/**
 * Created by ben23 on 2018-02-14.
 */

/**
 * Fragment for displaying the interest points in a list.
 */
public class ListFragment extends Fragment {

    private Context context;

    private ListFragmentInteractionListener mCallback;

    private Map map;

    private PullRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private InterestPointAdapter adapter;

    private Button btnStartNavigation;

    // Override the Fragment.onAttach() method to instantiate the ListFragmentInteractionListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            mCallback = (ListFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: ERROR HANDLING
        if (getArguments() != null)
            map = getArguments().getParcelable("map");
    }

    @Override
    public void onResume() {
        super.onResume();
        //adapter.notifyDataSetChanged();
        mCallback.refreshInterestPoints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getContext();
        btnStartNavigation = view.findViewById(R.id.btnStartNavigation);
        initRecyclerView(view, context);
        updateRecyclerView(view);
        return view;
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
     * @param context
     */
    private void initRecyclerView(View view, Context context) {
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new InterestPointAdapter(map.getInterestPoints(), context, recyclerView, btnStartNavigation);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(context, DividerItemDecoration.VERTICAL, R.drawable.list_item_divider));
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
                mCallback.refreshInterestPoints();
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
    public interface ListFragmentInteractionListener {
        // TODO: Update argument type and name
        void refreshInterestPoints();
    }
}
