package com.example.myapplication;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification notification;
    NotificationChannel notificationChannel;

    private String channel_id = "1";

    @Override
    public void onCreate(){
        super.onCreate();

        Toast.makeText(getApplicationContext(),"요건 온크리에잇",Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent,flags,startId);

        Notifi_M=(NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        Toast.makeText(getApplicationContext(),"제발좀 성공하자",Toast.LENGTH_SHORT).show();
        thread.start();

        return START_NOT_STICKY;
    }


    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        //        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(android.os.Message msg) {

            Intent intent = new Intent(MyService.this.getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(MyService.this,0,intent,0);
            NotificationCompat.Builder builder;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                //Toast.makeText(getApplicationContext(),"제발좀 성공하자",Toast.LENGTH_SHORT).show();
                String CHANNEL_ID="channel2";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"channel2"
                        ,NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
                builder= new NotificationCompat.Builder(MyService.this,CHANNEL_ID);
                //Notifi_M.createNotificationChannel(notificationChannel);

                builder.setSmallIcon(R.drawable.hi)
                        .setContentText("hello")
                        .setContentTitle("i'm title")
                        .setContentIntent(pendingIntent)
                        .setProgress(0,0,false)
                        .setTicker("알림!!!")
                        .setAutoCancel(true);


                startForeground(1,builder.build());
                stopForeground(true);
                Notifi_M.notify(1,builder.build());

            }else{
                builder=new NotificationCompat.Builder(MyService.this);


                builder.setSmallIcon(R.drawable.hi)
                        .setContentText("hello")
                        .setContentTitle("i'm title")
                        .setContentIntent(pendingIntent)
                        .setProgress(0,0,false)
                        .setTicker("알림!!!")
                        .setAutoCancel(true);


                String CHANNEL_ID="channel2";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"channel2"
                        ,NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
                Notifi_M.createNotificationChannel(notificationChannel);

                startForeground(1,builder.build());
                stopForeground(true);
                Notifi_M.notify(1,builder.build());
            }

            //thread.startAgain();
            //토스트 띄우기
            Toast.makeText(MyService.this, "뜸?", Toast.LENGTH_LONG).show();
        }
    };
}
