package com.clockyface.clocky;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.facebook.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Created by HKN on 23.03.2015.
 */
public class AlarmScreen extends Activity {
    private static final List<String> PERMİSSİONS= Arrays.asList("publish_actions");
    private static AlarmScreen alfrags;
    private AlarmManager alarmmanager;
    private PendingIntent pintents;
    private UiLifecycleHelper helper;
    private TimePicker picker;
    private EditText content;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private int hour;
    private int minute;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper=new UiLifecycleHelper(this,callback);
        helper.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        content=(EditText)findViewById(R.id.contents);
        alarmmanager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmcancel(pintents);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater minflater=getMenuInflater();
        minflater.inflate(R.menu.menus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.alarm:
                dialogbuilder=new AlertDialog.Builder(AlarmScreen.this);
                dialogbuilder.setTitle("Alarm Setting");
                dialogbuilder.setIcon(R.drawable.logo);
                dialogbuilder.setCancelable(true);
                dialogbuilder.setView(getview());
                dialogbuilder.setPositiveButton("SET THE ALARM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent ints = new Intent(AlarmScreen.this, AlarmReceiver.class);
                        pintents = PendingIntent.getBroadcast(AlarmScreen.this, 0, ints, 0);
                        alarmmanager.set(AlarmManager.RTC, getcalend().getTimeInMillis(), pintents);
                    }
                });
                dialogbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          dialog.dismiss();
                    }
                });
                dialog=dialogbuilder.create();
                dialog.show();
                return true;
            case R.id.shares:
                ShareContent(content.getText().toString());
                Toast.makeText(getApplicationContext(),"Shared On Facebook",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private View getview(){
        LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.timepicker,null);
        picker=(TimePicker)view.findViewById(R.id.time);
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i2) {
               hour=i;
               minute=i2;
            }
        });
        return view;

    }
    private Session.StatusCallback callback=new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if(state.isOpened()){
                Log.d("facebookfragment", "facebook session opened");

            }else if(state.isClosed()){
                Log.d("facebookfragment","facebook session closed");
            }
        }
    };
    private void ShareContent(String contents){
        if(checkpermission()){
            Request request=Request.newStatusUpdateRequest(Session.getActiveSession(), contents, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    if(response.getError() == null){
                        Toast.makeText(getApplicationContext(),"Shared On Facebook",Toast.LENGTH_LONG).show();
                    }
                }
            });
            request.executeAsync();
        }else{
            requestpermission();
        }
    }
    private boolean checkpermission(){
        Session s=Session.getActiveSession();
        if(s!=null){
            return s.getPermissions().contains("publish_actions");
        }else{
            return false;
        }
    }
    private void requestpermission(){
        Session s=Session.getActiveSession();
        if(s!=null){
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(this,PERMİSSİONS));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        helper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        helper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        helper.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.onSaveInstanceState(outState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        alfrags=this;
    }
    private void alarmcancel(PendingIntent pint){
        try {
            alarmmanager.cancel(pint);
        }catch (Exception e){
            Log.d("alarmexception",e.getMessage());
        }
    }
    private Calendar getcalend(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
        calendar.set(Calendar.MINUTE, picker.getCurrentMinute());
        return calendar;
    }
    public static AlarmScreen ınstance(){
        return alfrags;
    }
}
