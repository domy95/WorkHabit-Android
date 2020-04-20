package cat.coronout.workhabit.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * First AnalyticsService used in Workhabit app
 *
 * It deal with Firebase platform
 */
public class WorkhabitFirebaseAnalytics implements AnalyticsService {

    /// Firebase event id for DAILY_ALARM event
    private final String DAILY_ALARM_ID = "dailyAlarm";
    /// Firebase event id for JOB event
    private final String JOB_ID = "job";
    /// Firebase event id for BOOT_COMPLETED event
    private final String BOOT_COMPLETED_ID = "bootCompleted";
    /// Firebase event id for SAVE_SETTINGS event
    private final String SAVE_SETTINGS_ID = "saveSettings";
    /// Firebase event id for RESTORE_SETTINGS event
    private final String RESTORE_SETTINGS_ID = "restoreSettings";
    /// Firebase event id for DELETE_SETTINGS event
    private final String DELETE_SETTINGS_ID = "deleteSettings";
    /// Firebase event id for DEFAULT event
    private final String DEFAULT_EVENT_ID = "defaultEvent";
    /// Firebase event name for DAILY_ALARM event
    private final String DAILY_ALARM_NAME = "Daily alarm";
    /// Firebase event name for JOB event
    private final String JOB_NAME = "Job";
    /// Firebase event name for BOOT_COMPLETED event
    private final String BOOT_COMPLETED_NAME = "Boot completed";
    /// Firebase event name for SAVE_SETTINGS event
    private final String SAVE_SETTINGS_NAME = "Save settings";
    /// Firebase event name for RESTORE_SETTINGS event
    private final String RESTORE_SETTINGS_NAME = "Restore settings";
    /// Firebase event name for DELETE_SETTINGS event
    private final String DELETE_SETTINGS_NAME = "Delete settings";
    /// Firebase event name for DEFAULT event
    private final String DEFAULT_EVENT_NAME = "Default event";

    /// Private firebase analytics instance
    private FirebaseAnalytics firebaseAnalytics;
    /// Static Workhabit analytics service instance
    private static WorkhabitFirebaseAnalytics workhabitFirebaseAnalytics;

    /**
     * Static method to get an instance of WorkhabitFirebaseAnalytics
     * @param context Context
     * @return WorkhabitFirebaseAnalytics instance
     */
    public static WorkhabitFirebaseAnalytics getInstance(Context context) {
        if (workhabitFirebaseAnalytics == null) workhabitFirebaseAnalytics = new WorkhabitFirebaseAnalytics(context);
        return workhabitFirebaseAnalytics;
    }

    /**
     * Private constructor
     * @param context Context
     */
    private WorkhabitFirebaseAnalytics(Context context) {
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    /**
     * Send event method
     *
     * Method from AnalyticsService interface for all kind of analytic services
     * @param eventId Event id
     */
    @Override
    public void sendEvent(int eventId) {
        Bundle bundle = setupBundleEvent(eventId, null);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Send event method with custom properties
     *
     * Method from AnalyticsService interface for all kind of analytic services
     * @param eventId Event id
     * @param eventProperties Custom event properties
     */
    @Override
    public void sendEvent(int eventId, HashMap<String, Object> eventProperties) {
        Bundle bundle = setupBundleEvent(eventId, eventProperties);
        firebaseAnalytics.logEvent(getFirebaseEvent(eventId), bundle);
    }

    /**
     * Setup bundle method for Firebase platform
     * @param eventId Event id
     * @param eventProperties Custom event properties
     * @return Bundle with Firebase needed params
     */
    private Bundle setupBundleEvent(int eventId, HashMap<String, Object> eventProperties) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getFirebaseEventId(eventId));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getFirebaseEventName(eventId));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, getFirebaseContentType(eventId));
        bundle = setupFirebaseEventProperties(bundle, eventProperties);
        return bundle;
    }

    /**
     * Getter
     *
     * Gets firebase event Content Type by event id
     * @param eventId Event id
     * @return Firebase event kind
     */
    private String getFirebaseContentType(int eventId) {
        switch (eventId) {
            case AnalyticId.DAILY_ALARM_ID:
            case AnalyticId.JOB_ID:
            case AnalyticId.BOOT_COMPLETED_ID:
                return "job";
            case AnalyticId.SAVE_SETTINGS_ID:
            case AnalyticId.RESTORE_SETTINGS_ID:
            case AnalyticId.DELETE_SETTINGS_ID:
                return "settings";
            default:
                return "unknown_type";
        }
    }

    /**
     * Getter
     *
     * Gets firebase event by event id
     * @param eventId Event id
     * @return Firebase event kind
     */
    private String getFirebaseEvent(int eventId) {
        switch (eventId) {
            case AnalyticId.DAILY_ALARM_ID:
            case AnalyticId.JOB_ID:
            case AnalyticId.BOOT_COMPLETED_ID:
                return getFirebaseEventName(eventId);
            case AnalyticId.SAVE_SETTINGS_ID:
            case AnalyticId.RESTORE_SETTINGS_ID:
            case AnalyticId.DELETE_SETTINGS_ID:
                return FirebaseAnalytics.Event.SELECT_CONTENT;
            default:
                return "unknown_event";
        }
    }

    /**
     * Getter
     *
     * Gets firebase event id by event id
     * @param eventId Event id
     * @return Firebase event id
     */
    private String getFirebaseEventId(int eventId) {
        switch (eventId) {
            case AnalyticId.DAILY_ALARM_ID:
                return DAILY_ALARM_ID;
            case AnalyticId.JOB_ID:
                return JOB_ID;
            case AnalyticId.BOOT_COMPLETED_ID:
                return BOOT_COMPLETED_ID;
            case AnalyticId.SAVE_SETTINGS_ID:
                return SAVE_SETTINGS_ID;
            case AnalyticId.RESTORE_SETTINGS_ID:
                return RESTORE_SETTINGS_ID;
            case AnalyticId.DELETE_SETTINGS_ID:
                return DELETE_SETTINGS_ID;
            default:
                return DEFAULT_EVENT_ID;
        }
    }

    /**
     * Getter
     *
     * Gets firebase event name by event id
     * @param eventId Event id
     * @return Firebase event name
     */
    private String getFirebaseEventName(int eventId) {
        switch (eventId) {
            case AnalyticId.DAILY_ALARM_ID:
                return DAILY_ALARM_NAME;
            case AnalyticId.JOB_ID:
                return JOB_NAME;
            case AnalyticId.BOOT_COMPLETED_ID:
                return BOOT_COMPLETED_NAME;
            case AnalyticId.SAVE_SETTINGS_ID:
                return SAVE_SETTINGS_NAME;
            case AnalyticId.RESTORE_SETTINGS_ID:
                return RESTORE_SETTINGS_NAME;
            case AnalyticId.DELETE_SETTINGS_ID:
                return DELETE_SETTINGS_NAME;
            default:
                return DEFAULT_EVENT_NAME;
        }
    }

    /**
     * Setup custom properties in bundle object
     * @param bundle Bundle object to add custom properties
     * @param eventProperties Custom event properties
     * @return Bundle with properties setted
     */
    private Bundle setupFirebaseEventProperties(Bundle bundle, HashMap<String, Object> eventProperties) {
        if (eventProperties != null) {
            for (Map.Entry<String, Object> entry : eventProperties.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) bundle.putString(key, ((String) value));
                else if (value instanceof Long) bundle.putLong(key, ((Long) value));
                else if (value instanceof Double) bundle.putDouble(key, ((Double) value));
            }
        }
        return bundle;
    }

}
