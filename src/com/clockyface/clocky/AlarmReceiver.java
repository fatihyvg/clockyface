package com.clockyface.clocky;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by HKN on 24.03.2015.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         AlarmScreen alfrag= AlarmScreen.ınstance();
        Uri aluri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(aluri==null){
            aluri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ring=RingtoneManager.getRingtone(context,aluri);
        ring.play();
        ComponentName comp=new ComponentName(context.getPackageName(),AlarmService.class.getName());
        startWakefulService(context,(intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }
}
