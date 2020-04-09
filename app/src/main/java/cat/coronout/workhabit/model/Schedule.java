package cat.coronout.workhabit.model;

public class Schedule {

    private int weekDay;
    private String horaIniciMati;
    private String horaFiMati;
    private String horaIniciTarda;
    private String horaFiTarda;

    public Schedule(int weekDay, String horaIniciMati, String horaFiMati, String horaIniciTarda, String horaFiTarda) {
        this.weekDay = weekDay;
        this.horaIniciMati = horaIniciMati;
        this.horaFiMati = horaFiMati;
        this.horaIniciTarda = horaIniciTarda;
        this.horaFiTarda = horaFiTarda;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public String getHoraIniciMati() {
        return horaIniciMati;
    }

    public String getHoraFiMati() {
        return horaFiMati;
    }

    public String getHoraIniciTarda() {
        return horaIniciTarda;
    }

    public String getHoraFiTarda() {
        return horaFiTarda;
    }

}
