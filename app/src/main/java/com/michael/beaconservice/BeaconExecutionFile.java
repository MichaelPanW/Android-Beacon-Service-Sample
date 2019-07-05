package com.michael.beaconservice;

import android.content.Context;
import android.os.RemoteException;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.utils.AprilL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by J on 2017/7/5.
 */

public class BeaconExecutionFile {
    private BeaconManager beaconManager;
    private Region ALL_BEACONS_REGION;
    private BeaconManager.RangingListener searchBracon;
    private Context context;
    int delay_time;
    BeaconExecutionFile(Context contextInterface,BeaconManager.RangingListener searchBraconInterface,String uuid,int time) {
        delay_time=time;
        AprilL.enableDebugLogging(true);
        context=contextInterface;
        searchBracon=searchBraconInterface;
        ALL_BEACONS_REGION = new Region("apr", uuid, null, null);
        beaconManager = new BeaconManager(context);
        if (detect()) {
            startUp();
        }
    }
//檢查藍芽
    public boolean detect() {
        //检测是否支持蓝牙4.0BLE 不是回傳 NoFourPointZeroBlueBuds
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(context, "不支援支持蓝牙4.0BLE", Toast.LENGTH_LONG);
            return false;
        }
        //检测蓝牙是否可用 可用回傳 true
        if (!beaconManager.isBluetoothEnabled()) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 连接服务 开始搜索beacon connect service start scan beacons
     */
    public void startUp() {
               beaconManager.setForegroundScanPeriod(delay_time,delay_time);
                beaconManager.setRangingListener(searchBracon);
                beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                    @Override
                    public void onServiceReady() {
                        try {
                            beaconManager.startRanging(ALL_BEACONS_REGION);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //關閉偵測
    public void shut() {
        try {
            beaconManager.stopRanging(ALL_BEACONS_REGION);
            beaconManager.disconnect();
        } catch (RemoteException e) {
            // Log.d(TAG, "Error while stopping ranging", e);
        }
    }

    //最近的Beacon
    public static ArrayList<Beacon> getRecentBeaconTool(List<Beacon> beacons) {
        ArrayList<Beacon> myBeacons = new ArrayList<Beacon>();
        //myBeacons.clear();
        myBeacons.addAll(beacons);
        if (myBeacons.isEmpty() != true) {
            ComparatorBeaconByRssi com = new ComparatorBeaconByRssi();
            Collections.sort(myBeacons, com);
            return myBeacons;
        } else {
            return null;
        }
    }

/*
    //存取值Beacon值
    public void setRecentBeacon(List<Beacon> beacons) {
        myBeacons.clear();
        aBoolean=false;
        myBeacons.addAll(beacons);
        aBoolean=true;
    }
    public ArrayList<Beacon> getRecentBeacon() {
        if(aBoolean){
            return myBeacons;
        }else{
            return null;
        }
    }

    //Beacon的監聽事件
    BeaconManager.RangingListener selecBracon = new BeaconManager.RangingListener() {
        @Override
        public void onBeaconsDiscovered(Region region, List<Beacon> list) {
            setRecentBeacon(list);
        }
    };*/
}

//Beacon的比較器
    class ComparatorBeaconByRssi implements Comparator<Beacon> {
        @Override
        public int compare(Beacon lhs, Beacon rhs) {
            //Beacon接收的信號強度指示的強度做比對
            if (lhs.getRssi() == rhs.getRssi()) {
                //获取beacon的ProximityUUID
                //將此對象與指定的對象進行比較以進行排序。返回一個負整數，零或正整數，因為該對像小於，等於或大於指定的對象。
                int flag = lhs.getProximityUUID().compareTo(rhs.getProximityUUID());
                if (flag == 0) {
                    //获取beacon的Major值
                    if (lhs.getMajor() == rhs.getMajor()) {
                        //获取beacon的minor值
                        if (lhs.getMinor() == rhs.getMinor()) {
                            return 0;
                        } else {
                            return lhs.getMinor() - rhs.getMinor();
                        }
                    } else {
                        return lhs.getMajor() - rhs.getMajor();
                    }
                } else {
                    return flag;
                }
            } else {
                return rhs.getRssi() - lhs.getRssi();
            }
        }
    }