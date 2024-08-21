package com.example.mealapp.utils.connection_helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkUtil {
    private static NetworkUtil instance;
    private static Context context;

    private NetworkUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new NetworkUtil(context);
        }
    }
    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return networkCapabilities != null && (
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
           }
        return false;
    }
}
