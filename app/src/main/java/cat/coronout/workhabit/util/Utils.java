package cat.coronout.workhabit.util;

import java.util.Calendar;
import java.util.Date;

public abstract class Utils {

    public static Calendar getCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
