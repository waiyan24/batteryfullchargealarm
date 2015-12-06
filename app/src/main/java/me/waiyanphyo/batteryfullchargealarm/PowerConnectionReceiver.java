package me.waiyanphyo.batteryfullchargealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
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
//        r.play();
        String data = intent.getAction();
        if (data=="android.intent.action.ACTION_POWER_CONNECTED") {
            Toast.makeText(context, "Yeep...Charging now...", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "Charging Stopped",Toast.LENGTH_LONG).show();
        }
    }


}
