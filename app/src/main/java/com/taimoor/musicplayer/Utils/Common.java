package com.taimoor.musicplayer.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Common {

    public static String convertSecondsToTime(int totalSeconds){

        String timeString;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        timeString = String.format("%02d:%02d", minutes, seconds);

        return timeString;

    }


    public static String formattedTime(int currentPos) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPos % 60);
        String minutes = String.valueOf(currentPos / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    public static String convertToKilo(int number){
        String kilo;
        kilo = String.valueOf(number/1000);
        return kilo;
    }

    public static String firstWordToUppercase(String s){

        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();

    }




}
