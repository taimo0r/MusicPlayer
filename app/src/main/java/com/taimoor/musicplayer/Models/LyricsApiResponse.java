package com.taimoor.musicplayer.Models;

public class LyricsApiResponse {

    public String lyrics;
    public String script_tracking_url;
    public String lyrics_copyright;
    public String snippet;

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getScript_tracking_url() {
        return script_tracking_url;
    }

    public void setScript_tracking_url(String script_tracking_url) {
        this.script_tracking_url = script_tracking_url;
    }

    public String getLyrics_copyright() {
        return lyrics_copyright;
    }

    public void setLyrics_copyright(String lyrics_copyright) {
        this.lyrics_copyright = lyrics_copyright;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
