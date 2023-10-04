package com.taimoor.musicplayer.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taimoor.musicplayer.Activities.MainActivity;
import com.taimoor.musicplayer.Adapters.TrendingAdapter;
import com.taimoor.musicplayer.Models.LaunchDataAPI;
import com.taimoor.musicplayer.Models.NewTrending;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Retrofit.ApiClient;
import com.taimoor.musicplayer.Retrofit.LaunchDataListener;

import java.util.List;

public class TrendingFragment extends Fragment {

    RecyclerView trendingRecycler;
    TrendingAdapter trendingAdapter;
    ProgressDialog dialog;
    ApiClient client;
    TextView noSongsTv;

    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        trendingRecycler = view.findViewById(R.id.trending_recycler);
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
                Toast.makeText(getContext(), "No Trending", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            noSongsTv.setVisibility(View.GONE);
            showTrending(response.getNew_trending());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), "An error Occurred", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void showTrending(List<NewTrending> new_trending) {
        trendingRecycler.setHasFixedSize(true);
        trendingRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));
        trendingAdapter = new TrendingAdapter(getContext(), new_trending);
        trendingRecycler.setAdapter(trendingAdapter);
        dialog.dismiss();
    }

}