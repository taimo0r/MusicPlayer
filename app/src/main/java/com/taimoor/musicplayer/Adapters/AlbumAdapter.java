package com.taimoor.musicplayer.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
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

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.taimoor.musicplayer.Database.AppDatabase;
import com.taimoor.musicplayer.Database.FavoriteSongs;
import com.taimoor.musicplayer.Listeners.onSongClick;
import com.taimoor.musicplayer.Models.Song;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Utils.Common;
import com.taimoor.musicplayer.Utils.DataHandlers;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Song> list;
    onSongClick clickListener;

    public AlbumAdapter(Context context, List<Song> list, onSongClick clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppDatabase database = AppDatabase.getInstance(context);


        String time = Common.convertSecondsToTime(Integer.parseInt(list.get(position).getDuration()));
        String name = list.get(position).getSong();

        name = name.replace("#", "");
        name = name.replace("&", "");
        name = name.replace(";", "");
        name = name.replace("039", "'");


        holder.fileName.setText(name);
        holder.fileName.setSelected(true);

        Glide.with(context).load(list.get(position).getImage()).into(holder.album_art);
        //Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.music).into(holder.album_art);
        holder.artist.setText(list.get(position).getPrimary_artists());
        holder.artist.setSelected(true);
        holder.duration.setText(time);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        String finalName = name;

        SharedPreferences preferences = context.getSharedPreferences("My_prefs", Context.MODE_PRIVATE);
        String playingSong = preferences.getString("playing", null);
        String newName = list.get(position).getId();


        if (newName.equals(playingSong)) {
            holder.spinKitView.setVisibility(View.VISIBLE);
        } else {
            holder.spinKitView.setVisibility(View.GONE);
        }

        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickListener.onSongItemClick(position);

                if (newName.equals(playingSong)) {
                    holder.spinKitView.setVisibility(View.VISIBLE);
                } else {
                    holder.spinKitView.setVisibility(View.GONE);
                }

                SharedPreferences.Editor editor = context.getSharedPreferences("My_prefs", Context.MODE_PRIVATE).edit();
                editor.putString("playing", list.get(position).getId());
                editor.apply();

            }
        });

        String url = list.get(position).getEncrypted_media_url();

        if (url.equals(database.favoriteSongsDao().getSong(name))) {

            holder.favoriteBtn.setVisibility(View.VISIBLE);

        } else {

            holder.favoriteBtn.setVisibility(View.INVISIBLE);
        }

        String finalName1 = name;
        holder.popMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.pending_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_download:
                        Uri uri = null;
                        try {
                            uri = Uri.parse(DataHandlers.downloadLinkDecrypt(list.get(position).getEncrypted_media_url()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        DownloadManager.Request request = new DownloadManager.Request(uri);

                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setTitle(finalName)
                                .setDescription(list.get(position).getPrimary_artists())
                                .setVisibleInDownloadsUi(true)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "MusicPlayer/" + finalName + ".mp3");

                        downloadManager.enqueue(request);

                        Toast.makeText(context, "Download Started", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_add_to_favorites:
                        if (url.equals(database.favoriteSongsDao().getSong(finalName1))) {

                            holder.favoriteBtn.setVisibility(View.VISIBLE);

                            Toast.makeText(context, "Already Added to Favourites", Toast.LENGTH_SHORT).show();

                        } else {

                            FavoriteSongs songs = new FavoriteSongs();
                            songs.artist = list.get(position).getPrimary_artists();
                            songs.songName = finalName;
                            songs.position = position;
                            songs.image = list.get(position).getImage();
                            songs.duration = list.get(position).getDuration();
                            songs.url = list.get(position).getEncrypted_media_url();

                            database.favoriteSongsDao().SaveFavoriteSong(songs);

                            holder.favoriteBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        }

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
        TextView fileName, artist, duration;
        ImageView album_art, favoriteBtn;
        SpinKitView spinKitView;
        ImageView popMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            songItem = itemView.findViewById(R.id.audio_item);
            artist = itemView.findViewById(R.id.singer_name);
            popMenu = itemView.findViewById(R.id.pop_menu);
            duration = itemView.findViewById(R.id.song_duration);
            favoriteBtn = itemView.findViewById(R.id.favorite_btn);
            spinKitView = itemView.findViewById(R.id.spinKitView);


        }
    }
}
