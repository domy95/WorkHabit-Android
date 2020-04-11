package cat.coronout.workhabit.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import cat.coronout.workhabit.R;
import cat.coronout.workhabit.job.WorkhabitJobBuilder;
import cat.coronout.workhabit.model.Schedule;
import cat.coronout.workhabit.model.Setting;
import cat.coronout.workhabit.storage.LocalStorage;
import cat.coronout.workhabit.task.ScheduleNotificationTask;
import cat.coronout.workhabit.util.GlobalJobs;
import cat.coronout.workhabit.util.ScheduleValidator;
import cat.coronout.workhabit.util.Utils;

/**
 * Main controller
 *
 * Controller for configuration view
 */
public class MainActivity extends AppCompatActivity {

    /// `LocalStorage` instance
    private LocalStorage localStorage;
    /// `Setting` instance
    private Setting setting;

    // Checkbox
    private CheckBox checkboxSameSchedule;

    // Layouts
    private LinearLayout parentLayout, layoutMon, layoutTue, layoutWed, layoutThu, layoutFri, layoutSat, layoutSun;

    // Buttons
    private Button btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun;

    // TextViews
    //  Monday
    private TextView startHourMorningMon, endHourMorningMon, startHourAfternoonMon, endHourAfternoonMon;
    //  Tuesday
    private TextView startHourMorningTue, endHourMorningTue, startHourAfternoonTue, endHourAfternoonTue;
    //  Wednesday
    private TextView startHourMorningWed, endHourMorningWed, startHourAfternoonWed, endHourAfternoonWed;
    //  Thursday
    private TextView startHourMorningThu, endHourMorningThu, startHourAfternoonThu, endHourAfternoonThu;
    //  Friday
    private TextView startHourMorningFri, endHourMorningFri, startHourAfternoonFri, endHourAfternoonFri;
    //  Saturday
    private TextView startHourMorningSat, endHourMorningSat, startHourAfternoonSat, endHourAfternoonSat;
    //  Sunday
    private TextView startHourMorningSun, endHourMorningSun, startHourAfternoonSun, endHourAfternoonSun;

    /**
     * onCreate method
     *
     * Setups controller and views
     * @param savedInstanceState Bundle
     */
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
        setupCheckbox();
        setupSchedules();
        setupTestButtons();
    }

    /**
     * onCreateOptionsMenu
     *
     * Assign custom menu for this controller. Formed for 3 elements:
     * - Save
     * - Reset
     * - Delete
     * @param menu Menu
     * @return Always true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * onOptionsItemSelected
     *
     * Customize click action for each menu item
     * @param item MenuItem (Save|Reset|Delete)
     * @return Boolean result from parent
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveData();
                break;
            case R.id.reset:
                resetData();
                break;
            case R.id.delete:
                deleteData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup views
     *
     * Search needed views and save references
     */
    private void setupViews() {
        parentLayout = findViewById(R.id.parentLayout);
        checkboxSameSchedule = findViewById(R.id.checkboxSameSchedule);
        setupScheduleButtons();
        setupScheduleLayouts();
        setupScheduleTextViews();
    }

    /**
     * Search needed buttons and save references
     */
    private void setupScheduleButtons() {
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

    /**
     * Search needed schedule layouts and save references
     */
    private void setupScheduleLayouts() {
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

    /**
     * Search needed schedule text view and save references
     */
    private void setupScheduleTextViews() {
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
    }

    /**
     * Setup listeners
     *
     * Setup all listeners to abilitate user to do actions
     * with view elements
     */
    private void setupListeners() {
        setupCheckboxListeners();
        setupButtonsListeners();
        setupScheduleListeners();
    }

    /**
     * Setup actions for checkbox views
     */
    private void setupCheckboxListeners() {
        checkboxSameSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setUsingSameSchedule(isChecked);
            }
        });
    }

    /**
     * Search in schedules array a schedule which have at least one hour setted
     * @param skipWeekDay Day of the week to skip
     * @return Schedule if there are on setted correctly; null otherwise
     */
    private Schedule getFirstScheduleWithHours(int skipWeekDay) {
        Schedule schedule = null;
        boolean found = false;
        int i = 0;
        while (i < 7 && !found) {
            int weekDay = i + 1;
            if (weekDay != skipWeekDay) {
                schedule = setting.getSchedule(weekDay);
                if (!TextUtils.isEmpty(schedule.getStartHourMorning()) ||
                        !TextUtils.isEmpty(schedule.getEndHourMorning()) ||
                        !TextUtils.isEmpty(schedule.getStartHourAfternoon()) ||
                        !TextUtils.isEmpty(schedule.getEndHourAfternoon())) {
                    found = true;
                }
            }
            i++;
        }
        return (found ? schedule : null);
    }

    /**
     * Setup actions for button views
     */
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
                    if (!schedule.isEnabled()) {
                        schedule.setStartHourMorning("");
                        schedule.setEndHourMorning("");
                        schedule.setStartHourAfternoon("");
                        schedule.setEndHourAfternoon("");
                    } else if (setting.isUsingSameSchedule()) {
                        Schedule settedSchedule = getFirstScheduleWithHours(schedule.getWeekDay());
                        if (settedSchedule != null) {
                            schedule.setStartHourMorning(settedSchedule.getStartHourMorning());
                            schedule.setEndHourMorning(settedSchedule.getEndHourMorning());
                            schedule.setStartHourAfternoon(settedSchedule.getStartHourAfternoon());
                            schedule.setEndHourAfternoon(settedSchedule.getEndHourAfternoon());
                        }
                    }
                    setting.setSchedule(schedule);
                    setupWeekDay(schedule.getWeekDay());
                }
            });
        }
    }

    /**
     * Setup actions for each text view that represents an hour
     */
    private void setupScheduleListeners() {
        for (int i = 0; i < 7; i++) {
            int weekDay = i + 1;
            TextView[] textViews = getTextViews(weekDay);
            final Schedule schedule;

            if (setting.getSchedule(weekDay) == null)
                setting.setSchedule(new Schedule(weekDay, "", "", "", ""));
            schedule = setting.getSchedule(weekDay);

            setupHourListener(schedule, textViews[0], Schedule.HOUR_MODE.START_MORNING);
            setupHourListener(schedule, textViews[1], Schedule.HOUR_MODE.END_MORNING);
            setupHourListener(schedule, textViews[2], Schedule.HOUR_MODE.START_AFTERNOON);
            setupHourListener(schedule, textViews[3], Schedule.HOUR_MODE.END_AFTERNOON);
        }
    }

    /**
     * Setup action for one text view that represents an hour
     * @param schedule Schedule to be modified
     * @param textView TextView to be modified
     * @param hourMode User selection mode
     */
    private void setupHourListener(final Schedule schedule, final TextView textView, final Schedule.HOUR_MODE hourMode) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scheduleHour = getScheduleHour(schedule, hourMode);
                int hour = Utils.getHour(scheduleHour);
                int minute = Utils.getMinute(scheduleHour);
                TimePickerDialog dialog = new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String finalHour = Utils.getUserHour(hourOfDay, minute);
                                if (ScheduleValidator.isValidSchedule(schedule, hourMode, finalHour))
                                    applyHourChange(finalHour, schedule, hourMode);
                                else
                                    Utils.showBasicSnackBar(parentLayout, getString(R.string.schedule_error));
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

    /**
     * Get schedule hour propertie depending on the user selection mode
     * @param schedule Schedule to be modified
     * @param hourMode User selection mode
     * @return Hour represented in String object
     */
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

    /**
     * Apply user selection for schedule requested or for all active schedules
     * It depends on usingSameSchedule setting attribute
     * @param hour New hour
     * @param schedule Schedule to be modified
     * @param hourMode User selection mode
     */
    private void applyHourChange(String hour, final Schedule schedule, final Schedule.HOUR_MODE hourMode) {
        if (setting.isUsingSameSchedule()) {
            for (int i = 0; i < 7; i++) {
                int weekDay = (i + 1);
                Schedule tmpSchedule = setting.getSchedule(weekDay);
                if (tmpSchedule == null)
                    tmpSchedule = new Schedule(weekDay, "", "", "", "");
                if (tmpSchedule.isEnabled()) {
                    saveScheduleHour(tmpSchedule, hourMode, hour);
                    setting.setSchedule(tmpSchedule);
                    setupWeekDay(tmpSchedule.getWeekDay());
                }
            }
        } else {
            saveScheduleHour(schedule, hourMode, hour);
            setting.setSchedule(schedule);
            setupWeekDay(schedule.getWeekDay());
        }
    }

    /**
     * Save in current propertie of schedule depending on the user selection mode
     * @param schedule Schedule to be modified
     * @param hourMode User selection mode
     * @param hour New hour
     */
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

    /**
     * Setup data in all checkboxs
     */
    private void setupCheckbox() {
        checkboxSameSchedule.setChecked(setting.isUsingSameSchedule());
    }

    /**
     * Setup data in all schedules
     */
    private void setupSchedules() {
        for (int i = 0; i < 7; i++) {
            final int weekDay = i + 1;
            setupWeekDay(weekDay);
        }
    }

    /**
     * Setup data in one schedule representing `weekDay`
     * @param weekDay Day of the week
     */
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

    /**
     * Get button representing `weekDay`
     * @param weekDay Day of the week
     * @return Button instance
     */
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

    /**
     * Get layout representing `weekDay`
     * @param weekDay Day of the week
     * @return LinearLayout instance
     */
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

    /**
     * Get text view collection representing `weekDay`
     * @param weekDay Day of the week
     * @return Text view collection
     */
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

    /**
     * Save current memory setting in `LocalStorage`
     */
    private void saveData() {
        if (localStorage.hasChanged(setting)) {
            localStorage.saveSetting(setting);
            resetData();
            resetAllJobs();
        }
        Utils.showBasicSnackBar(parentLayout, getString(R.string.setting_saved));
    }

    /**
     * Replace current memory setting for `LocalStorage` setting
     */
    private void resetData() {
        setting = localStorage.getCurrentSetting();
        setupCheckbox();
        setupSchedules();
        Utils.showBasicSnackBar(parentLayout, getString(R.string.setting_retored));
    }

    /**
     * Delete current setting and reset all views
     */
    private void deleteData() {
        localStorage.deleteCurrentSetting();
        setting = localStorage.getCurrentSetting();
        resetData();
        resetAllJobs();
        Utils.showBasicSnackBar(parentLayout, getString(R.string.setting_deleted));
    }

    /**
     * Reset all scheduled jobs
     *
     * Reset all alarms
     */
    private void resetAllJobs() {
        WorkhabitJobBuilder jobBuilder = WorkhabitJobBuilder.getInstance(getApplicationContext());
        jobBuilder.cancelJob();
        jobBuilder.buildNextJob();
    }

    /**
     * Method that setup buttons to test all different kind of workhabit notifications
     */
    private void setupTestButtons() {
        ((Button) findViewById(R.id.testStartWorking)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleNotificationTask task = new ScheduleNotificationTask(getApplicationContext(), GlobalJobs.START_WORK_JOB_ID, true);
                task.execute();
            }
        });
        ((Button) findViewById(R.id.testFinishWorking)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleNotificationTask task = new ScheduleNotificationTask(getApplicationContext(), GlobalJobs.END_WORK_JOB_ID, true);
                task.execute();
            }
        });
        ((Button) findViewById(R.id.testTakeBreak)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleNotificationTask task = new ScheduleNotificationTask(getApplicationContext(), GlobalJobs.TAKE_SHORT_BREAK_JOB_ID, true);
                task.execute();
            }
        });
        ((Button) findViewById(R.id.testTakeLongBreak)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleNotificationTask task = new ScheduleNotificationTask(getApplicationContext(), GlobalJobs.TAKE_LONG_BREAK_JOB_ID, true);
                task.execute();
            }
        });
        ((Button) findViewById(R.id.testExercise)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleNotificationTask task = new ScheduleNotificationTask(getApplicationContext(), GlobalJobs.DO_EXERCISE_JOB_ID, true);
                task.execute();
            }
        });
        ((Button) findViewById(R.id.testSocialize)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleNotificationTask task = new ScheduleNotificationTask(getApplicationContext(), GlobalJobs.SOCIALIZE_JOB_ID, true);
                task.execute();
            }
        });
    }

}
