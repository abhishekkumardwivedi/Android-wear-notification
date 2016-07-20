package wear.android.com.notifyme;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.StringTokenizer;

public class MQTTController {

    private static final String TAG = MQTTController.class.getName();

    public static String TOPIC_TABLE_NO;

    //Convert it to array of table number for having multiple tables assigned
    public static String TABLE_NO = "1";

    private static final String DEFAULT_BROKER = "192.168.0.101";
    private static final String DEFAULT_PORT = "8884";
    private static String CLIENT_ID = "testClient123";

    private static CallBack tableOrderListener;

    private Context mContext;

    public MQTTController(Context context) {
        mContext = context;
        startListeningNotifications();
    }

    private void startListeningNotifications() {
        setupMqttClient();
        //If want to tell anything to server here?
    }

    private void setupMqttClient() {
        Log.d(TAG, "Lets connect ... ");
        WifiManager wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        //Do some wifi checking stuff if needed. It might not be needed if reachable through other
        // wireless media.
        configTopics();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String broker = DEFAULT_BROKER;
                String port = DEFAULT_PORT;
                String client = CLIENT_ID;

                if(MQTTHelper.connect(broker, port, client)) {
                    Log.d(TAG, "connected!!");
                    Thread t = new Thread(subscriber);
                    t.start();
                } else {
                    Looper.prepare();
                    Log.d(TAG, "failed to connect!!");
                }
            }
        }).start();
    }

    private void configTopics() {
        TOPIC_TABLE_NO = "/request/" + TABLE_NO;
    }

    private void registerDevice(String key) {
        if(!MQTTHelper.isConnected()) {
            setupMqttClient();
        }
    }

    public static interface CallBack {
        void updateMessage(String msg);
    }

    public void registerTableOrderListener(CallBack callback) {
        tableOrderListener = callback;
    }

    public static void MqttMessageHandler(String s, MqttMessage mqttMessage) {
        String  data = parseMqttMessage(mqttMessage);
        if (s.equals(TOPIC_TABLE_NO) && tableOrderListener != null) {
            tableOrderListener.updateMessage(data);
        }
    }

    private static String parseMqttMessage(MqttMessage mqttPayload) {
        Log.d(TAG, mqttPayload.toString());
        return mqttPayload.toString();
    }

    static private Runnable subscriber  = new Runnable() {
        @Override
        public void run() {
            MQTTHelper.subscribe(TOPIC_TABLE_NO);
        }
    };
}