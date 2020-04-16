package cat.coronout.workhabit.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import cat.coronout.workhabit.job.WorkhabitJobBuilder;
import cat.coronout.workhabit.notification.NotificationsManager;
import cat.coronout.workhabit.task.ScheduleNotificationTask;
import cat.coronout.workhabit.util.GlobalJobs;

/**
 * BroadcastReceiver
 *
 * It is used to catch alarm events. Workhabit schedule some different kind of alarms
 * and here, all types, are catched. We check what task need to do and request to do it.
 */
public class WorkhabitPublisher extends BroadcastReceiver {

    /**
     * onReceive
     *
     * Called by every broadcast call. We check here what kind of task we need to do
     * @param context Context
     * @param intent Call Intent instance
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(GlobalJobs.GLOBAL_JOBS_TAG, "WorkhabitPublisher onReceive: " + intent.getAction());
        if (intent.getIntExtra(GlobalJobs.SHOW_NOTIFICATION_ID_KEY, -1) == GlobalJobs.SHOW_NOTIFICATION_ID) {
            // Show notification
            Log.i(GlobalJobs.GLOBAL_JOBS_TAG, "WorkhabitPublisher onReceive: showNotification job");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = intent.getParcelableExtra(NotificationsManager.NOTIFICATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NotificationsManager.NOTIFICATION_CHANNEL_ID, NotificationsManager.NOTIFICATION_CHANNEL_NAME, importance);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            }
            int id = intent.getIntExtra(NotificationsManager.NOTIFICATION_ID, 0);
            assert notificationManager != null;
            notificationManager.notify(id, notification);
        } else if (intent.getIntExtra(GlobalJobs.CHECK_NOTIFICATION_PLANNING_KEY, -1) == GlobalJobs.CHECK_NOTIFICATION_PLANNING_ID) {
            // Setup jobs
            Log.i(GlobalJobs.GLOBAL_JOBS_TAG, "WorkhabitPublisher onReceive: setupJobs job");
            WorkhabitJobBuilder jobBuilder = WorkhabitJobBuilder.getInstance(context.getApplicationContext());
//            jobBuilder.createDailyAlarm();
            jobBuilder.buildNextJob();
        } else if (intent.getIntExtra(GlobalJobs.DO_JOB_EXECUTION_KEY, 1) == GlobalJobs.DO_JOB_EXECUTION_ID) {
            int jobId = intent.getIntExtra(GlobalJobs.JOB_ID_KEY, -1);
            WorkhabitJobBuilder jobBuilder = WorkhabitJobBuilder.getInstance(context.getApplicationContext());
            int nextJobId = jobBuilder.setupNextJob(jobId);
            ScheduleNotificationTask scheduleNotificationTask = new ScheduleNotificationTask(context.getApplicationContext(), jobId, nextJobId);
            scheduleNotificationTask.execute();
        } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(GlobalJobs.GLOBAL_JOBS_TAG, "WorkhabitPublisher onReceive: BOOT_COMPLETED");
        }
    }

}
