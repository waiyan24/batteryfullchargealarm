package me.waiyanphyo.batteryfullchargealarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView txtlevel,txtstatus;
    Button testnoti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testnoti = (Button) findViewById(R.id.show_noti);
        txtlevel = (TextView) findViewById(R.id.battery_level);
        txtstatus=(TextView)findViewById(R.id.battery_status);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        getBatteryCapacity();
        testnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify("Battery Fully Charged","unplug your charger");
                Toast.makeText(getApplicationContext(),"Test Button Click", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void Notify(String notificationTitle, String notificationMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

//        Notification notification = new Notification(R.mipmap.ic_launcher,"New Message", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kyo_nint_tot);
        Notification noti = new Notification.Builder(getApplicationContext())
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSmallIcon(R.mipmap.bat_full)
                .setContentIntent(pendingIntent)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setTicker(notificationTitle+"\n"+notificationMessage)
                .build();

        notificationManager.notify(1, noti);
    }

//    private void createAndGenerateNotifcation() {
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                this).setContentTitle("Testing Notification").setContentText("Unplug your charger");
//        mBuilder.setAutoCancel(true);
//        try {
//            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
//                    + R.raw.kyo_nint_tot);
//            mBuilder.setSound(uri);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // Intent resultIntent = new Intent(this, Notification.class);
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(Notification.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify((int) System.currentTimeMillis(),
//                mBuilder.build());
//    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;

            Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), path);

            if (isCharging){
                txtstatus.setText("Charging");
            }else if (isFull){
                txtstatus.setText("Full Charged");
                r.play();
            }else {
                txtstatus.setText("DisCharging");
                r.stop();
            }

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            txtlevel.setText(String.valueOf(level) + "%");
        }
    };

    public Double getBatteryCapacity() {

        Object mPowerProfile_ = null;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);

        } catch (Exception e) {

            // Class not found?
            e.printStackTrace();
        }

        try {

            // Invoke PowerProfile method "getAveragePower" with param
            // "battery.capacity"
            batteryCapacity = (Double) Class.forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

        } catch (Exception e) {

            // Something went wrong
            e.printStackTrace();
        }
//        txtcapacity.setText(String.valueOf(batteryCapacity));
        return batteryCapacity;
    }



}
