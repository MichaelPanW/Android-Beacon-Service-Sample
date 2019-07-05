package com.michael.beaconservice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeaconService {
    ArrayList<Beacon> myBeacons;
    Time updateTime; // 更新時間

    //啟動Beacon物件
    BeaconExecutionFile beaconExecutionFile;
    BeaconService(Context context,int time){
        myBeacons=new ArrayList<Beacon>();
        updateTime=new Time();

        beaconExecutionFile = new BeaconExecutionFile(context,searchBracon,null,time);

    }
    BeaconManager.RangingListener searchBracon=new BeaconManager.RangingListener() {
        @Override
        public void onBeaconsDiscovered(Region region, List<Beacon> list) {
            myBeacons.clear();
            ComparatorBeaconByRssi com = new ComparatorBeaconByRssi();
            Collections.sort(myBeacons, com);
            if(BeaconExecutionFile.getRecentBeaconTool(list)!=null){
                myBeacons = BeaconExecutionFile.getRecentBeaconTool(list);
                updateTime.setToNow(); // 取得系統時間。

            }
        }
    };    //顯示Beacon值
    ArrayList<Beacon> getArray(){
        return myBeacons;
    }
    int size(){
        return  myBeacons.size();
    }
    String getTime(){
        return updateTime.minute+":"+updateTime.second;
    }
    /*
    void disPlayBeaconData(Beacon beacon) {
        distance = beacon.getDistance();
        //获取beacon的mac地址
        macAddress = beacon.getMacAddress();
        //获取beacon的Major值
        major = beacon.getMajor();
        //获取beacon的measurePower
        measuredPower = beacon.getMeasuredPower();
        //获取beacon的minor值
        minor = beacon.getMinor();
        //获取beacon名称
        name = beacon.getName();
        //获取beacon的电量（固件版本2.1及以上使用）
        power = beacon.getPower();
        //获取beacon的远近情况
        proximity = beacon.getProximity();
        //获取beacon的ProximityUUID
        proximityUUID = beacon.getProximityUUID();
        //获取beacon的RSSI
        rssi = beacon.getRssi();
        Log.e("beacon",macAddress);

    }
    */
}
