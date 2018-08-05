//Ryan Guido
//07/03/17
//Lumos~Simple HP inspired flashlight app


package org.lumos.lumos;

//*****COMMENTED OUT VERSION IS ORIGINAL CODE FOR ORIGINAL API (CAMERA). UPDATED TO NEW API THAT USES CAMERA2
//import android.content.DialogInterface;
//import android.content.pm.PackageManager;
//import android.hardware.camera;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//
//public class MainActivity extends AppCompatActivity
//{
//    ImageButton imageButton;
//    Camera camera;
//    Camera.Parameters parameters;
//    boolean isFlash = false;
//    boolean isOn = false;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        imageButton = (ImageButton)findViewById(R.id.ImageButton);
//
//        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
//        {
//            camera = Camera.open();
//            parameters = camera.getParameters();
//            isFlash = true;
//        }
//        //if wand (image button is clicked)
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (isFlash)
//                {
//                    if (!isOn)
//                    {
//                        imageButton.setImageResource(R.drawable.on);
//                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                        camera.setParameters(parameters);
//                        camera.startPreview();
//                        isOn = true;
//                    }
//
//                    else
//                    {
//                        imageButton.setImageResource(R.drawable.off);
//                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                        camera.setParameters(parameters);
//                        camera.stopPreview();
//                        isOn = false;
//                    }
//                }
//
//                else
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("Error");
//                    builder.setMessage("Flash not accessible on device");
//                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    });
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                }
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (camera != null)
//        {
//            camera.release();//flashlight is turned off if app is exited
//            camera = null;
//        }
//    }
//}
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageButton mFlashOnOffButton;
    private Boolean isFlashOn;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//specifically for new Android version
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate()");
        setContentView(R.layout.activity_main);
        mFlashOnOffButton = (ImageButton) findViewById(R.id.ImageButton);
        isFlashOn = false;

    Boolean isFlashAvailable = getApplicationContext().getPackageManager()
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setTitle("Error");
        alert.setMessage("Your device does not support flash light");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // closing the application
                finish();
                System.exit(0);
            }
        });
        alert.show();
        return;
    }

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        mFlashOnOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isFlashOn) {
                        turnOffFlashLight();
                        isFlashOn = false;
                    } else {
                        turnOnFlashLight();
                        isFlashOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
                mFlashOnOffButton.setImageResource(R.drawable.on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                mFlashOnOffButton.setImageResource(R.drawable.off);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(isFlashOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isFlashOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFlashOn){
            turnOnFlashLight();
        }
    }
}