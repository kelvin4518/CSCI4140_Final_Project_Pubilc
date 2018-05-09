package hk.com.csci4140.culife.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import hk.com.csci4140.culife.R;

/**
 * Created by liujie(Jerry Liu) on 30/04/2018.
 */

/**
 * This class is used to check the network state dynamically
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取ConnectivityManager对象对应的NetworkInfo对象
        //获取WIFI连接的信息
        NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //获取移动数据连接的信息
        NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (!wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
            Toast.makeText(context, context.getString(R.string.network_connect_disable), Toast.LENGTH_SHORT).show();
        }
    }
}
