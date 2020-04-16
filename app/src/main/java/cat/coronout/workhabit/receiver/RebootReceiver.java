package cat.coronout.workhabit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cat.coronout.workhabit.job.WorkhabitJobBuilder;
import cat.coronout.workhabit.util.GlobalJobs;

/**
 * BroadcastReceiver
 *
 * It is used to catch system reboot events. We schedule this events to restore
 * next job alarm.
 */
public class RebootReceiver extends BroadcastReceiver {

    /**
     * onReceive
     *
     * Called by every broadcast call. We schedule here next job alarm
     * @param context Context
     * @param intent Call Intent instance
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(GlobalJobs.GLOBAL_JOBS_TAG, "RebootReceiver onReceive: " + intent.getAction());
        //Disabled to catch in WorkhabitPublisher
    }

}
