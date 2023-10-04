package com.taimoor.musicplayer.Models;

import java.io.Serializable;
import java.util.List;

public class ArtistMap implements Serializable {

    public List<PrimaryArtist> primary_artists;
    public List<FeaturedArtist> featured_artists;
    public List<Artist> artists;

    public List<PrimaryArtist> getPrimary_artists() {
        return primary_artists;
    }

    public void setPrimary_artists(List<PrimaryArtist> primary_artists) {
        this.primary_artists = primary_artists;
    }

    public List<FeaturedArtist> getFeatured_artists() {
        return featured_artists;
    }

    public void setFeatured_artists(List<FeaturedArtist> featured_artists) {
        this.featured_artists = featured_artists;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
