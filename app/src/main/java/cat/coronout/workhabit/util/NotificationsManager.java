package cat.coronout.workhabit.util;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import cat.coronout.workhabit.R;
import cat.coronout.workhabit.receiver.WorkhabitPublisher;

public class NotificationsManager {

    private final String NOTIFICATION_CHANNEL_ID = "10001";
    private final String DEFAULT_NOTIFICATION_CHANNEL = "default";

    private Context context;

    private static NotificationsManager notificationsManager;

    public static NotificationsManager getInstance(Context context) {
        if (notificationsManager == null)
            notificationsManager = new NotificationsManager(context);
        return notificationsManager;
    }

    private NotificationsManager(Context context) {
        this.context = context;
    }

    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(context, WorkhabitPublisher.class);
    }

    private Notification getNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANNEL);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

}
