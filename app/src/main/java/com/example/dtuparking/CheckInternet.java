package com.example.dtuparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class CheckInternet {
    public static boolean isConnect(Context context){
        // kiểm tra kết nối internet
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if(infos != null){
                for(int i=0 ; i<infos.length ; i++){
                    if(infos[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
