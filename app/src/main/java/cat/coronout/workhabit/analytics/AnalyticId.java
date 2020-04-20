package cat.coronout.workhabit.analytics;

/**
 * AnalyticId abstract class to save all static identifiers
 * for all kind of analytic events
 */
public abstract class AnalyticId {

    /// Key for analytic when a daily alarm is scheduled
    public static final int DAILY_ALARM_ID = 1;
    /// Key for analytic when a job is scheduled
    public static final int JOB_ID = 2;
    /// Key for analytic when a boot completed event is scheduled
    public static final int BOOT_COMPLETED_ID = 3;
    /// Key for analytic when user save app settings
    public static final int SAVE_SETTINGS_ID = 4;
    /// Key for analytic when user restore app settings
    public static final int RESTORE_SETTINGS_ID = 5;
    /// Key for analytic when user delete app settings
    public static final int DELETE_SETTINGS_ID = 6;

}
