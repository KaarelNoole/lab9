package com.app.lab9;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class ForegroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final String CHANNEL_ID = "ChannelId1";
    public static final String CHANNEL_NAME = "Foreground service notification";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("input");
        createNotificationChannel();

        Intent intent1 = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent1, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Intent updateIntent = new Intent(this,ForegroundService.class);
        updateIntent.putExtra("input", "Update notification"+ getRandomNumber());
        PendingIntent update = PendingIntent.getService(this,0,updateIntent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(input)
                .setContentText(getString(R.string.front))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(R.mipmap.ic_launcher_round, "UPDATE", update)
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }
    private void  createNotificationChannel () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            serviceChannel.enableVibration(true);
            serviceChannel.setVibrationPattern(new long[]{
                100,200,300,400,500,400,300,200,400
            });
            serviceChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            serviceChannel.setBypassDnd(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    public int getRandomNumber(){
        int min = 1;
        int max = 100;
        Random random = new Random();
        return random.nextInt(max+min)+min;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();
    }
}
