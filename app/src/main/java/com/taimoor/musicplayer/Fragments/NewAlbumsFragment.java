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

import com.taimoor.musicplayer.Adapters.NewAlbumsAdapter;
import com.taimoor.musicplayer.Models.LaunchDataAPI;
import com.taimoor.musicplayer.Models.NewAlbum;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Retrofit.ApiClient;
import com.taimoor.musicplayer.Retrofit.LaunchDataListener;

import java.util.List;

public class NewAlbumsFragment extends Fragment {

    RecyclerView newAlbumsRecycler;
    NewAlbumsAdapter albumsAdapter;
    ProgressDialog dialog;
    ApiClient client;
    TextView noSongsTv;

    public NewAlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_albums, container, false);

        newAlbumsRecycler = view.findViewById(R.id.new_albums_recycler);
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
                Toast.makeText(getContext(), "No Albums Found for this Language", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            noSongsTv.setVisibility(View.GONE);
            showNewAlbums(response.getNew_albums());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), "An error Occurred", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void showNewAlbums(List<NewAlbum> new_albums) {
        newAlbumsRecycler.setHasFixedSize(true);
        newAlbumsRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        albumsAdapter = new NewAlbumsAdapter(getContext(), new_albums);
        newAlbumsRecycler.setAdapter(albumsAdapter);
        dialog.dismiss();
    }
}