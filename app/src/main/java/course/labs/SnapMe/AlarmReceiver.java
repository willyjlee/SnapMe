package course.labs.SnapMe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by william_lee on 6/23/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private Intent toHome;
    private PendingIntent toHomePending;

    private String ticker = "SnapMe";
    private String title = "Daily Selfie";
    private String content = "Time to take your Daily Selfie!";

    private int NOTIFICATION_ID = 2;

    private final long[] mVibratePattern = {0, 200, 200, 300};


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("tag", "received");
        toHome = new Intent(context, HomeActivity.class);
        toHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toHomePending = PendingIntent.getActivity(context, 0, toHome, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setTicker(ticker)
                .setSmallIcon(R.drawable.camera_icon)
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(content).setContentIntent(toHomePending).setVibrate(mVibratePattern);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID,
                notificationBuilder.build());
    }
}
