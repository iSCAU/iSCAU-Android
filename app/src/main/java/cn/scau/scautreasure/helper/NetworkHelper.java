package cn.scau.scautreasure.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import org.androidannotations.annotations.EBean;

/**
 * Created by macroyep on 14/12/26.
 */
@EBean
public class NetworkHelper {
    public enum NetworkType {
        Wifi("wifi"), G2("2g"), G3("3g"), G4("4g"), NONE("none");
        private String name;

        NetworkType(String _name) {
            this.name = _name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 得到当前的手机网络类型
     *
     * @param context
     * @return
     */
    public NetworkType getCurrentNetType(Context context) {
        NetworkType type = NetworkType.NONE;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = NetworkType.NONE;
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = NetworkType.Wifi;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = NetworkType.G2;
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = NetworkType.G3;
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = NetworkType.G4;
            }
        }
        return type;
    }
}
