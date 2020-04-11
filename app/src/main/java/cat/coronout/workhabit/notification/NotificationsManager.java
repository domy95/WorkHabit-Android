package cat.coronout.workhabit.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import cat.coronout.workhabit.R;
import cat.coronout.workhabit.receiver.WorkhabitPublisher;
import cat.coronout.workhabit.util.GlobalJobs;
import cat.coronout.workhabit.util.Utils;

/**
 * NotificationsManager
 *
 * It can be called NotificationsHelper. Class use it to centralize
 * Notification creations and schedules.
 */
public class NotificationsManager {

    /// Notification channel id, used in android newest versions
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    /// Notification channel name, used in android newest versions
    public static final String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_WORKHABIT";
    /// Notification id key, used to send notification id in the intent extras
    public static final String NOTIFICATION_ID = "workhabit_notification_id";
    /// Notification key, used to send notification in the intent extras
    public static final String NOTIFICATION = "notification";

    /// Context
    private Context context;

    /// NotificationsManager instance, it's a singleton
    private static NotificationsManager notificationsManager;

    /**
     * Static method to get NotificationsManager instance
     * @param context Context
     * @return NotificationsManager instance
     */
    public static NotificationsManager getInstance(Context context) {
        if (notificationsManager == null)
            notificationsManager = new NotificationsManager(context);
        return notificationsManager;
    }

    /**
     * Private constructor
     * @param context Context
     */
    private NotificationsManager(Context context) {
        this.context = context;
    }

    /**
     * Schedule notification. It schedule a notification using WorkhabitPublisher.
     * Schedule all notifications in broadcast mode: SHOW_NOTIFICATION
     * @param notification Notifications to schedule
     * @param delay Possible delay in milliseconds
     */
    public void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(context, WorkhabitPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NOTIFICATION, notification);
        notificationIntent.putExtra(GlobalJobs.SHOW_NOTIFICATION_ID_KEY, GlobalJobs.SHOW_NOTIFICATION_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, GlobalJobs.REQUEST_CODE_NOTIFICATION, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    /**
     * Build a notification with custom title and message
     * @param title Notification title
     * @param message Notification message
     * @return Notifications instance
     */
    public Notification getNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setLargeIcon(Utils.getBitmap(context, R.mipmap.ic_launcher));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setVibrate(new long[] {1000, 1000});
        builder.setLights(Color.RED, 3000, 3000);
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        return builder.build();
    }

}
