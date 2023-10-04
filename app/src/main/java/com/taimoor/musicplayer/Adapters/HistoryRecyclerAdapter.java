package com.taimoor.musicplayer.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.taimoor.musicplayer.Activities.MainActivity;
import com.taimoor.musicplayer.Activities.SearchActivity;
import com.taimoor.musicplayer.Database.SearchedSongs;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Utils.Common;

import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    Context context;
    List<SearchedSongs> list;

    public HistoryRecyclerAdapter(Context context, List<SearchedSongs> songs) {
        this.context = context;
        this.list = songs;
    }

    @NonNull
    @Override
    public HistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_history_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerAdapter.ViewHolder holder, int position) {

        SearchedSongs songs = list.get(position);

        holder.song.setText(Common.firstWordToUppercase(songs.song));

        holder.song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = holder.song.getText().toString();
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("query", query);
                

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Chip song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song = itemView.findViewById(R.id.word);
        }
    }
}