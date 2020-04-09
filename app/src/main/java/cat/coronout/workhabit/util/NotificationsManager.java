package cat.coronout.workhabit.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import cat.coronout.workhabit.R;
import cat.coronout.workhabit.receiver.WorkhabitPublisher;

public class NotificationsManager {

    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String DEFAULT_NOTIFICATION_CHANNEL = "default";
    public static final String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_WORKHABIT";
    public static final String NOTIFICATION_ID = "workhabit_notification_id";
    public static final String NOTIFICATION = "notification";

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

    public void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(context, WorkhabitPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANNEL);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

}
