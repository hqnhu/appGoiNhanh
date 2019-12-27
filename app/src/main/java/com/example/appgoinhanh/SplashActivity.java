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

    private boolean hasPhoneContactsPermission(String permission)
    {
        boolean ret = false;
        //Nếu phiên bản android sdk lớn hơn 23 thì cần kiểm tra thời gian chạy.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // trả lại điện thoại đọc danh bạ cấp quyền
            int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            // If permission is granted then return true.
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                ret = true;
                Intent streamPlayerHome = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(streamPlayerHome);
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
                {
                    Intent streamPlayerHome = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(streamPlayerHome);
                }

            }else
            {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                Toast.makeText(getApplicationContext(), "Bạn đã từ chối sự cho phép.", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            if(!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
            {
                requestPermission(Manifest.permission.READ_CONTACTS, PERMISSION_REQUEST_CODE_READ_CONTACTS);

            }else{

            }
        }catch (SecurityException px){
            ActivityCompat.requestPermissions(
                    SplashActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_READ_CONTACTS);
            return;
        }

        gifImageView = (GifImageView)findViewById(R.id.splash_logo);
        try{

            //tạo hoạt ảnh cho gif
            InputStream inputStream = getAssets().open("splash_screen.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }
        catch (IOException ex)
        {

        }


//        new CountDownTimer(2500, 2500) {
//
//            public void onTick(long millisUntilFinished) {
////
//            }
//
//            public void onFinish() {
//                Intent streamPlayerHome = new Intent(SplashActivity.this,MainActivity.class);
//                startActivity(streamPlayerHome);
//            }
//
//        }.start();

    }


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
