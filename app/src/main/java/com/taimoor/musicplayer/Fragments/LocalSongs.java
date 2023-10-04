package com.taimoor.musicplayer.Fragments;

import static com.taimoor.musicplayer.Activities.LocalSongs.musicFiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taimoor.musicplayer.Adapters.LocalMusicAdapter;
import com.taimoor.musicplayer.R;

public class LocalSongs extends Fragment {

    RecyclerView recyclerView;
    LocalMusicAdapter adapter;

    public LocalSongs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_songs, container, false);

        recyclerView = view.findViewById(R.id.local_songs_recycler);
        recyclerView.setHasFixedSize(true);

        adapter = new LocalMusicAdapter(getContext(), musicFiles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        return view;

    }
}