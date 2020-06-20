package com.procter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.procter.utils.ConnectionDetector;


public class NetworkChangeReceiver extends BroadcastReceiver {
    public static NetworkStatusListener networkStatusListener;
    ConnectionDetector connectionDetector;

    @Override
    public void onReceive(Context context, Intent intent) {
        connectionDetector = new ConnectionDetector(context);
        if (networkStatusListener != null) {
            networkStatusListener.onNetworkConnectionChanged(connectionDetector.isConnectingToInternet());
        }
    }

    public interface NetworkStatusListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
