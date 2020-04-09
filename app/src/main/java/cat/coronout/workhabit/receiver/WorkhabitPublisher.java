package cat.coronout.workhabit.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import cat.coronout.workhabit.util.NotificationsManager;

public class WorkhabitPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NotificationsManager.NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NotificationsManager.NOTIFICATION_ID, NotificationsManager.NOTIFICATION_CHANNEL_NAME, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        int id = intent.getIntExtra(NotificationsManager.NOTIFICATION_ID, 0);
        assert notificationManager != null;
        notificationManager.notify(id, notification);
    }

}
