package cn.scau.scautreasure.helper;

/**
 * Created by apple on 14-9-4.
 *网络工具类
 */


import java.io.IOException;
        import java.io.UnsupportedEncodingException;
        import java.util.List;
        import org.apache.http.HttpResponse;
        import org.apache.http.HttpStatus;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.client.methods.HttpUriRequest;
        import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
        import org.json.JSONException;
        import org.json.JSONObject;
        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.util.Log;
        import android.widget.Toast;

public class NetUtil {
    private static final String TAG = "NetHelper";

    /**
     * 网络连接是否可用
     */
    public static boolean isConnnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        Log.e(TAG, "the net is ok");
                        return true;
                    }
                }
            }
        }
        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 网络可用状态下，通过get方式向server端发送请求，并返回响应数据
     *
     * @param strUrl 请求网址
     * @param context 上下文
     * @return 响应数据
     */
    public static JSONObject getResponseForGet(String strUrl, Context context) {
        if (isConnnected(context)) {
            return getResponseForGet(strUrl);
        }
        return null;
    }

    /**
     * 通过Get方式处理请求，并返回相应数据
     *
     * @param strUrl 请求网址
     * @return 响应的JSON数据
     */
    public static JSONObject getResponseForGet(String strUrl) {
        HttpGet httpRequest = new HttpGet(strUrl);
        return getRespose(httpRequest);
    }

    /**
     * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据
     *
     * @param market_uri 请求网址
     * @param nameValuePairs 参数信息
     * @param context 上下文
     * @return 响应数据
     */
    public static JSONObject getResponseForPost(String market_uri, List<NameValuePair> nameValuePairs, Context context) {
        if (isConnnected(context)) {
            return getResponseForPost(market_uri, nameValuePairs);
        }
        return null;
    }

    /**
     * 通过post方式向服务器发送请求，并返回响应数据
     *
     * @param market_uri 请求网址
     * @param nameValuePairs 参数信息
     * @return 响应数据
     */
    public static JSONObject getResponseForPost(String market_uri, List<NameValuePair> nameValuePairs) {
        if (null == market_uri || "" == market_uri) {
            return null;
        }


        HttpPost request = new HttpPost(market_uri);
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            return getRespose(request);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 响应客户端请求
     *
     * @param request 客户端请求get/post
     * @return 响应数据
     */
    public static JSONObject getRespose(HttpUriRequest request) {
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                Log.i(TAG, "results=" + result);
                return new JSONObject(result);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}