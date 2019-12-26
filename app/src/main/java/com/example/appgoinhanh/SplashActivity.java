package com.example.appgoinhanh;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;


public class SplashActivity extends AppCompatActivity {


    private GifImageView gifImageView;

    private static final int PERMISSION_REQUEST_CODE_READ_CONTACTS = 1;

    // Cấp quyền
    private boolean hasPhoneContactsPermission(String permission)
    {
        boolean ret = false;

        // If android sdk version is bigger than 23 the need to check run time permission.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // return phone read contacts permission grant status.
            int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            // If permission is granted then return true.
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                ret = true;
            }
        }else
        {
            ret = true;
        }
        return ret;
    }

    private void requestPermission(String permission, int requestCode)
    {
        String requestPermissionArray[] = {permission};
        ActivityCompat.requestPermissions(this, requestPermissionArray, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int length = grantResults.length;
        if(length > 0)
        {
            int grantResult = grantResults[0];

            if(grantResult == PackageManager.PERMISSION_GRANTED) {

                if(requestCode==PERMISSION_REQUEST_CODE_READ_CONTACTS)
                {}
            }else
            {
                Toast.makeText(getApplicationContext(), "You denied permission.", Toast.LENGTH_LONG).show();
            }
        }
    }
    // Kết thúc cấp quyền

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            if(!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
            {
                requestPermission(Manifest.permission.READ_CONTACTS, PERMISSION_REQUEST_CODE_READ_CONTACTS);
            }
        }catch (Exception ko){}

        gifImageView = (GifImageView)findViewById(R.id.splash_logo);
        try{

            InputStream inputStream = getAssets().open("splash_screen.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }
        catch (IOException ex)
        {}

        //load 1s
        new CountDownTimer(1100, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent streamPlayerHome = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(streamPlayerHome);
            }

        }.start();

    }


    ///ads
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
