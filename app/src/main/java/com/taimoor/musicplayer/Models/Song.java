package com.taimoor.musicplayer.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Song implements Serializable{

    public String id;
    public String type;
    public String song;
    public String album;
    public String year;
    public String music;
    public String music_id;
    public String primary_artists;
    public String primary_artists_id;
    public String featured_artists;
    public String featured_artists_id;
    public String singers;
    public String starring;
    public String image;
    public String label;
    public String albumid;
    public String language;
    public String origin;
    public String play_count;
    public String copyright_text;
    public String _320kbps;
    public boolean is_dolby_content;
    public int explicit_content;
    public String has_lyrics;
    public String lyrics_snippet;
    public String encrypted_media_url;
    public String encrypted_media_path;
    public String media_preview_url;
    public String perma_url;
    public String album_url;
    public String duration;
    public Rights rights;
    public boolean webp;
    public String starred;
    public ArtistMap artistMap;
    public String release_date;
    public String vcode;
    public String vlink;
    public boolean triller_available;
    public String label_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getMusic_id() {
        return music_id;
    }

    public void setMusic_id(String music_id) {
        this.music_id = music_id;
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

    public String getFeatured_artists() {
        return featured_artists;
    }

    public void setFeatured_artists(String featured_artists) {
        this.featured_artists = featured_artists;
    }

    public String getFeatured_artists_id() {
        return featured_artists_id;
    }

    public void setFeatured_artists_id(String featured_artists_id) {
        this.featured_artists_id = featured_artists_id;
    }

    public String getSingers() {
        return singers;
    }

    public void setSingers(String singers) {
        this.singers = singers;
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public String getCopyright_text() {
        return copyright_text;
    }

    public void setCopyright_text(String copyright_text) {
        this.copyright_text = copyright_text;
    }

    public String get_320kbps() {
        return _320kbps;
    }

    public void set_320kbps(String _320kbps) {
        this._320kbps = _320kbps;
    }

    public boolean isIs_dolby_content() {
        return is_dolby_content;
    }

    public void setIs_dolby_content(boolean is_dolby_content) {
        this.is_dolby_content = is_dolby_content;
    }

    public int getExplicit_content() {
        return explicit_content;
    }

    public void setExplicit_content(int explicit_content) {
        this.explicit_content = explicit_content;
    }

    public String getHas_lyrics() {
        return has_lyrics;
    }

    public void setHas_lyrics(String has_lyrics) {
        this.has_lyrics = has_lyrics;
    }

    public String getLyrics_snippet() {
        return lyrics_snippet;
    }

    public void setLyrics_snippet(String lyrics_snippet) {
        this.lyrics_snippet = lyrics_snippet;
    }

    public String getEncrypted_media_url() {
        return encrypted_media_url;
    }

    public void setEncrypted_media_url(String encrypted_media_url) {
        this.encrypted_media_url = encrypted_media_url;
    }

    public String getEncrypted_media_path() {
        return encrypted_media_path;
    }

    public void setEncrypted_media_path(String encrypted_media_path) {
        this.encrypted_media_path = encrypted_media_path;
    }

    public String getMedia_preview_url() {
        return media_preview_url;
    }

    public void setMedia_preview_url(String media_preview_url) {
        this.media_preview_url = media_preview_url;
    }

    public String getPerma_url() {
        return perma_url;
    }

    public void setPerma_url(String perma_url) {
        this.perma_url = perma_url;
    }

    public String getAlbum_url() {
        return album_url;
    }

    public void setAlbum_url(String album_url) {
        this.album_url = album_url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Rights getRights() {
        return rights;
    }

    public void setRights(Rights rights) {
        this.rights = rights;
    }

    public boolean isWebp() {
        return webp;
    }

    public void setWebp(boolean webp) {
        this.webp = webp;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }

    public ArtistMap getArtistMap() {
        return artistMap;
    }

    public void setArtistMap(ArtistMap artistMap) {
        this.artistMap = artistMap;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getVlink() {
        return vlink;
    }

    public void setVlink(String vlink) {
        this.vlink = vlink;
    }

    public boolean isTriller_available() {
        return triller_available;
    }

    public void setTriller_available(boolean triller_available) {
        this.triller_available = triller_available;
    }

    public String getLabel_url() {
        return label_url;
    }

    public void setLabel_url(String label_url) {
        this.label_url = label_url;
    }


}
