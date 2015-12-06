package me.waiyanphyo.batteryfullchargealarm;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView txtlevel,txtstatus,txtcapacity,txtremain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtlevel = (TextView) findViewById(R.id.battery_level);
        txtstatus=(TextView)findViewById(R.id.battery_status);
        txtcapacity=(TextView)findViewById(R.id.battery_capacity);
        txtremain=(TextView)findViewById(R.id.battery_remain);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        getBatteryCapacity();
    }

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
        txtcapacity.setText(String.valueOf(batteryCapacity));
        return batteryCapacity;
    }



}
