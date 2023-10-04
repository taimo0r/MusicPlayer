package com.taimoor.musicplayer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taimoor.musicplayer.Activities.PlayerActivity;
import com.taimoor.musicplayer.Database.AppDatabase;
import com.taimoor.musicplayer.Database.FavoriteSongs;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Utils.Common;

import java.util.List;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {

    Context context;
    List<FavoriteSongs> list;

    public FavoriteRecyclerAdapter(Context context, List<FavoriteSongs> songs) {
        this.context = context;
        this.list = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorites_recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String song, artist, duration, url, image;
        AppDatabase database = AppDatabase.getInstance(context);

        song = list.get(position).songName;
        artist = list.get(position).artist;
        duration = Common.convertSecondsToTime(Integer.parseInt(list.get(position).duration));
        image = list.get(position).image;
        url = list.get(position).url;

        song = song.replace("#", "");
        song = song.replace("&", "");
        song = song.replace(";", "");
        song = song.replace("039", "'");

        holder.fileName.setText(song);
        holder.artistName.setText(artist);
        holder.duration.setText(duration);

        Picasso.get().load(image).placeholder(R.drawable.music_img).into(holder.album_art);

        String finalSong = song;
        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayerActivity.class);

                intent.putExtra("pos", position);
                intent.putExtra("url", url);
                intent.putExtra("song", finalSong);
                intent.putExtra("artist", artist);
                intent.putExtra("duration", duration);
                intent.putExtra("durationSeconds", list.get(position).duration);
                intent.putExtra("image", image);
                intent.putExtra("isSaved", true);
                intent.putExtra("size", list.size());
                context.startActivity(intent);
            }
        });

        holder.popup.setOnClickListener(v -> {

            FavoriteSongs favoriteSong = list.get(holder.getAdapterPosition());

            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.remove_favorite_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        database.favoriteSongsDao().delete(favoriteSong);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Song Removed from favorites", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return false;
            });
            popupMenu.show();
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout songItem;
        TextView fileName, artistName, duration, totalSongs;
        ImageView album_art, popup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            songItem = itemView.findViewById(R.id.audio_item);
            artistName = itemView.findViewById(R.id.singer_name);
            duration = itemView.findViewById(R.id.song_duration);
            totalSongs = itemView.findViewById(R.id.total_songs);
            popup = itemView.findViewById(R.id.menu_favorites);

        }
    }
}
