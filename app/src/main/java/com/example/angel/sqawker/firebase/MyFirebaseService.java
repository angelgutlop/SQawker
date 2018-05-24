package com.example.angel.sqawker.firebase;

import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.angel.sqawker.R;
import com.example.angel.sqawker.utils.DataBaseControl;
import com.example.angel.sqawker.utils.InstructorsInfo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.joda.time.DateTime;

import java.util.Map;

import timber.log.Timber;

public class MyFirebaseService extends FirebaseMessagingService {

    private String notificationChannelID = "new_mmess";
    private int notificationId = 0;


    public MyFirebaseService() {


    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        remoteMessage.getFrom();
        String mess = "";
        try {
            remoteMessage.getNotification().getBody();
            Timber.d("Mensaje recibido de " + remoteMessage.getFrom() + ": " + mess);
        } catch (NullPointerException npe) {

        }


        Map<String, String> data = remoteMessage.getData();
        if (data == null) return;

        for (String key : data.keySet()) {

            Timber.d("\t Recibido [%s] --> %s", key, data.get(key));
        }

        String author = data.get("author");
        String message = data.get("message");
        DateTime date = new DateTime(Long.parseLong(data.get("date")));
        String authorKey = data.get("authorKey");

        DataBaseControl.storeMessage(this, authorKey, author, date, message);
        muestraNotificacion(author, mess, InstructorsInfo.getInstructorImage(authorKey));
    }

    private void muestraNotificacion(String autor, String mess, Bitmap autorImage) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, notificationChannelID)
                .setSmallIcon(R.drawable.ic_duck)
                .setLargeIcon(autorImage)
                .setContentTitle(autor)
                .setContentText(mess)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());
    }
}
