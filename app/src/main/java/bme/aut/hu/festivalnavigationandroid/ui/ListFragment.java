package bme.aut.hu.festivalnavigationandroid.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.adapter.InterestPointAdapter;
import bme.aut.hu.festivalnavigationandroid.model.InterestPoint;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;

/**
 * Created by ben23 on 2018-02-14.
 */

/**
 * Fragment for displaying the POIs in a list.
 */
public class ListFragment extends Fragment {

    private Map map;

    private RecyclerView recyclerView;
    private InterestPointAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: ERROR HANDLING
        if (getArguments() != null)
            map = getArguments().getParcelable("map");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initRecyclerView(view);
        return view;
    }

    public static ListFragment newInstance(Map map) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelable("map", map);
        fragment.setArguments(args);
        return fragment;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new InterestPointAdapter(map, this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

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
        void onFragmentInteraction(Uri uri);
    }
}
