package cat.coronout.workhabit.model;

import androidx.annotation.Nullable;

/**
 * Schedule respresentation
 *
 * Consisting of 4 string hours, one day of the week and one enablement property
 */
public class Schedule {

    /**
     * Enum to differentiate actions with the schedule
     */
    public enum HOUR_MODE {
        START_MORNING,
        END_MORNING,
        START_AFTERNOON,
        END_AFTERNOON
    }

    /// Representation of the day of the week; Sunday (1), Monday (2) ... Saturday (7)
    private int weekDay;
    /// Representation of the start time of working in the morning
    private String startHourMorning;
    /// Representation of the end time of working in the morning
    private String endHourMorning;
    /// Representation of the start time of working in the afternoon
    private String startHourAfternoon;
    /// Representation of the end time of working in the afternoon
    private String endHourAfternoon;
    /// Semaphore that indicates if this day is workable or not
    private boolean isEnabled;

    /**
     * Parametizable constructor
     * @param weekDay Day of the week
     * @param startHourMorning Start time of working in the morning
     * @param endHourMorning End time of working in the morning
     * @param startHourAfternoon Start time of working in the afternoon
     * @param endHourAfternoon End time of working in the afternoon
     */
    public Schedule(int weekDay, String startHourMorning, String endHourMorning, String startHourAfternoon, String endHourAfternoon) {
        this.weekDay = weekDay;
        this.startHourMorning = startHourMorning;
        this.endHourMorning = endHourMorning;
        this.startHourAfternoon = startHourAfternoon;
        this.endHourAfternoon = endHourAfternoon;
        this.isEnabled = true;
    }

    /**
     * Getter
     * @return Day of the week
     */
    public int getWeekDay() {
        return weekDay;
    }

    /**
     * Getter
     * @return Start time of working in the morning
     */
    public String getStartHourMorning() {
        return startHourMorning;
    }

    /**
     * Setter
     * @param startHourMorning Start time of working in the morning
     */
    public void setStartHourMorning(String startHourMorning) {
        this.startHourMorning = startHourMorning;
    }

    /**
     * Getter
     * @return End time of working in the morning
     */
    public String getEndHourMorning() {
        return endHourMorning;
    }

    /**
     * Setter
     * @param endHourMorning End time of working in the morning
     */
    public void setEndHourMorning(String endHourMorning) {
        this.endHourMorning = endHourMorning;
    }

    /**
     * Getter
     * @return Start time of working in the afternoon
     */
    public String getStartHourAfternoon() {
        return startHourAfternoon;
    }

    /**
     * Setter
     * @param startHourAfternoon Start time of working in the afternoon
     */
    public void setStartHourAfternoon(String startHourAfternoon) {
        this.startHourAfternoon = startHourAfternoon;
    }

    /**
     * Getter
     * @return End time of working in the afternoon
     */
    public String getEndHourAfternoon() {
        return endHourAfternoon;
    }

    /**
     * Setter
     * @param endHourAfternoon End time of working in the afternoon
     */
    public void setEndHourAfternoon(String endHourAfternoon) {
        this.endHourAfternoon = endHourAfternoon;
    }

    /**
     * Getter
     * @return Workable day
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Setter
     * @param enabled Workable day
     */
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * Check if two schedules are the same
     *
     * Two schedules are the same if they represent the same day of the week
     * @param obj Object to check equality
     * @return True if they represent the same day of the week
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Schedule) {
            return (this.weekDay == ((Schedule) obj).weekDay);
        }
        return false;
    }

    /**
     * Calculate the hash code for an instance of this class
     * @return Hash code
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.weekDay;
        result = 31 * result + (this.startHourMorning == null ? 0 : this.startHourMorning.hashCode());
        result = 31 * result + (this.endHourMorning == null ? 0 : this.endHourMorning.hashCode());
        result = 31 * result + (this.startHourAfternoon == null ? 0 : this.startHourAfternoon.hashCode());
        result = 31 * result + (this.endHourAfternoon == null ? 0 : this.endHourAfternoon.hashCode());
        result = 31 * result + (this.isEnabled ? 1 : 0);
        return result;
    }
}
