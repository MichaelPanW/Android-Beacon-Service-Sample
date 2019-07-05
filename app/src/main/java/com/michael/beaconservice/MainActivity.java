package com.michael.beaconservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.aprilbrother.aprilbrothersdk.Beacon;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    BeaconService bService;
    //更新時間
    private int updateTime=2000;
    private Handler handler = new Handler();
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        //更新時間
        bService=new BeaconService(this,5000);
        text=(TextView)findViewById(R.id.text);
        //設定定時要執行的方法
        handler.removeCallbacks(updateTimer);
        //設定Delay的時間
        handler.postDelayed(updateTimer, updateTime);

    }
    //計時器
    private Runnable updateTimer = new Runnable() {
        public void run() {
            ArrayList<Beacon> myBeacons=bService.getArray();
            String outPut="time="+bService.getTime()+",count:"+myBeacons.size();
            for(int i=0;i<myBeacons.size();i++){
                outPut=outPut+"\n"+String.valueOf(myBeacons.get(i).getMacAddress());
            }
            text.setText(outPut);
            handler.postDelayed(this, updateTime);
        }
    };
    //檢查權限
    void requestPermissions(){
        //<詢問是否開起藍芽使用權限>
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                //Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
                Log.e("BeaconPermission","The permission to get BLE location data is required");
            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                //Log.e("BeaconPermission","The permission to get BLE location data is required");
            }
        }else{
            // Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
            Log.e("BeaconPermission","Location permissions already granted");
        }
        //<詢問是否開起藍芽使用權限/>
    }
}
