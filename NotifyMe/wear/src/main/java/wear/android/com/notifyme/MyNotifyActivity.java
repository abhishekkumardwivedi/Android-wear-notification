package wear.android.com.notifyme;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Example shell activity which simply broadcasts to our receiver and exits.
 */
public class MyNotifyActivity extends Activity {

    private static final String TAG = MyNotifyActivity.class.getName();
    private MQTTController statController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mContext = getActivity();
        statController = new MQTTController(this);
/*
        NotificationManagerCompat.from(this).cancelAll();
        int notificationId = 0;

// Titles, authors, and overdue status of some books to display
        String[] titles = { "How to survive with no food",
                "Sailing around the world",
                "Navigation on the high seas",
                "Avoiding sea monsters",
                "Salt water distillation",
                "Sail boat maintenance" };
        String[] authors = { "I. M. Hungry",
                "F. Magellan",
                "E. Shackleton",
                "K. Kracken",
                "U. R. Thirsty",
                "J. Macgyver" };
        Boolean[] overdue = { true, true, true, true, true, false };
        List extras = new ArrayList();

// Extra pages of information for the notification that will
// only appear on the wearable
        int numOverdue = 0;
        for (int i = 0; i < titles.length; i++) {
            if (!overdue[i]) continue;
            NotificationCompat.BigTextStyle extraPageStyle = new NotificationCompat.BigTextStyle();
            extraPageStyle.setBigContentTitle("Overdue Book " + (i+1))
                    .bigText("Title: " + titles[i] + ", Author: " + authors[i]);
            Notification extraPageNotification = new NotificationCompat.Builder(this)
                    .setStyle(extraPageStyle)
                    .build();
            extras.add(extraPageNotification);
            numOverdue++;
        }

// Main notification that will appear on the phone handset and the wearable
        Intent viewIntent1 = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent1 =
                PendingIntent.getActivity(this, notificationId+1, viewIntent1, 0);
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_full_cancel, "Returned", viewPendingIntent1)
                .setContentTitle("Books Overdue")
                .setContentText("You have " + numOverdue + " books due at the library")
                .setSmallIcon(R.drawable.ic_full_sad);
//        Notification notification1 = new WearableNotifications.Builder(builder1)
//                .addPages(extras)
//                .build();

// Issue the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId+1, builder1.build());
*/

        //------------------ MQTT -----------------------------
        deviceUpdater();
    }



    private void deviceUpdater() {
        statController.registerTableOrderListener(new MQTTController.CallBack() {
            @Override
            public void updateMessage(String msg) {
                Log.d(TAG, "dev: " + msg);
                sendNotificaiton(msg);
            }
        });
    }

    private void sendNotificaiton(String msg) {

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintShowBackgroundOnly(true);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Hello Android Wear")
                        .setContentText(msg)
                        .extend(wearableExtender)
                        .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        int notificationId = 1;

        notificationManager.notify(notificationId, notification);
    }
}
