package com.taimoor.musicplayer.Utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DataHandlers {

    public static String albumApiLink = "https://www.jiosaavn.com/api.php?_format=json&__call=content.getAlbumDetails&albumid=";
    public static String searchApiLink = "https://www.jiosaavn.com/api.php?_format=json&__call=autocomplete.get&query=";
    public static String playlistApiLink = "https://www.jiosaavn.com/api.php?_format=json&__call=playlist.getDetails&listid=";
    public static String artistApiLink = "";
    public static String songApiLink = "https://www.jiosaavn.com/api.php?app_version=5.18.3&api_version=4&readable_version=5.18.3&v=79&_format=json&__call=song.getDetails&pids=";
    public static String songssearchLink = "https://www.jiosaavn.com/search/";
    public static String albumssearchLink = "https://www.jiosaavn.com/search/album/";
    public static String playlistssearchLink = "https://www.jiosaavn.com/search/playlist/";
    public static String songsearchapi = "https://www.jiosaavn.com/api.php?p=1&_format=json&_marker=0&api_version=4&ctx=web6dot0&n=20&__call=search.getResults&q=";
    public static String albumssearchapi = "https://www.jiosaavn.com/api.php?p=1&_format=json&_marker=0&api_version=4&ctx=web6dot0&n=20&__call=search.getAlbumResults&q=";
    public static String playlistssearchapi = "https://www.jiosaavn.com/api.php?p=1&_format=json&_marker=0&api_version=4&ctx=web6dot0&n=20&__call=search.getPlaylistResults&q=";
    public static String lyrics_api = "https://www.jiosaavn.com/api.php?__call=lyrics.getLyrics&ctx=web6dot0&api_version=4&_format=json&_marker=0&lyrics_id=";
    public static String dot = " • ";

    public static String downloadLinkDecrypt(String encData)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
                InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            String key = "38346591";
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher
                    .doFinal(Base64.decode(encData.getBytes(), 0));
            return new String(original).trim();
        }

}
