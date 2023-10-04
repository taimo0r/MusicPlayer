package com.taimoor.musicplayer.Models;

import java.util.List;

public class AlbumApiResponse {

    public String title;
    public String name;
    public String year;
    public String release_date;
    public String primary_artists;
    public String primary_artists_id;
    public String albumid;
    public String perma_url;
    public String image;
    public List<Song> songs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPrimary_artists() {
        return primary_artists;
    }

    public void setPrimary_artists(String primary_artists) {
        this.primary_artists = primary_artists;
    }

    public String getPrimary_artists_id() {
        return primary_artists_id;
    }

    public void setPrimary_artists_id(String primary_artists_id) {
        this.primary_artists_id = primary_artists_id;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getPerma_url() {
        return perma_url;
    }

    public void setPerma_url(String perma_url) {
        this.perma_url = perma_url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
