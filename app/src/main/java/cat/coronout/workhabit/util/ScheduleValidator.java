package cat.coronout.workhabit.util;

import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

import cat.coronout.workhabit.model.Schedule;

public abstract class ScheduleValidator {

    private static boolean checkStartMorningHourValidation(Schedule schedule, String currentHour, String newHour) {
        if (TextUtils.isEmpty(currentHour)) return true;
        int compareResult = newHour.compareTo(currentHour);
        if (compareResult == 0) return true; //Equals
        else if (compareResult < 0) return true; //Valor anterior
        String nextHour = schedule.getEndHourMorning();
        if (TextUtils.isEmpty(nextHour)) return true;
        compareResult = newHour.compareTo(nextHour);
        if (compareResult <= 0) return true;
        return false;
    }

    private static boolean checkEndMorningHourValidation(Schedule schedule, String newHour) {
        String lastHour = schedule.getStartHourMorning();
        if (TextUtils.isEmpty(lastHour)) return false;
        int compareResult = newHour.compareTo(lastHour);
        if (compareResult <= 0) return false;
        String nextHour = schedule.getStartHourAfternoon();
        if (TextUtils.isEmpty(nextHour)) return true;
        compareResult = newHour.compareTo(nextHour);
        if (compareResult <= 0) return true;
        return false;
    }

    private static boolean checkStartAfternoonHourValidation(Schedule schedule, String newHour) {
        String lastHour = schedule.getEndHourMorning();
        if (TextUtils.isEmpty(lastHour)) return false;
        int compareResult = newHour.compareTo(lastHour);
        if (compareResult <= 0) return false;
        String nextHour = schedule.getEndHourAfternoon();
        if (TextUtils.isEmpty(nextHour)) return true;
        compareResult = newHour.compareTo(nextHour);
        if (compareResult <= 0) return true;
        return false;
    }

    private static boolean checkEndAfternoonHourValidation(Schedule schedule, String newHour) {
        String lastHour = schedule.getStartHourAfternoon();
        if (TextUtils.isEmpty(lastHour)) return false;
        int compareResult = newHour.compareTo(lastHour);
        if (compareResult <= 0) return false;
        return true;
    }

    public static boolean isValidSchedule(Schedule schedule, Schedule.HOUR_MODE hourMode, String newValue) {
        switch (hourMode) {
            case START_MORNING:
                return checkStartMorningHourValidation(schedule, schedule.getStartHourMorning(), newValue);
            case END_MORNING:
                return checkEndMorningHourValidation(schedule, newValue);
            case START_AFTERNOON:
                return checkStartAfternoonHourValidation(schedule, newValue);
            case END_AFTERNOON:
                return checkEndAfternoonHourValidation(schedule, newValue);
        }
        return false;
    }

}
