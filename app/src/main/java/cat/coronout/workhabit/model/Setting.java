package cat.coronout.workhabit.model;

import java.util.Date;

public class Setting {

    private Schedule[] schedules;
    private Date birthDate;

    public Setting() {
        this.schedules = new Schedule[7];
        this.birthDate = null;
    }

    public Schedule[] getSchedules() {
        return schedules;
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

}
