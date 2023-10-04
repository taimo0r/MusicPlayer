package com.taimoor.musicplayer.Models;

import java.util.List;

public class LaunchDataAPI {

    public List<String> history;
    public List<NewTrending> new_trending;
    public List<TopPlaylist> top_playlists;
    public List<NewAlbum> new_albums;
//    public List<BrowseDiscover> browse_discover;
//    public GlobalConfig global_config;
    public List<Chart> charts;
//    public List<Radio> radio;
//    public List<ArtistReco> artist_recos;
//    public List<TagMix> tag_mixes;
//    public List<CityMod> city_mod;


    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public List<NewTrending> getNew_trending() {
        return new_trending;
    }

    public void setNew_trending(List<NewTrending> new_trending) {
        this.new_trending = new_trending;
    }

    public List<TopPlaylist> getTop_playlists() {
        return top_playlists;
    }

    public void setTop_playlists(List<TopPlaylist> top_playlists) {
        this.top_playlists = top_playlists;
    }

    public List<NewAlbum> getNew_albums() {
        return new_albums;
    }

    public void setNew_albums(List<NewAlbum> new_albums) {
        this.new_albums = new_albums;
    }

    public List<Chart> getCharts() {
        return charts;
    }

    public void setCharts(List<Chart> charts) {
        this.charts = charts;
    }
}
