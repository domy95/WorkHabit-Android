package cat.coronout.workhabit.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Date;

import cat.coronout.workhabit.model.Schedule;
import cat.coronout.workhabit.model.Setting;

/**
 * LocalStorage class
 *
 * Use it to manage all events and actions between app and local storage
 */
public class LocalStorage {

    /// Storage name attribute
    private final String STORAGE_NAME = "Workhabit-Storage";
    /// Key prefix for schedules
    private final String KEY_SCHEDULE_PREFIX = "ScheduleForDay_";
    /// Key for birth date
    private final String KEY_BIRTH_DATE = "BirthDate";
    /// Key for control schedules semaphore
    private final String KEY_USING_SAME_SCHEDULE = "UsingSameSchedule";
    /// Access with reading mode in `LocalStorage`
    private SharedPreferences preferences;
    /// Access with editing mode in `LocalStorage`
    private SharedPreferences.Editor preferencesEditor;
    /// Instance of `LocalStorage`
    private static LocalStorage localStorage;

    /**
     * Method to get the instance of `LocalStorage`
     * @param context Context
     * @return `LocalStorage` instance
     */
    public static LocalStorage getInstance(Context context) {
        if (localStorage == null)
            localStorage = new LocalStorage(context);
        return localStorage;
    }

    /**
     * Constructor
     * @param context Context
     */
    private LocalStorage(Context context) {
        setupPreferences(context);
    }

    /**
     * Setup `LocalStorage` access properties
     * @param context Context
     */
    private void setupPreferences(Context context) {
        preferences = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    /**
     * Getter for Gson instance
     * @return Gson instance
     */
    private Gson getGson() {
        return (new Gson());
    }

    /**
     * Cast an object to JSON String using Gson library
     * @param object Object to cast
     * @return String representation of object in JSON format
     */
    private String objectToJson(Object object) {
        return getGson().toJson(object);
    }

    /**
     * Getter from `LocalStorage`
     * @param key Key of the attribute to search
     * @return String representations of object in JSON format
     */
    private String getObjectAsJson(String key) {
        if (preferences != null) {
            return preferences.getString(key, "");
        }
        return "";
    }

    /**
     * Save `Schedule` in `LocalStorage`
     * @param schedule Schedule to save
     */
    private void saveSchedule(Schedule schedule) {
        if (preferencesEditor != null && schedule != null) {
            preferencesEditor.putString(KEY_SCHEDULE_PREFIX + schedule.getWeekDay(), objectToJson(schedule));
            preferencesEditor.commit();
        }
    }

    /**
     * Get `Schedule` from `LocalStorage` by day of the week
     * that represents
     * @param weekDay Day of the week
     * @return Schedule representing `weekDay`
     */
    private Schedule getSchedule(int weekDay) {
        String json = getObjectAsJson(KEY_SCHEDULE_PREFIX + weekDay);
        if (!TextUtils.isEmpty(json)) {
            return getGson().fromJson(json, Schedule.class);
        }
        return null;
    }

    /**
     * Save property birth date in `LocalStorage`
     * @param date Birth date
     */
    private void saveBirthDate(Date date) {
        if (preferencesEditor != null && date != null) {
            preferencesEditor.putLong(KEY_BIRTH_DATE, date.getTime());
            preferencesEditor.commit();
        }
    }

    /**
     * Get property birth date from `LocalStorage`
     * @return Birth date
     */
    private Date getBirthDate() {
        if (preferences != null) {
            long birthDate = preferences.getLong(KEY_BIRTH_DATE, -1);
            if (birthDate < 0)
                return null;
            return (new Date(birthDate));
        }
        return null;
    }

    /**
     * Save schedules control semaphore in `LocalStorage`
     * @param usingSameSchedule Schedules control semaphore
     */
    private void saveUsingSameSchedule(boolean usingSameSchedule) {
        if (preferencesEditor != null) {
            preferencesEditor.putBoolean(KEY_USING_SAME_SCHEDULE, usingSameSchedule);
            preferencesEditor.commit();
        }
    }

    /**
     * Get schedules control semaphore from `LocalStorage`
     * @return Schedules control semaphore
     */
    private boolean getUsingSameSchedule() {
        if (preferences != null) {
            return preferences.getBoolean(KEY_USING_SAME_SCHEDULE, true);
        }
        return true;
    }

    /**
     * Delete all setting properties saved in `LocalStorage`
     */
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

    /**
     * Get a instance of Setting class setted with all attributes
     * saved in `LocalStorage`
     * @return Setting instance
     */
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

    /**
     * Save a instance of Setting; Save all it's attributes
     * @param setting Instance of Setting to save
     */
    public void saveSetting(Setting setting) {
        for(Schedule schedule : setting.getSchedules()) {
            saveSchedule(schedule);
        }
        saveBirthDate(setting.getBirthDate());
        saveUsingSameSchedule(setting.isUsingSameSchedule());
    }

    /**
     * Check if a instance of Setting is different from
     * the instance saved in `LocalStorage`
     * @param setting Instance of Setting to compare
     * @return True if there are differences
     */
    public boolean hasChanged(Setting setting) {
        Setting currentSetting = getCurrentSetting();
        return (!setting.equals(currentSetting));
    }

}
