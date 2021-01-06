package com.example.wa_client;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundService extends Service {
    public BackgroundService() {
    }
    public ExecutorService sendMessageService, processResponseService;

    int mStartMode = START_STICKY;
    int counter =0;

    // interface for clients that bind.
    final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        BackgroundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BackgroundService.this;
        }
    }
    // indicates whether onRebind should be used
    boolean mAllowRebind = true;

    @Override
    public void onCreate() {
        Log.d("waclonedebug", "onCreate: Service ");
        // The service is being created.
    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags, int startId) {
//        onTaskRemoved(intent);
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String channelId = getString(R.string.app_name);
//        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
//        notificationManager.createNotificationChannel(notificationChannel);
//
//        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_layout);
//
//        Notification notification =
//                new Notification.Builder(this,channelId).setCustomContentView(remoteViews).build();
////                        .setContentTitle("hi")
////                        .setContentText("bye")
////                        .setContentIntent(pendingIntent)
////                        .setTicker("hello")
////                        .build();
//        startForeground(1, notification);
//
//        notificationManager.cancel(1);
        Log.d("waclonedebug", "onStart: Service ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(true)
                {
                    if(i%1000000000==0)
                        Log.d("waclonedebug", "service is up "+i);
                    i++;
                }
            }
        }).start();
        // The service is starting, due to a call to startService().
        return mStartMode;
    }
    private void initializations(){
        Log.d("waclonedebug", "In initializations");
        sendMessageService = Executors.newSingleThreadExecutor();
        processResponseService = Executors.newSingleThreadExecutor();

//        ReceivingThread receivingThread = new ReceivingThread(globalVariables);
//        receivingThread.start();



    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("waclonedebug", "onBind: Service ");
        // A client is binding to the service with bindService().
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("waclonedebug", "onUNBind: Service ");
        // All clients have unbound with unbindService()
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("waclonedebug", "onBind: Service ");

        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    @Override
    public void onDestroy() {
        Log.d("waclonedebug", "onDestroy: Service ");
        // The service is no longer used and is being destroyed
        Intent broadcastIntent = new Intent(this, RestartBackgroundServiceReceiver.class);
        sendBroadcast(broadcastIntent);
        Log.d("waclonedebug", "onDestroy: sendBroadcast ");

    }
}