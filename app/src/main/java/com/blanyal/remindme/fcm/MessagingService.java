package com.blanyal.remindme.fcm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.blanyal.remindme.MainActivity;
import com.blanyal.remindme.R;
import com.blanyal.remindme.Reminder;
import com.blanyal.remindme.ReminderDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by carlosmendes on 10/23/16.
 */

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        Log.d("Message receive from", remoteMessage.getFrom());
        sendNotification(notification, data);
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        saveReminder(notification);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, Activity.RESULT_OK, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setLargeIcon(icon)
                .setColor(Color.YELLOW)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher);

        try {
            String picture_url = data.get("picture_url");
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    public void saveReminder(RemoteMessage.Notification notification) {
        Reminder reminder = new Reminder();
        reminder.setActive(Boolean.TRUE.toString());
        reminder.setRepeat(Boolean.FALSE.toString());
        reminder.setTitle(notification.getTitle() + notification.getBody());

        Calendar calendar = Calendar.getInstance();
        String sDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        String sTime;
        int minute = calendar.get(Calendar.MINUTE);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (minute < 10) {
            sTime = hourOfDay + ":" + "0" + minute;
        } else {
            sTime = hourOfDay + ":" + minute;
        }
        reminder.setDate(sDate);
        reminder.setTime(sTime);

        ReminderDatabase database = new ReminderDatabase(this);
        database.addReminder(reminder);
    }
}
