package com.clockyface.clocky;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by HKN on 24.03.2015.
 */
public class AlarmService extends IntentService {
    private NotificationManager notmanager;
    public AlarmService(){
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       SendNotif("What's On Your Mind?");
    }
    private void SendNotif(String message){
        PowerManagerStates();
        notmanager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pintent=PendingIntent.getActivity(this,0,new Intent(this,AlarmScreen.class),0);
        NotificationCompat.Builder alnotif=new NotificationCompat.Builder(this);
        alnotif.setContentTitle("ClockyFace");
        alnotif.setSmallIcon(R.drawable.logo);
        alnotif.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        alnotif.setContentText(message);
        alnotif.setContentIntent(pintent);
        notmanager.notify(1, alnotif.build());
        Log.d("AlarmService","Notification");
    }
    private void PowerManagerStates(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alarm");
        wl.acquire();
    }
}
