package com.example.firebasetest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    FirebaseAnalytics firebaseAnalytics;
    FirebaseApp firebaseApp;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        showNotification(title, body);
        firebaseAnalytics.getInstance(this); // Firebase Analytics 초기화
        firebaseApp.initializeApp(this); // FirebaseApp 초기화
    }


    private void showNotification(String a, String b) {

        Log.e("안녕", "hello");
        Intent intent = new Intent(this, HomeActivity.class); // 원하는 대상 액티비티로 변경
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Arrr 경매알림"; //채널 ID
            CharSequence channelName = "ItemOn"; //채널 이름
            String channelDescription = "BiddingItemOn"; //채널 설명
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Arrr 경매알림")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(a)
                .setContentText(b)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}