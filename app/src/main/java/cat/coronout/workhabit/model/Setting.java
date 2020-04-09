package cat.coronout.workhabit.model;

import androidx.annotation.Nullable;

import java.util.Date;

/**
 * Setting respresentation
 *
 * Consisting of 7 schedules, a birth date, and one semaphore property
 */
public class Setting {

    /// Collection for the 7 schedules
    private Schedule[] schedules;
    /// Birth date
    private Date birthDate;
    /// Semaphore to control if all schedules are the same
    private boolean usingSameSchedule;

    /**
     * Constructor
     */
    public Setting() {
        this.schedules = new Schedule[7];
        this.birthDate = null;
        this.usingSameSchedule = true;
    }

    /**
     * Getter
     * @return 7 schedules
     */
    public Schedule[] getSchedules() {
        return schedules;
    }

    /**
     * Getter
     * @param weekDay Day of the week
     * @return Schedule for day of the week requested
     */
    public Schedule getSchedule(int weekDay) {
        return this.schedules[weekDay - 1];
    }

    /**
     * Setter
     * @param schedule Schedule
     */
    public void setSchedule(Schedule schedule) {
        this.schedules[schedule.getWeekDay() - 1] = schedule;
    }

    /**
     * Getter
     * @return Birth date
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Setter
     * @param birthDate Birth date
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Getter
     * @return Same schedule for all workable days
     */
    public boolean isUsingSameSchedule() {
        return usingSameSchedule;
    }

    /**
     * Setter
     * @param usingSameSchedule Same schedule for all workable days
     */
    public void setUsingSameSchedule(boolean usingSameSchedule) {
        this.usingSameSchedule = usingSameSchedule;
    }

    /**
     * Check equality between two objects
     *
     * Two settings are equals when all of their attributes are equals
     * @param obj Object to compare
     * @return True if all of their attributes are equals
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Setting) {
            Setting tmpSetting = (Setting) obj;
            if ((this.birthDate == null && tmpSetting.birthDate == null) || (this.birthDate != null && tmpSetting.birthDate != null && this.birthDate.equals(tmpSetting.birthDate))) {
                if (this.usingSameSchedule == tmpSetting.usingSameSchedule) {
                    boolean found = false;
                    int i = 0;
                    while (i < this.schedules.length && !found) {
                        if (this.schedules[i] != null && tmpSetting.schedules[i] != null) {
                            if (this.schedules[i].hashCode() != tmpSetting.schedules[i].hashCode())
                                found = true;
                        }
                        else if (this.schedules[i] != null && tmpSetting.schedules[i] == null)
                            found = true;
                        else if (this.schedules[i] == null && tmpSetting.schedules[i] != null)
                            found = true;
                        i++;
                    }
                    return !found;
                }
            }
        }
        return false;
    }
}
