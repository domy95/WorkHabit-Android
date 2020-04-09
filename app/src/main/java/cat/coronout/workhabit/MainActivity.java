package cat.coronout.workhabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import cat.coronout.workhabit.model.Schedule;
import cat.coronout.workhabit.model.Setting;
import cat.coronout.workhabit.util.LocalStorage;
import cat.coronout.workhabit.util.Utils;

public class MainActivity extends AppCompatActivity {

    /// `LocalStorage` instance
    private LocalStorage localStorage;
    /// `Setting` instance
    private Setting setting;

    // Layouts
    LinearLayout layoutMon, layoutTue, layoutWed, layoutThu, layoutFri, layoutSat, layoutSun;

    // Buttons
    Button btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun;

    // TextViews
    //  Monday
    TextView startHourMorningMon, endHourMorningMon, startHourAfternoonMon, endHourAfternoonMon;
    //  Tuesday
    TextView startHourMorningTue, endHourMorningTue, startHourAfternoonTue, endHourAfternoonTue;
    //  Wednesday
    TextView startHourMorningWed, endHourMorningWed, startHourAfternoonWed, endHourAfternoonWed;
    //  Thursday
    TextView startHourMorningThu, endHourMorningThu, startHourAfternoonThu, endHourAfternoonThu;
    //  Friday
    TextView startHourMorningFri, endHourMorningFri, startHourAfternoonFri, endHourAfternoonFri;
    //  Saturday
    TextView startHourMorningSat, endHourMorningSat, startHourAfternoonSat, endHourAfternoonSat;
    //  Sunday
    TextView startHourMorningSun, endHourMorningSun, startHourAfternoonSun, endHourAfternoonSun;

    // BirthDate
    TextView birthDateSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get LocalStorage instance
        localStorage = LocalStorage.getInstance(getApplicationContext());

        // Get current settings
        setting = localStorage.getCurrentSetting();

        // Setup views
        setupViews();

        // Setup listeners
        setupListeners();

        // Setup data
        setupSchedules();
        setupBirthDate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                saveData();
                break;
            case R.id.reset:
                resetData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        setupButtons();
        setupLayouts();
        setupTextViews();
    }

    private void setupListeners() {
        setupButtonsListeners();
        setupScheduleListeners();
        setupBirthDateListener();
    }

    private void saveScheduleHour(final Schedule schedule, final Schedule.HOUR_MODE hourMode, String hour) {
        switch (hourMode) {
            case START_MORNING:
                schedule.setStartHourMorning(hour);
                break;
            case END_MORNING:
                schedule.setEndHourMorning(hour);
                break;
            case START_AFTERNOON:
                schedule.setStartHourAfternoon(hour);
                break;
            case END_AFTERNOON:
                schedule.setEndHourAfternoon(hour);
                break;
            default:
        }
    }

    private String getScheduleHour(final Schedule schedule, final Schedule.HOUR_MODE hourMode) {
        switch (hourMode) {
            case START_MORNING:
                return schedule.getStartHourMorning();
            case END_MORNING:
                return schedule.getEndHourMorning();
            case START_AFTERNOON:
                return schedule.getStartHourAfternoon();
            case END_AFTERNOON:
                return schedule.getEndHourAfternoon();
            default:
                return "";
        }
    }

    private void setupHourListener(final Schedule schedule, final TextView textView, final Schedule.HOUR_MODE hourMode) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scheduleHour = getScheduleHour(schedule, hourMode);
                String[] scheduleHourSplitted = scheduleHour.split(":");
                int hour;
                int minute;
                try {
                    hour = ((scheduleHourSplitted.length > 1) ? Integer.parseInt(scheduleHourSplitted[0]) : (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY));
                    minute = ((scheduleHourSplitted.length > 1) ? Integer.parseInt(scheduleHourSplitted[1]) : (Calendar.getInstance()).get(Calendar.MINUTE));
                } catch (Exception ex) {
                    hour = (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY);
                    minute = (Calendar.getInstance()).get(Calendar.MINUTE);
                    ex.printStackTrace();
                }
                TimePickerDialog dialog = new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                saveScheduleHour(schedule, hourMode, Utils.getUserHour(hourOfDay, minute));
                                setting.setSchedule(schedule);
                                setupWeekDay(schedule.getWeekDay());
                            }
                        },
                        hour,
                        minute,
                        true
                );

                dialog.setCancelable(false);
                dialog.show();

            }
        });
    }

    private void setupButtonsListeners() {
        for (int i = 0; i < 7; i++) {
            final int weekDay = i + 1;
            final Button button = getButtonForWeekDay(weekDay);
            final Schedule schedule;

            if (setting.getSchedule(weekDay) == null)
                schedule = new Schedule(weekDay, "", "", "", "");
            else
                schedule = setting.getSchedule(weekDay);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Canvi d'estat
                    schedule.setEnabled(!schedule.isEnabled());
                    setting.setSchedule(schedule);
                    setupWeekDay(schedule.getWeekDay());
                }
            });
        }
    }

    private void setupScheduleListeners() {
        for (int i = 0; i < 7; i++) {
            int weekDay = i + 1;
            TextView[] textViews = getTextViews(weekDay);
            final Schedule schedule;

            if (setting.getSchedule(weekDay) == null)
                schedule = new Schedule(weekDay, "", "", "", "");
            else
                schedule = setting.getSchedule(weekDay);

            setupHourListener(schedule, textViews[0], Schedule.HOUR_MODE.START_MORNING);
            setupHourListener(schedule, textViews[1], Schedule.HOUR_MODE.END_MORNING);
            setupHourListener(schedule, textViews[2], Schedule.HOUR_MODE.START_AFTERNOON);
            setupHourListener(schedule, textViews[3], Schedule.HOUR_MODE.END_AFTERNOON);
        }
    }

    private void setupBirthDateListener() {

        birthDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = setting.getBirthDate();
                Calendar startDate;
                if (date != null)
                    startDate = Utils.getCalendarFromDate(date);
                else
                    startDate = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(Calendar.YEAR, year);
                                selectedDate.set(Calendar.MONTH, month);
                                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                setting.setBirthDate(Utils.getDateFromCalendar(selectedDate));
                                setupBirthDate();
                            }
                        },
                        startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH),
                        startDate.get(Calendar.DAY_OF_MONTH)
                );

                dialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                dialog.setCancelable(false);
                dialog.show();
            }
        });

    }

    private void setupButtons() {
        // Monday
        btnMon = findViewById(R.id.btnMon);
        // Tuesday
        btnTue = findViewById(R.id.btnTue);
        // Wednesday
        btnWed = findViewById(R.id.btnWed);
        // Thursday
        btnThu = findViewById(R.id.btnThu);
        // Friday
        btnFri = findViewById(R.id.btnFri);
        // Saturday
        btnSat = findViewById(R.id.btnSat);
        // Sunday
        btnSun = findViewById(R.id.btnSun);
    }

    private void setupLayouts() {
        // Monday
        layoutMon = findViewById(R.id.layoutMon);
        // Tuesday
        layoutTue = findViewById(R.id.layoutTue);
        // Wednesday
        layoutWed = findViewById(R.id.layoutWed);
        // Thursday
        layoutThu = findViewById(R.id.layoutThu);
        // Friday
        layoutFri = findViewById(R.id.layoutFri);
        // Saturday
        layoutSat = findViewById(R.id.layoutSat);
        // Sunday
        layoutSun = findViewById(R.id.layoutSun);
    }

    private void setupTextViews() {
        // Monday
        startHourMorningMon = findViewById(R.id.startHourMorningMon);
        endHourMorningMon = findViewById(R.id.endHourMorningMon);
        startHourAfternoonMon = findViewById(R.id.startHourAfternoonMon);
        endHourAfternoonMon = findViewById(R.id.endHourAfternoonMon);
        // Tuesday
        startHourMorningTue = findViewById(R.id.startHourMorningTue);
        endHourMorningTue = findViewById(R.id.endHourMorningTue);
        startHourAfternoonTue = findViewById(R.id.startHourAfternoonTue);
        endHourAfternoonTue = findViewById(R.id.endHourAfternoonTue);
        // Wednesday
        startHourMorningWed = findViewById(R.id.startHourMorningWed);
        endHourMorningWed = findViewById(R.id.endHourMorningWed);
        startHourAfternoonWed = findViewById(R.id.startHourAfternoonWed);
        endHourAfternoonWed = findViewById(R.id.endHourAfternoonWed);
        // Thursday
        startHourMorningThu = findViewById(R.id.startHourMorningThu);
        endHourMorningThu = findViewById(R.id.endHourMorningThu);
        startHourAfternoonThu = findViewById(R.id.startHourAfternoonThu);
        endHourAfternoonThu = findViewById(R.id.endHourAfternoonThu);
        // Friday
        startHourMorningFri = findViewById(R.id.startHourMorningFri);
        endHourMorningFri = findViewById(R.id.endHourMorningFri);
        startHourAfternoonFri = findViewById(R.id.startHourAfternoonFri);
        endHourAfternoonFri = findViewById(R.id.endHourAfternoonFri);
        // Saturday
        startHourMorningSat = findViewById(R.id.startHourMorningSat);
        endHourMorningSat = findViewById(R.id.endHourMorningSat);
        startHourAfternoonSat = findViewById(R.id.startHourAfternoonSat);
        endHourAfternoonSat = findViewById(R.id.endHourAfternoonSat);
        // Sunday
        startHourMorningSun = findViewById(R.id.startHourMorningSun);
        endHourMorningSun = findViewById(R.id.endHourMorningSun);
        startHourAfternoonSun = findViewById(R.id.startHourAfternoonSun);
        endHourAfternoonSun = findViewById(R.id.endHourAfternoonSun);
        // BirthDate
        birthDateSelector = findViewById(R.id.birthDateSelector);
    }

    private void saveData() {
        localStorage.saveSetting(setting);
        resetData();
    }

    private void resetData() {
        setting = localStorage.getCurrentSetting();
        setupSchedules();
        setupBirthDate();
    }

    private void setupSchedules() {
        for (int i = 0; i < 7; i++) {
            final int weekDay = i + 1;
            setupWeekDay(weekDay);
        }
    }

    private Button getButtonForWeekDay(final int weekDay) {
        switch (weekDay) {
            case Calendar.MONDAY:
                return btnMon;
            case Calendar.TUESDAY:
                return btnTue;
            case Calendar.WEDNESDAY:
                return btnWed;
            case Calendar.THURSDAY:
                return btnThu;
            case Calendar.FRIDAY:
                return btnFri;
            case Calendar.SATURDAY:
                return btnSat;
            default:
                return btnSun;
        }
    }

    private LinearLayout getLayoutForWeekDay(final int weekDay) {
        switch (weekDay) {
            case Calendar.MONDAY:
                return layoutMon;
            case Calendar.TUESDAY:
                return layoutTue;
            case Calendar.WEDNESDAY:
                return layoutWed;
            case Calendar.THURSDAY:
                return layoutThu;
            case Calendar.FRIDAY:
                return layoutFri;
            case Calendar.SATURDAY:
                return layoutSat;
            default:
                return layoutSun;
        }
    }

    private TextView[] getTextViews(int weekDay) {
        TextView[] textViews = new TextView[4];
        switch (weekDay) {
            case Calendar.MONDAY:
                textViews[0] = startHourMorningMon;
                textViews[1] = endHourMorningMon;
                textViews[2] = startHourAfternoonMon;
                textViews[3] = endHourAfternoonMon;
                break;
            case Calendar.TUESDAY:
                textViews[0] = startHourMorningTue;
                textViews[1] = endHourMorningTue;
                textViews[2] = startHourAfternoonTue;
                textViews[3] = endHourAfternoonTue;
                break;
            case Calendar.WEDNESDAY:
                textViews[0] = startHourMorningWed;
                textViews[1] = endHourMorningWed;
                textViews[2] = startHourAfternoonWed;
                textViews[3] = endHourAfternoonWed;
                break;
            case Calendar.THURSDAY:
                textViews[0] = startHourMorningThu;
                textViews[1] = endHourMorningThu;
                textViews[2] = startHourAfternoonThu;
                textViews[3] = endHourAfternoonThu;
                break;
            case Calendar.FRIDAY:
                textViews[0] = startHourMorningFri;
                textViews[1] = endHourMorningFri;
                textViews[2] = startHourAfternoonFri;
                textViews[3] = endHourAfternoonFri;
                break;
            case Calendar.SATURDAY:
                textViews[0] = startHourMorningSat;
                textViews[1] = endHourMorningSat;
                textViews[2] = startHourAfternoonSat;
                textViews[3] = endHourAfternoonSat;
                break;
            default:
                textViews[0] = startHourMorningSun;
                textViews[1] = endHourMorningSun;
                textViews[2] = startHourAfternoonSun;
                textViews[3] = endHourAfternoonSun;
                break;
        }
        return textViews;
    }

    private void setupWeekDay(int weekDay) {
        final Button button = getButtonForWeekDay(weekDay);
        final LinearLayout layout = getLayoutForWeekDay(weekDay);
        Schedule schedule = setting.getSchedule(weekDay);
        if (schedule == null)
            schedule = new Schedule(weekDay, "", "", "", "");
        TextView[] textViews = getTextViews(weekDay);
        TextView startHourMorning = textViews[0];
        TextView endHourMorning = textViews[1];
        TextView startHourAfternoon = textViews[2];
        TextView endHourAfternoon = textViews[3];
        startHourMorning.setText(schedule.getStartHourMorning());
        endHourMorning.setText(schedule.getEndHourMorning());
        startHourAfternoon.setText(schedule.getStartHourAfternoon());
        endHourAfternoon.setText(schedule.getEndHourAfternoon());
        // Change background button color
        if (schedule.isEnabled()) button.setBackground(getDrawable(R.drawable.back_button_active));
        else button.setBackground(getDrawable(R.drawable.back_button_inactive));
        // Disable/enable views
        if (schedule.isEnabled()) {
            layout.setAlpha(1f);
            for (int i = 0; i < textViews.length; i++)
                textViews[i].setEnabled(true);
        }
        else {
            layout.setAlpha(0.25f);
            for (int i = 0; i < textViews.length; i++)
                textViews[i].setEnabled(false);
        }
    }

    private void setupBirthDate() {
        Date birthDate = setting.getBirthDate();
        birthDateSelector.setText(Utils.getUserDateFormat(birthDate));
    }

}
