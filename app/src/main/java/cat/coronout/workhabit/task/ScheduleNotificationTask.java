package cat.coronout.workhabit.task;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;

import cat.coronout.workhabit.R;
import cat.coronout.workhabit.notification.NotificationMessages;
import cat.coronout.workhabit.notification.NotificationsManager;
import cat.coronout.workhabit.util.GlobalJobs;

/**
 * Task to show notification for specific job
 */
public class ScheduleNotificationTask extends AsyncTask<Void, Void, Boolean> {

    /// Context
    private Context context;
    /// Job identifier
    private int jobId;
    /// Next job identifier
    private int nextJobId;
    /// Flag isTest to delete delays while showing notifications
    private boolean isTest;

    /// NotificationMessages instance to retrieve correct message body
    private NotificationMessages notificationMessages;
    /// NotificationBody instance
    private NotificationMessages.NotificationBody notificationBody;

    /**
     * Constructor
     * @param context Context
     * @param jobId Job identifier
     */
    public ScheduleNotificationTask(Context context, int jobId, int nextJobId) {
        this(context, jobId, nextJobId, false);
    }

    /**
     * Constructor
     * @param context Context
     * @param jobId Job identifier
     * @param isTest Flag test mode
     */
    public ScheduleNotificationTask(Context context, int jobId, int nextJobId, boolean isTest) {
        this.context = context;
        this.jobId = jobId;
        this.nextJobId = nextJobId;
        this.isTest = isTest;
    }

    /**
     * onPreExecute
     *
     * Before execute task, we need the instance of NotificationMessages
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.notificationMessages = NotificationMessages.getInstance(context);
    }

    /**
     * doInBackground
     *
     * Task body. Here, task is executed
     * @param voids Void params, we don't require any parameter
     * @return Boolean as success result
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        switch (jobId) {
            case GlobalJobs.START_WORK_JOB_ID:
                notificationBody = notificationMessages.getNotificationMessage(R.string.start_working, R.string.start_working_message);
                break;
            case GlobalJobs.TAKE_SHORT_BREAK_JOB_ID:
                notificationBody = notificationMessages.getNotificationMessage(R.string.take_break, R.string.take_break_short_message);
                break;
            case GlobalJobs.TAKE_LONG_BREAK_JOB_ID:
                notificationBody = notificationMessages.getNotificationMessage(R.string.take_break_long, R.string.take_break_long_message);
                break;
            case GlobalJobs.END_WORK_JOB_ID:
                notificationBody = notificationMessages.getNotificationMessage(R.string.finish_working, R.string.finish_working_message);
                if (nextJobId == GlobalJobs.START_WORK_JOB_ID)
                    notificationBody = notificationMessages.getNotificationMessage(R.string.finish_working, R.string.first_finish_working_message);
                break;
            case GlobalJobs.DO_EXERCISE_JOB_ID:
                notificationBody = notificationMessages.getNotificationMessage(R.string.practise_exercise, R.string.practise_exercise_message);
                break;
            case GlobalJobs.SOCIALIZE_JOB_ID:
                notificationBody = notificationMessages.getNotificationMessage(R.string.socialize, R.string.socialize_message);
                break;
            default:
                break;
        }
        return (notificationBody != null);
    }

    /**
     * onPostExecute
     *
     * After task execution we need to show the notification requested
     * @param aBoolean Boolean indication if task is succeded
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            // Prepare notification
            NotificationsManager manager = NotificationsManager.getInstance(context);
            Notification notification = manager.getNotification(notificationBody.getTitle(), notificationBody.getMessage());
            manager.scheduleNotification(notification, (isTest ? 0 : (1 * 1000)));
        }
    }

}
