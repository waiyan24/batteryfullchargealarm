package me.waiyanphyo.batteryfullchargealarm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by waiyanphyo on 12/6/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context,Intent intent){
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, path);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
        if (isFull==true) {
            Notify("Battery Fully charged", "Unplug charger",context);
            Toast.makeText(context, "Battery Fully Charged",Toast.LENGTH_LONG).show();
        }

        String data = intent.getAction();
        if (data=="android.intent.action.ACTION_POWER_CONNECTED") {
            Toast.makeText(context, "Yeep...Charging now...", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "Charging Stopped",Toast.LENGTH_LONG).show();
        }
    }

    private void Notify(String notificationTitle,String notificationMessage,Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

//        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.kyo_nint_tot);
        Notification noti = new Notification.Builder(context.getApplicationContext())
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSmallIcon(R.mipmap.bat_full)
//                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .getNotification();

        notificationManager.notify(0, noti);
    }

}
