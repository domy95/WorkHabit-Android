package cat.coronout.workhabit.model;

import androidx.annotation.Nullable;

import java.util.Date;

public class Setting {

    private Schedule[] schedules;
    private Date birthDate;
    private boolean usingSameSchedule;

    public Setting() {
        this.schedules = new Schedule[7];
        this.birthDate = null;
        this.usingSameSchedule = true;
    }

    public Schedule[] getSchedules() {
        return schedules;
    }

    public Schedule getSchedule(int weekDay) {
        return this.schedules[weekDay - 1];
    }

    public void setSchedule(Schedule schedule) {
        this.schedules[schedule.getWeekDay() - 1] = schedule;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isUsingSameSchedule() {
        return usingSameSchedule;
    }

    public void setUsingSameSchedule(boolean usingSameSchedule) {
        this.usingSameSchedule = usingSameSchedule;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Setting) {
            Setting tmpSetting = (Setting) obj;
            if (this.birthDate.equals(tmpSetting.birthDate)) {
                if (this.usingSameSchedule == tmpSetting.usingSameSchedule) {
                    boolean found = false;
                    int i = 0;
                    while (i < this.schedules.length && !found) {
                        if (this.schedules[i].hashCode() != tmpSetting.schedules[i].hashCode())
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
