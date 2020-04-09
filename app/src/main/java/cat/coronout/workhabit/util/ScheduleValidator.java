package cat.coronout.workhabit.util;

import android.text.TextUtils;

import cat.coronout.workhabit.model.Schedule;

/**
 * Abstract class schedule validator
 *
 * Use it to validate a possible configuration selected for the user
 */
public abstract class ScheduleValidator {

    /**
     * Check if start time of working at morning is valid
     * @param schedule Schedule to be modified
     * @param currentHour Current schedule value
     * @param newHour User new hour selection
     * @return True if user selection is a valid configuration
     */
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

    /**
     * Check if end time of working at morning is valid
     * @param schedule Schedule to be modified
     * @param newHour User new hour selection
     * @return True if user selection is a valid configuration
     */
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

    /**
     * Check if start time of working at afternoon is valid
     * @param schedule Schedule to be modified
     * @param newHour User new hour selection
     * @return True if user selection is a valid configuration
     */
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

    /**
     * Check if end time of working at afternoon is valid
     * @param schedule Schedule to be modified
     * @param newHour User new hour selection
     * @return True if user selection is a valid configuration
     */
    private static boolean checkEndAfternoonHourValidation(Schedule schedule, String newHour) {
        String lastHour = schedule.getStartHourAfternoon();
        if (TextUtils.isEmpty(lastHour)) return false;
        int compareResult = newHour.compareTo(lastHour);
        if (compareResult <= 0) return false;
        return true;
    }

    /**
     * Check if user new configuration selection is valid
     * @param schedule Schedule to be modified
     * @param hourMode User selection mode
     * @param newValue User new hour selection
     * @return True if user selection is a valid configuration
     */
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
