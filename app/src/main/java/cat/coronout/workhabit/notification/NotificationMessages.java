package cat.coronout.workhabit.notification;

import android.content.Context;

/**
 * Class to manage all possible messages
 *
 * Nowadays, there are only one simple kind of messages
 */
public class NotificationMessages {

    /**
     * Simple class to define notification content
     *
     * Nowadays, all notification contents are formed by title and message
     */
    public class NotificationBody {

        /// Notification title
        private String title;
        /// Notification message
        private String message;

        /**
         * Constructor
         * @param title Notification title
         * @param message Notification message
         */
        public NotificationBody(String title, String message) {
            this.title = title;
            this.message = message;
        }

        /**
         * Getter
         * @return Notification title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Getter
         * @return Notification message
         */
        public String getMessage() {
            return message;
        }

    }

    /// NotificationMessages instance; It's a singleton
    private static NotificationMessages notificationMessages;

    /// Context
    private Context context;

    /**
     * Static public method to get NotificationMessages instance
     * @param context Context
     * @return NotificationMessages instance
     */
    public static NotificationMessages getInstance(Context context) {
        if (notificationMessages == null)
            notificationMessages = new NotificationMessages(context);
        return notificationMessages;
    }

    /**
     * Private constructor
     * @param context Context
     */
    private NotificationMessages(Context context) {
        this.context = context;
    }

    /**
     * Method to buid NotificationBody by title and message resources id
     * @param titleId Title id
     * @param messageId Message id
     * @return NotificationBody object
     */
    public NotificationBody getNotificationMessage(int titleId, int messageId) {
        String title = context.getString(titleId);
        String message = context.getString(messageId);;
        return (new NotificationBody(title, message));
    }

}
