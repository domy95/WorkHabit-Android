package cat.coronout.workhabit.util;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class Utils {

    public static Calendar getCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date getDateFromCalendar(Calendar calendar) {
        return calendar.getTime();
    }

    public static String getUserDateFormat(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(date);
    }

    public static String getLocalStorageDateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    public static String getUserHour(int hour, int minute) {
        String finalHour = "";
        if (hour < 10) finalHour += ("0" + hour);
        else finalHour += hour;
        finalHour += ":";
        if (minute < 10) finalHour += ("0" + minute);
        else finalHour += minute;
        return finalHour;
    }

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

    public static void showBasicSnackBar(View container, String message) {
        Snackbar snackBar = Snackbar.make(container, message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

}
