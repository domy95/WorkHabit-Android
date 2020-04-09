package cat.coronout.workhabit.model;

public class Schedule {

    public enum HOUR_MODE {
        START_MORNING,
        END_MORNING,
        START_AFTERNOON,
        END_AFTERNOON
    }

    private int weekDay;
    private String startHourMorning;
    private String endHourMorning;
    private String startHourAfternoon;
    private String endHourAfternoon;
    private boolean isEnabled;

    public Schedule(int weekDay, String startHourMorning, String endHourMorning, String startHourAfternoon, String endHourAfternoon) {
        this.weekDay = weekDay;
        this.startHourMorning = startHourMorning;
        this.endHourMorning = endHourMorning;
        this.startHourAfternoon = startHourAfternoon;
        this.endHourAfternoon = endHourAfternoon;
        this.isEnabled = true;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public String getStartHourMorning() {
        return startHourMorning;
    }

    public void setStartHourMorning(String startHourMorning) {
        this.startHourMorning = startHourMorning;
    }

    public String getEndHourMorning() {
        return endHourMorning;
    }

    public void setEndHourMorning(String endHourMorning) {
        this.endHourMorning = endHourMorning;
    }

    public String getStartHourAfternoon() {
        return startHourAfternoon;
    }

    public void setStartHourAfternoon(String startHourAfternoon) {
        this.startHourAfternoon = startHourAfternoon;
    }

    public String getEndHourAfternoon() {
        return endHourAfternoon;
    }

    public void setEndHourAfternoon(String endHourAfternoon) {
        this.endHourAfternoon = endHourAfternoon;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

}
