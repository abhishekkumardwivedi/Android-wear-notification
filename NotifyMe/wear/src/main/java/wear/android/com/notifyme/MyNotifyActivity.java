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

public class MyNotifyActivity extends Activity {

    private static final String TAG = MyNotifyActivity.class.getName();
    private MQTTController statController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statController = new MQTTController(this);
        deviceUpdater();
    }

    private void deviceUpdater() {
        statController.registerTableOrderListener(new MQTTController.CallBack() {
            @Override
            public void updateMessage(String place, String msg) {
                sendNotificaiton(place, msg);
            }
        });
    }

    private void sendNotificaiton(String table, String msg) {

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintShowBackgroundOnly(true);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Table no [" + table + "] ")
                        .setContentText("Order [" + msg + "]")
                        .extend(wearableExtender)
                        .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        int notificationId = 1;

        notificationManager.notify(notificationId, notification);
    }
}
