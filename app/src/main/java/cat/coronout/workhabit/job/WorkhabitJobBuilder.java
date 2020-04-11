package cat.coronout.workhabit.job;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import java.util.Calendar;

import cat.coronout.workhabit.model.Schedule;
import cat.coronout.workhabit.model.Setting;
import cat.coronout.workhabit.receiver.WorkhabitPublisher;
import cat.coronout.workhabit.storage.LocalStorage;
import cat.coronout.workhabit.util.GlobalJobs;
import cat.coronout.workhabit.util.Utils;

public class WorkhabitJobBuilder {

    /// WorkhabitJobBuilder instance; It's a singleton
    private static WorkhabitJobBuilder scheduler;

    /// Context
    private Context context;

    /**
     * Public static method to get WorkhabitJobBuilder instance
     * @param context Context
     * @return WorkhabitJobBuilder instance
     */
    public static WorkhabitJobBuilder getInstance(Context context) {
        if (scheduler == null)
            scheduler = new WorkhabitJobBuilder(context);
        return scheduler;
    }

    /**
     * Private constructor
     * @param context Context
     */
    private WorkhabitJobBuilder(Context context) {
        this.context = context;
    }

    /**
     * Cancel control daily alarm method
     *
     * It cancel the control daily alarm before schedule it another time
     */
    private void cancelDailyAlarm() {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, WorkhabitPublisher.class);
            intent.putExtra(GlobalJobs.CANCEL_NOTIFICATIONS_ID_KEY, GlobalJobs.CANCEL_NOTIFICATIONS_ID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, GlobalJobs.REQUEST_CODE_DAILY_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            assert alarmManager != null;
            alarmManager.cancel(pendingIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Schedule control daily alarm
     *
     * Before reschedule it, it was cancelled
     */
    public void createDailyAlarm() {
        this.cancelDailyAlarm();
        Intent intent = new Intent(context, WorkhabitPublisher.class);
        intent.putExtra(GlobalJobs.CHECK_NOTIFICATION_PLANNING_KEY, GlobalJobs.CHECK_NOTIFICATION_PLANNING_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, GlobalJobs.REQUEST_CODE_DAILY_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = getDailyConfigurationCalendar();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Cancel previous scheduled job
     *
     * Used it before reschedule another job or before change/remove saved settings
     */
    public void cancelJob() {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, WorkhabitPublisher.class);
            intent.putExtra(GlobalJobs.CANCEL_JOBS_KEY, GlobalJobs.CANCEL_JOBS_ID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, GlobalJobs.REQUEST_CODE_EXECUTE_JOB, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            assert alarmManager != null;
            alarmManager.cancel(pendingIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Schedule specified job
     * @param jobId Job identifier
     * @param calendar Calendar setted with correctly date that the job will be run
     */
    private void buildJob(int jobId, Calendar calendar) {
        this.cancelJob();
        if (calendar != null) {
//            long millis = SystemClock.elapsedRealtime() + (System.currentTimeMillis() - calendar.getTimeInMillis());
            long millis = calendar.getTimeInMillis();
            Intent intent = new Intent(context, WorkhabitPublisher.class);
            intent.putExtra(GlobalJobs.DO_JOB_EXECUTION_KEY, GlobalJobs.DO_JOB_EXECUTION_ID);
            intent.putExtra(GlobalJobs.JOB_ID_KEY, jobId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, GlobalJobs.REQUEST_CODE_EXECUTE_JOB, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(millis, null);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        }
    }

    /**
     * Getter for calendar instance to use for schedule
     * control daily alarm
     * @return Calendar instance
     */
    private Calendar getDailyConfigurationCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        return calendar;
    }

    /**
     * Search next workable schedule in saved setting
     * @param setting Saved setting
     * @param weekDay From which day of the week we need to start the search
     * @return Next workable schedule, if exist; null otherwise
     */
    private Schedule getNextWorkableSchedule(Setting setting, int weekDay) {
        if (setting == null) return null;
        if (weekDay < 1 || weekDay > 7) return null;
        boolean ended = false;
        boolean isWorkable = false;
        Schedule nextSchedule = null;
        int tmpWeekDay = weekDay;
        do {
            tmpWeekDay = (tmpWeekDay + 1) > 7 ? 1 : (tmpWeekDay + 1);
            ended = (tmpWeekDay == weekDay);
            if (!ended) {
                nextSchedule = setting.getSchedule(tmpWeekDay);
                isWorkable = (nextSchedule != null) && nextSchedule.isEnabled();
                // To be a workable day needs a startWorking hour
                isWorkable = (isWorkable && (!TextUtils.isEmpty(nextSchedule.getStartHourMorning()) || !TextUtils.isEmpty(nextSchedule.getStartHourAfternoon())));
            }
        } while (!isWorkable && !ended);
        if (isWorkable) return nextSchedule;
        return null;
    }

    /**
     * Build next job for specified schedule
     * @param schedule Schedule where to schedule the next job
     * @param todayWeekDay Today day of week
     */
    private void buildNextJobForSchedule(Schedule schedule, int todayWeekDay) {
        if (schedule == null) return;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // If isn't alarm for today, next alarm sure it's for start working
        if (todayWeekDay != schedule.getWeekDay()) {
            String startWorkingHour = (TextUtils.isEmpty(schedule.getStartHourMorning()) ? schedule.getStartHourAfternoon() : schedule.getStartHourMorning());
            Utils.setupCorrectNextDate(calendar, schedule.getWeekDay());
            calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(startWorkingHour));
            calendar.set(Calendar.MINUTE, Utils.getMinute(startWorkingHour));
            buildJob(GlobalJobs.START_WORK_JOB_ID, calendar);
        }
        // Otherwise we need to check what time is now to schedule the correct job
        else {
            String strNow = Utils.getUserHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            String strStartHourMorning = (TextUtils.isEmpty(schedule.getStartHourMorning()) ? "" :  Utils.getUserHour(Utils.getHour(schedule.getStartHourMorning()), Utils.getMinute(schedule.getStartHourMorning())));
            String strStartHourAfternoon = (TextUtils.isEmpty(schedule.getStartHourAfternoon()) ? "" : Utils.getUserHour(Utils.getHour(schedule.getStartHourAfternoon()), Utils.getMinute(schedule.getStartHourAfternoon())));
            String endHourMorning = (TextUtils.isEmpty(schedule.getEndHourMorning()) ? "" : Utils.getUserHour(Utils.getHour(schedule.getEndHourMorning()), Utils.getMinute(schedule.getEndHourMorning())));
            String endHourAfternoon = (TextUtils.isEmpty(schedule.getEndHourAfternoon()) ? "" : Utils.getUserHour(Utils.getHour(schedule.getEndHourAfternoon()), Utils.getMinute(schedule.getEndHourAfternoon())));
            if (!TextUtils.isEmpty(strStartHourMorning) && (strNow.compareTo(strStartHourMorning) < 0)) {
                calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(strStartHourMorning));
                calendar.set(Calendar.MINUTE, Utils.getMinute(strStartHourMorning));
                buildJob(GlobalJobs.START_WORK_JOB_ID, calendar);
            }
            else if (!TextUtils.isEmpty(schedule.getEndHourMorning()) && (strNow.compareTo(endHourMorning) < 0)) {
                // There're three possibilities: Difference greather than (or equal) 2 hours => SHORT_BREAK || LONG_BREAK (depending on start work difference); otherwise END_WORK
                long nowDifferenceEndWork = Utils.getHourDifferenceBetweenTwoDates(Utils.getDateFromHour(strNow), Utils.getDateFromHour(schedule.getEndHourMorning()));
                if (nowDifferenceEndWork >= 2) {
                    long startWorkDifferenceNow = Utils.getHourDifferenceBetweenTwoDates(Utils.getDateFromHour(schedule.getStartHourMorning()), Utils.getDateFromHour(strNow));
                    calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(strNow));
                    calendar.set(Calendar.MINUTE, Utils.getMinute(strNow));
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    if (startWorkDifferenceNow % 2 == 0) {
                        buildJob(GlobalJobs.TAKE_SHORT_BREAK_JOB_ID, calendar);
                    } else {
                        buildJob(GlobalJobs.TAKE_LONG_BREAK_JOB_ID, calendar);
                    }
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(schedule.getEndHourMorning()));
                    calendar.set(Calendar.MINUTE, Utils.getMinute(schedule.getEndHourMorning()));
                    buildJob(GlobalJobs.END_WORK_JOB_ID, calendar);
                }
            }
            else if (!TextUtils.isEmpty(strStartHourAfternoon) && (strNow.compareTo(strStartHourAfternoon) < 0)) {
                calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(strStartHourAfternoon));
                calendar.set(Calendar.MINUTE, Utils.getMinute(strStartHourAfternoon));
                buildJob(GlobalJobs.START_WORK_JOB_ID, calendar);
            }
            else if (!TextUtils.isEmpty(schedule.getEndHourAfternoon()) && (strNow.compareTo(endHourAfternoon) < 0)) {
                // There're three possibilities: Difference greather than (or equal) 2 hours => SHORT_BREAK || LONG_BREAK (depending on start work difference); otherwise END_WORK
                long nowDifferenceEndWork = Utils.getHourDifferenceBetweenTwoDates(Utils.getDateFromHour(strNow), Utils.getDateFromHour(schedule.getEndHourAfternoon()));
                if (nowDifferenceEndWork >= 2) {
                    long startWorkDifferenceNow = Utils.getHourDifferenceBetweenTwoDates(Utils.getDateFromHour(schedule.getStartHourAfternoon()), Utils.getDateFromHour(strNow));
                    calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(strNow));
                    calendar.set(Calendar.MINUTE, Utils.getMinute(strNow));
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    if (startWorkDifferenceNow % 2 == 0) {
                        buildJob(GlobalJobs.TAKE_SHORT_BREAK_JOB_ID, calendar);
                    } else {
                        buildJob(GlobalJobs.TAKE_LONG_BREAK_JOB_ID, calendar);
                    }
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(schedule.getEndHourAfternoon()));
                    calendar.set(Calendar.MINUTE, Utils.getMinute(schedule.getEndHourAfternoon()));
                    buildJob(GlobalJobs.END_WORK_JOB_ID, calendar);
                }
            } else {
                // There's an error; Before call this method it check if now is before last endWork hour
                return;
            }
        }
    }

    /**
     * Build next job from now.
     *
     * Checks now hour and calculate when can be scheduled
     * the new job.
     *
     * Search schedule for today and use `buildNextJobForSchedule` to schedule it.
     * If today isn't a workable day call `buildNextJobForSchedule` using `getNextWorkableSchedule`
     * to search the correct schedule.
     */
    public void buildNextJob() {
        LocalStorage storage = LocalStorage.getInstance(context);
        Setting setting = storage.getCurrentSetting();
        int weekDay = Utils.getCurrentWeekDay();
        Schedule schedule = setting.getSchedule(weekDay);

        // Check if today schedule is valid to create next alarms
        if (schedule != null && (!TextUtils.isEmpty(schedule.getEndHourMorning()) || !TextUtils.isEmpty(schedule.getEndHourAfternoon()))) {
            // Set now date
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String now = Utils.getUserHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            String endHourMorning = Utils.getUserHour(Utils.getHour(schedule.getEndHourMorning()), Utils.getMinute(schedule.getEndHourMorning()));
            String endHourAfternoon = Utils.getUserHour(Utils.getHour(schedule.getEndHourAfternoon()), Utils.getMinute(schedule.getEndHourAfternoon()));
            String lastHour = TextUtils.isEmpty(schedule.getEndHourAfternoon()) ? endHourMorning : endHourAfternoon;
            // Check if is a workable day
            if (schedule.isEnabled()) {
                // Check if now is before (or equal) to the end workable hour
                if (now.compareTo(lastHour) <= 0) {
                    buildNextJobForSchedule(schedule, weekDay);
                }
                // Set next alarm for next workable day
                else {
                    buildNextJobForSchedule(getNextWorkableSchedule(setting, weekDay), weekDay);
                }
            }
            // Set next alarm for next workable day
            else {
                buildNextJobForSchedule(getNextWorkableSchedule(setting, weekDay), weekDay);
            }
        }
        // Set next alarm for next workable day
        else {
            buildNextJobForSchedule(getNextWorkableSchedule(setting, weekDay), weekDay);
        }
    }

    /**
     * Build next job from job identifier.
     *
     * It's used after run job identified by `jobId`.
     *
     * For example after END_WORKING job (`jobId`) we need to schedule DO_EXERCISE job
     * @param jobId Job identifier
     */
    public void setupNextJob(int jobId) {
        LocalStorage storage = LocalStorage.getInstance(context);
        Setting setting = storage.getCurrentSetting();
        int weekDay = Utils.getCurrentWeekDay();
        Schedule schedule = setting.getSchedule(weekDay);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String strNow = Utils.getUserHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        String endHourMorning = Utils.getUserHour(Utils.getHour(schedule.getEndHourMorning()), Utils.getMinute(schedule.getEndHourMorning()));
        String endHourAfternoon = Utils.getUserHour(Utils.getHour(schedule.getEndHourAfternoon()), Utils.getMinute(schedule.getEndHourAfternoon()));
        String lastEndHour = (TextUtils.isEmpty(schedule.getEndHourAfternoon()) ? endHourMorning : endHourAfternoon);

        if (strNow.compareTo(lastEndHour) >= 0) {
            if (jobId == GlobalJobs.END_WORK_JOB_ID) {
                // Next job is do exercise if is the last endWork hour
                calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(lastEndHour));
                calendar.set(Calendar.MINUTE, Utils.getMinute(lastEndHour));
                calendar.add(Calendar.MINUTE, 5);
                buildJob(GlobalJobs.DO_EXERCISE_JOB_ID, calendar);
            } else if (jobId == GlobalJobs.DO_EXERCISE_JOB_ID) {
                // Next job is socialize
                calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(lastEndHour));
                calendar.set(Calendar.MINUTE, Utils.getMinute(lastEndHour));
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                buildJob(GlobalJobs.SOCIALIZE_JOB_ID, calendar);
            } else {
                // Next job is startWork from next active schedule
                buildNextJobForSchedule(getNextWorkableSchedule(setting, weekDay), weekDay);
            }
        } else {
            String strStartHourAfternoon = (TextUtils.isEmpty(schedule.getStartHourAfternoon()) ? "" : Utils.getUserHour(Utils.getHour(schedule.getStartHourAfternoon()), Utils.getMinute(schedule.getStartHourAfternoon())));
            switch (jobId) {
                case GlobalJobs.START_WORK_JOB_ID:
                case GlobalJobs.TAKE_SHORT_BREAK_JOB_ID:
                case GlobalJobs.TAKE_LONG_BREAK_JOB_ID:
                    long hourDifference;
                    String endHour = "";
                    if (!TextUtils.isEmpty(schedule.getEndHourMorning()) && (strNow.compareTo(endHourMorning) < 0)) {
                        hourDifference = Utils.getHourDifferenceBetweenTwoDates(Utils.getDateFromHour(strNow), Utils.getDateFromHour(schedule.getEndHourMorning()));
                        endHour = endHourMorning;
                    } else if (!TextUtils.isEmpty(schedule.getEndHourAfternoon()) && (strNow.compareTo(endHourAfternoon) < 0)) {
                        hourDifference = Utils.getHourDifferenceBetweenTwoDates(Utils.getDateFromHour(strNow), Utils.getDateFromHour(schedule.getEndHourAfternoon()));
                        endHour = endHourAfternoon;
                    } else {
                        hourDifference = -1;
                    }
                    if (hourDifference > 1) {
                        // Next job is short or long break
                        int nextJobId = (
                                (jobId == GlobalJobs.START_WORK_JOB_ID) ? GlobalJobs.TAKE_SHORT_BREAK_JOB_ID :
                                        (jobId == GlobalJobs.TAKE_SHORT_BREAK_JOB_ID ? GlobalJobs.TAKE_LONG_BREAK_JOB_ID : GlobalJobs.TAKE_SHORT_BREAK_JOB_ID)
                        );
                        calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(strNow));
                        calendar.set(Calendar.MINUTE, Utils.getMinute(strNow));
                        calendar.add(Calendar.HOUR_OF_DAY, 1);
                        buildJob(nextJobId, calendar);
                    } else if (!TextUtils.isEmpty(endHour)) {
                        // Next job is endWork
                        calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(endHour));
                        calendar.set(Calendar.MINUTE, Utils.getMinute(endHour));
                        buildJob(GlobalJobs.END_WORK_JOB_ID, calendar);
                    }
                    break;
                case GlobalJobs.END_WORK_JOB_ID:
                    // Next job is start work if isn't the last endWork hour
                    if (!TextUtils.isEmpty(strStartHourAfternoon) && strNow.compareTo(strStartHourAfternoon) < 0) {
                        // Next job is startWork
                        calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(strStartHourAfternoon));
                        calendar.set(Calendar.MINUTE, Utils.getMinute(strStartHourAfternoon));
                        buildJob(GlobalJobs.START_WORK_JOB_ID, calendar);
                    } else {
                        // Next job is do exercise
                        calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(lastEndHour));
                        calendar.set(Calendar.MINUTE, Utils.getMinute(lastEndHour));
                        calendar.add(Calendar.MINUTE, 5);
                        buildJob(GlobalJobs.DO_EXERCISE_JOB_ID, calendar);
                    }
                    break;
                default:
                    break; //There's an error
            }
        }
    }

}
