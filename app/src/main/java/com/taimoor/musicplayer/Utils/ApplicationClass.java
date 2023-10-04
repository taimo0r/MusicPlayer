package com.taimoor.musicplayer.Utils;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {

    public static final String CHANNEL_ID_1 = "channel1";
    public static final String CHANNEL_ID_2 = "channel2";
    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_NEXT = "actionnext";
    public static final String ACTION_PLAY = "actionplay";
    public static boolean shouldExecuteOnResume;

    @Override
    public void onCreate() {
        super.onCreate();
        shouldExecuteOnResume = false;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_1, "Channel(1)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Music Player Notification Service");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID_2, "Channel(2)", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Music Player Notification Service");

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
