package cat.coronout.workhabit.analytics;

import java.util.HashMap;

/**
 * Interface to define any analytics service
 *
 * Used as a template for all kind of analytics that Workhabit can use
 */
public interface AnalyticsService {

    /**
     * Send event method
     * @param eventId Event id
     */
    void sendEvent(int eventId);

    /**
     * Send event method with custom properties
     * @param eventId Event id
     * @param eventProperties Custom event properties
     */
    void sendEvent(int eventId, HashMap<String, Object> eventProperties);

}
