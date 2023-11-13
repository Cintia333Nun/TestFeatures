package com.cin.testfeatures.remminders_work_manager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cin.testfeatures.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "Recordatorio_de_cobranza";
    private static final int NOTIFICATION_ID = 10;

    private final Context context;

    public NotificationHelper (Context context) {
        this.context = context;
    }

    public void createNotification(String title, String message) {
        // Create Remote Views
        RemoteViews expandedView = new RemoteViews(
                context.getPackageName(),
                R.layout.layout_remminder
        );
        RemoteViews collapsedView = new RemoteViews(
                context.getPackageName(),
                R.layout.layout_remminder_small
        );
        expandedView.setTextViewText(R.id.title,title);
        expandedView.setTextViewText(R.id.description,message);
        collapsedView.setTextViewText(R.id.title,title);
        collapsedView.setTextViewText(R.id.description,message);

        // Init Notification
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel =
                        new NotificationChannel(
                                CHANNEL_ID, "Recordatorio",
                                NotificationManager.IMPORTANCE_HIGH
                        );

                NotificationManager manager = (NotificationManager) this.context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(mChannel);
            }

            Intent intent = new Intent(context, RemmindersActivity.class);

            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent =  PendingIntent.getActivity(context, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandedView)
                    .build();
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
