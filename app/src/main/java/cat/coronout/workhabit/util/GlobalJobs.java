package cat.coronout.workhabit.util;

/**
 * Global abstract class to save all static keys and identifiers
 * to deal with all kind of jobs
 */
public abstract class GlobalJobs {

    /// GlobalJobs tag; Used to show logs in logcat console
    public static final String GLOBAL_JOBS_TAG = "GLOBAL_JOBS";

    /// Key for SHOW_NOTIFICATION broadcast mode
    public static final String SHOW_NOTIFICATION_ID_KEY = "ShowNotificationId";
    /// Identifier for SHOW_NOTIFICATION broadcast mode
    public static final int SHOW_NOTIFICATION_ID = 1;

    /// Key for CANCEL_NOTIFICATIONS broadcast mode
    public static final String CANCEL_NOTIFICATIONS_ID_KEY = "CancelNotificationsId";
    /// Identifier for CANCEL_NOTIFICATIONS broadcast mode
    public static final int CANCEL_NOTIFICATIONS_ID = 2;

    /// Key for CHECK_NOTIFICATION_PLANNING broadcast mode
    public static final String CHECK_NOTIFICATION_PLANNING_KEY = "CheckNotificationPlanning";
    /// Identifier for CHECK_NOTIFICATION_PLANNING broadcast mode
    public static final int CHECK_NOTIFICATION_PLANNING_ID = 3;

    /// Key for DO_JOB_EXECUTION broadcast mode
    public static final String DO_JOB_EXECUTION_KEY = "DoJobExecution";
    /// Identifier for DO_JOB_EXECUTION broadcast mode
    public static final int DO_JOB_EXECUTION_ID = 4;

    /// Key for CANCEL_JOBS broadcast mode
    public static final String CANCEL_JOBS_KEY = "CancelJobsId";
    /// Identifier for CANCEL_JOBS broadcast mode
    public static final int CANCEL_JOBS_ID = 5;

    /// Request code used to show notifications
    public static final int REQUEST_CODE_NOTIFICATION = 0;
    /// Request code used to schedule a control daily alarm
    public static final int REQUEST_CODE_DAILY_ALARM = 2;
    /// Request code used to schedule a job execution
    public static final int REQUEST_CODE_EXECUTE_JOB = 3;

    /// Key to add the job id requested in broadcast call
    public static final String JOB_ID_KEY = "JobId";
    /// Identifier for job type: START_WORK
    public static final int START_WORK_JOB_ID = 10;
    /// Identifier for job type: TAKE_SHORT_BREAK
    public static final int TAKE_SHORT_BREAK_JOB_ID = 11;
    /// Identifier for job type: TAKE_LONG_BREAK
    public static final int TAKE_LONG_BREAK_JOB_ID = 12;
    /// Identifier for job type: END_WORK
    public static final int END_WORK_JOB_ID = 13;
    /// Identifier for job type: DO_EXERCISE
    public static final int DO_EXERCISE_JOB_ID = 14;
    /// Identifier for job type: SOCIALIZE
    public static final int SOCIALIZE_JOB_ID = 15;
}
