package com.taimoor.musicplayer.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.taimoor.musicplayer.Activities.AlbumActivity;
import com.taimoor.musicplayer.Activities.SearchActivity;
import com.taimoor.musicplayer.Listeners.onSongItemClick;
import com.taimoor.musicplayer.Models.Result;
import com.taimoor.musicplayer.Models.SongApiResponse;
import com.taimoor.musicplayer.Adapters.MusicAdapter;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Retrofit.ApiClient;
import com.taimoor.musicplayer.Retrofit.SongResponseListener;

import java.util.List;

public class SongsFragment extends Fragment implements onSongItemClick {

    RecyclerView recyclerView;
    MusicAdapter adapter;
    ApiClient client;
    ProgressDialog dialog;
    String query;
    TextView noSongsTv;

    public SongsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noSongsTv = view.findViewById(R.id.no_songs_tv);
        client = new ApiClient(getContext());

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading...");

        if (((SearchActivity) getActivity()).getQuery() != null) {
            query = ((SearchActivity) getActivity()).getQuery();
            if (query != null) {
                client.getSearchedSong(listener, query);
                dialog.show();
            }
        }


        return view;
    }

    private final SongResponseListener listener = new SongResponseListener() {
        @Override
        public void onFetch(SongApiResponse response, String message) {
            if (response.getResults().isEmpty()) {
                Toast.makeText(getContext(), "No Songs Found", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            noSongsTv.setVisibility(View.GONE);
            showData(response.getResults());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(),"An error Occurred", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void showData(List<Result> results) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new MusicAdapter(getContext(), results, this);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();

    }


    @Override
    public void onClick(String albumId) {
        Intent intent = new Intent(getContext(), AlbumActivity.class);
        intent.putExtra("albumId", albumId);
        getContext().startActivity(intent);
    }
}