package nc.opt.mobile.optmobile.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import nc.opt.mobile.optmobile.activity.MainActivity;

/**
 * Created by 2761oli on 10/10/2017.
 */

public class NotificationSender {
    private static final int OPT_NOTIFICATION_ID = 100;
    private static final String OPT_CHANNEL_ID = "OPT_CHANNEL";

    private NotificationSender() {
    }

    public static void sendNotification(Context context, String title, String textContent, @DrawableRes int drawableRes) {

        // The id of the channel.
        String channel = OPT_CHANNEL_ID;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, channel)
                        .setSmallIcon(drawableRes)
                        .setContentTitle(title)
                        .setContentText(textContent)
                        .setChannelId(channel);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // OPT_NOTIFICATION_ID is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify(OPT_NOTIFICATION_ID, mBuilder.build());
    }
}
