package com.clockyface.clocky;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private LoginButton loginbut;
    private UiLifecycleHelper helper;
    private static final List<String> PERMİSSİONS= Arrays.asList("publish_actions");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper=new UiLifecycleHelper(this,callback);
        helper.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loginbut=(LoginButton)findViewById(R.id.login);
        loginbut.setUserInfoChangedCallback(setuser);
        try{
            PackageInfo inf=getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),PackageManager.GET_SIGNATURES);
            for(Signature sign : inf.signatures){
                MessageDigest dg=MessageDigest.getInstance("SHA");
                dg.update(sign.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(dg.digest(),Base64.DEFAULT));
            }

        }catch (PackageManager.NameNotFoundException e){
            Log.d("namenotfound",e.getMessage());
        }catch (NoSuchAlgorithmException e){
            Log.d("algorithmexception",e.getMessage());
        }
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
    private LoginButton.UserInfoChangedCallback setuser=new LoginButton.UserInfoChangedCallback() {
        @Override
        public void onUserInfoFetched(GraphUser user) {
            if(user != null){
               Intent ip=new Intent(MyActivity.this,AlarmScreen.class);
               startActivity(ip);
            }
        }
    };
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
}
