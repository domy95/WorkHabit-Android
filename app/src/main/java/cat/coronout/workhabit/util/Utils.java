package cat.coronout.workhabit.util;

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
     * Get the hour unit from string representation of hour
     * @param hour String representation of hour
     * @return Hour unit
     */
    public static int getHour(String hour) {
        String[] hourSplitted = hour.split(":");
        int result;
        try {
            result = ((hourSplitted.length > 1) ? Integer.parseInt(hourSplitted[0]) : (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY));
        } catch (Exception ex) {
            result = (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY);
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
        int result;
        try {
            result = ((hourSplitted.length > 1) ? Integer.parseInt(hourSplitted[1]) : (Calendar.getInstance()).get(Calendar.MINUTE));
        } catch (Exception ex) {
            result = (Calendar.getInstance()).get(Calendar.MINUTE);
            ex.printStackTrace();
        }
        return result;
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

}
