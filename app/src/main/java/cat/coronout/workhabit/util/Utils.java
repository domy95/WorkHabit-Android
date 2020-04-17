package cat.coronout.workhabit.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Abstract class with util methods
 *
 * Basically methods to use dates and hours
 */
public abstract class Utils {

    /**
     * Get calendar from date
     * @param date Date to set in calendar
     * @return Calendar instance
     */
    public static Calendar getCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Get date from calendar
     * @param calendar Calendar with needed date
     * @return Date instance from calendar
     */
    public static Date getDateFromCalendar(Calendar calendar) {
        return calendar.getTime();
    }

    /**
     * Get date in string representation with format dd/MM/yyyy
     * @param date Date instance
     * @return String representation of `date`
     */
    public static String getUserDateFormat(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(date);
    }

    /**
     * Get the hour using left zeros
     * @param hour Hour
     * @param minute Minute
     * @return String representation of hour
     */
    public static String getUserHour(int hour, int minute) {
        String finalHour = "";
        if (hour < 10) finalHour += ("0" + hour);
        else finalHour += hour;
        finalHour += ":";
        if (minute < 10) finalHour += ("0" + minute);
        else finalHour += minute;
        return finalHour;
    }

    /**
     * Get the hour using left zeros and adding and integer number of minutes
     * @param hour Hour
     * @param minute Minute
     * @param addMinutes Integer number of minutes to add
     * @return String representation of hour
     */
    public static String getUserHour(int hour, int minute, int addMinutes) {
        //Apply minutes
        minute += addMinutes;
        if (minute > 59) {
            hour++;
            minute = (minute - 60);
        } else if (minute < 0) {
            hour--;
            minute = (60 + minute);
        }
        //Cast to user hour representation
        String finalHour = "";
        if (hour < 10) finalHour += ("0" + hour);
        else finalHour += hour;
        finalHour += ":";
        if (minute < 10) finalHour += ("0" + minute);
        else finalHour += minute;
        return finalHour;
    }

    /**
     * Get the hour unit from string representation of hour
     * @param hour String representation of hour
     * @return Hour unit
     */
    public static int getHour(String hour) {
        String[] hourSplitted = hour.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int result;
        try {
            result = ((hourSplitted.length > 1) ? Integer.parseInt(hourSplitted[0]) : calendar.get(Calendar.HOUR_OF_DAY));
        } catch (Exception ex) {
            result = calendar.get(Calendar.HOUR_OF_DAY);
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Get the minute unit from string representation of hour
     * @param hour String representation of hour
     * @return Minute unit
     */
    public static int getMinute(String hour) {
        String[] hourSplitted = hour.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int result;
        try {
            result = ((hourSplitted.length > 1) ? Integer.parseInt(hourSplitted[1]) : calendar.get(Calendar.MINUTE));
        } catch (Exception ex) {
            result = calendar.get(Calendar.MINUTE);
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Get current day of the week
     * @return Day of the week
     */
    public static int getCurrentWeekDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Show SnackBar in `container`
     * @param container View when show SnackBar
     * @param message Message to show
     */
    public static void showBasicSnackBar(View container, String message) {
        Snackbar snackBar = Snackbar.make(container, message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    /**
     * Get date from hour
     * @param hour Hour as string representations
     * @return Date representing that hour today
     */
    public static Date getDateFromHour(String hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Utils.getHour(hour));
        calendar.set(Calendar.MINUTE, Utils.getMinute(hour));
        return calendar.getTime();
    }

    /**
     * Get hour difference between two dates
     * @param lhs Date instance (lesser)
     * @param rhs Date instance (greather)
     * @return Difference in hours
     */
    public static long getHourDifferenceBetweenTwoDates(Date lhs, Date rhs) {
        long different = rhs.getTime() - lhs.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        return ((elapsedDays * 24) + elapsedHours);
    }

    /**
     * Cast drawable/mipmap resource to Bitmap object
     * @param context Context
     * @param resourceId Resource identifier
     * @return Bitmap instance of that resource
     */
    public static Bitmap getBitmap(Context context, int resourceId) {
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    /**
     * Setup correct next date adding days while not matching day of the week that we needs
     * @param calendar Calendar to add days
     * @param neededWeekDay Day of the week that we need
     */
    public static void setupCorrectNextDate(Calendar calendar, int neededWeekDay) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != neededWeekDay) {
            calendar.add(Calendar.DATE, 1);
        }
    }

}
