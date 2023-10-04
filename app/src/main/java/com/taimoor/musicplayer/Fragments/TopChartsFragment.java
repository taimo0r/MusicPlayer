package com.taimoor.musicplayer.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.taimoor.musicplayer.Adapters.TopChartsAdapter;
import com.taimoor.musicplayer.Models.Chart;
import com.taimoor.musicplayer.Models.LaunchDataAPI;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Retrofit.ApiClient;
import com.taimoor.musicplayer.Retrofit.LaunchDataListener;

import java.util.List;

public class TopChartsFragment extends Fragment {

    RecyclerView chartsRecycler;
    TopChartsAdapter chartsAdapter;
    ProgressDialog dialog;
    ApiClient client;
    TextView noSongsTv;


    public TopChartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_charts, container, false);

        chartsRecycler = view.findViewById(R.id.top_charts_recycler);
        noSongsTv = view.findViewById(R.id.no_songs_tv);

        client = new ApiClient(getActivity());
        client.getLaunchData(listener);


        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading...");
        dialog.show();


        return view;
    }

    private final LaunchDataListener listener = new LaunchDataListener() {
        @Override
        public void onFetch(LaunchDataAPI response, String message) {
            if (response.getNew_trending().isEmpty()) {
                Toast.makeText(getContext(), "No Top Charts", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            noSongsTv.setVisibility(View.GONE);
            showTopCharts(response.getCharts());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), "An error Occurred", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };


    private void showTopCharts(List<Chart> charts) {
        chartsRecycler.setHasFixedSize(true);
        chartsRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        chartsAdapter = new TopChartsAdapter(getContext(), charts);
        chartsRecycler.setAdapter(chartsAdapter);
        dialog.dismiss();
    }

}