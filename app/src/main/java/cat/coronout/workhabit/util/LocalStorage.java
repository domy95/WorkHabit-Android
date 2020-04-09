package cat.coronout.workhabit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Date;

import cat.coronout.workhabit.model.Schedule;
import cat.coronout.workhabit.model.Setting;

public class LocalStorage {

    private final String STORAGE_NAME = "Workhabit-Storage";
    private final String KEY_SCHEDULE_PREFIX = "ScheduleForDay_";
    private final String KEY_BIRTH_DATE = "BirthDate";
    private final String KEY_USING_SAME_SCHEDULE = "UsingSameSchedule";

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

    private static LocalStorage localStorage;

    public static LocalStorage getInstance(Context context) {
        if (localStorage == null)
            localStorage = new LocalStorage(context);
        return localStorage;
    }

    private LocalStorage(Context context) {
        setupPreferences(context);
    }

    private void setupPreferences(Context context) {
        preferences = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    private Gson getGson() {
        return (new Gson());
    }

    private String objectToJson(Object object) {
        return getGson().toJson(object);
    }

    private String getObjectAsJson(String key) {
        if (preferences != null) {
            return preferences.getString(key, "");
        }
        return "";
    }

    private void saveSchedule(Schedule schedule) {
        if (preferencesEditor != null && schedule != null) {
            preferencesEditor.putString(KEY_SCHEDULE_PREFIX + schedule.getWeekDay(), objectToJson(schedule));
            preferencesEditor.commit();
        }
    }

    private Schedule getSchedule(int weekDay) {
        String json = getObjectAsJson(KEY_SCHEDULE_PREFIX + weekDay);
        if (!TextUtils.isEmpty(json)) {
            return getGson().fromJson(json, Schedule.class);
        }
        return null;
    }

    private void saveBirthDate(Date date) {
        if (preferencesEditor != null && date != null) {
            preferencesEditor.putLong(KEY_BIRTH_DATE, date.getTime());
            preferencesEditor.commit();
        }
    }

    private Date getBirthDate() {
        if (preferences != null) {
            long birthDate = preferences.getLong(KEY_BIRTH_DATE, -1);
            if (birthDate < 0)
                return null;
            return (new Date(birthDate));
        }
        return null;
    }

    private void saveUsingSameSchedule(boolean usingSameSchedule) {
        if (preferencesEditor != null) {
            preferencesEditor.putBoolean(KEY_USING_SAME_SCHEDULE, usingSameSchedule);
            preferencesEditor.commit();
        }
    }

    private boolean getUsingSameSchedule() {
        if (preferences != null) {
            return preferences.getBoolean(KEY_USING_SAME_SCHEDULE, true);
        }
        return true;
    }

    public void deleteCurrentSetting() {
        if (preferencesEditor != null) {
            for (int i = 0; i < 7; i++) {
                int weekDay = (i + 1);
                preferencesEditor.remove(KEY_SCHEDULE_PREFIX + weekDay);
            }
            preferencesEditor.remove(KEY_BIRTH_DATE);
            preferencesEditor.remove(KEY_USING_SAME_SCHEDULE);
            preferencesEditor.commit();
        }
    }

    public Setting getCurrentSetting() {
        Setting setting = new Setting();
        for (int i = 0; i < 7; i++) {
            Schedule schedule = getSchedule((i + 1));
            if (schedule != null)
                setting.setSchedule(schedule);
        }
        setting.setBirthDate(getBirthDate());
        setting.setUsingSameSchedule(getUsingSameSchedule());
        return setting;
    }

    public void saveSetting(Setting setting) {
        for(Schedule schedule : setting.getSchedules()) {
            saveSchedule(schedule);
        }
        saveBirthDate(setting.getBirthDate());
        saveUsingSameSchedule(setting.isUsingSameSchedule());
    }

    public boolean hasChanged(Setting setting) {
        Setting currentSetting = getCurrentSetting();
        return (!setting.equals(currentSetting));
    }

}
