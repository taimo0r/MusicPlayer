package com.taimoor.musicplayer.Models;

import java.util.List;

public class PlaylistApiResponse {

    public List<String> artists;
    public String listid;
    public String listname;
    public String perma_url;
    public String follower_count;
    public String uid;
    public String last_updated;
    public String username;
    public String firstname;
    public String lastname;
    public String is_followed;
    public boolean isFY;
    public String image;
    public String share;
    public List<Song> songs;
    public String type;
    public String list_count;
    public int fan_count;
    public String H2;
    public boolean is_dolby_playlist;
    public String subheading;
    public List<String> sub_types;

    public boolean video_available;
    public int video_count;

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getPerma_url() {
        return perma_url;
    }

    public void setPerma_url(String perma_url) {
        this.perma_url = perma_url;
    }

    public String getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(String follower_count) {
        this.follower_count = follower_count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(String is_followed) {
        this.is_followed = is_followed;
    }

    public boolean isFY() {
        return isFY;
    }

    public void setFY(boolean FY) {
        isFY = FY;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getList_count() {
        return list_count;
    }

    public void setList_count(String list_count) {
        this.list_count = list_count;
    }

    public int getFan_count() {
        return fan_count;
    }

    public void setFan_count(int fan_count) {
        this.fan_count = fan_count;
    }

    public String getH2() {
        return H2;
    }

    public void setH2(String h2) {
        H2 = h2;
    }

    public boolean isIs_dolby_playlist() {
        return is_dolby_playlist;
    }

    public void setIs_dolby_playlist(boolean is_dolby_playlist) {
        this.is_dolby_playlist = is_dolby_playlist;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public List<String> getSub_types() {
        return sub_types;
    }

    public void setSub_types(List<String> sub_types) {
        this.sub_types = sub_types;
    }


    public boolean isVideo_available() {
        return video_available;
    }

    public void setVideo_available(boolean video_available) {
        this.video_available = video_available;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }
}
