package cn.scau.scautreasure.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.scau.scautreasure.R;

/**
 * Created by stcdasqy on 2014-08-16.
 */
public class SchoolActivityContentWebView extends WebView {
    private Context mContext;
    final static Pattern domain = Pattern.compile("^\\w+(\\.\\w+){1,9}");

    public SchoolActivityContentWebView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SchoolActivityContentWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    void init() {
        getSettings().setAllowFileAccess(true);
        //wv.getSettings().setAppCachePath(getFilesDir()+"/appCache");
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                 /*其实可以不用添加该Category*/
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setData(Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    int e_position = url.indexOf("files/");
                    if (e_position != -1)
                        url = url.substring(e_position + 6);
                    Matcher matcher = domain.matcher(url);
                    if (matcher.matches()) {
                        intent.setData(Uri.parse("http://" + url));
                        try {
                            mContext.startActivity(intent);
                        } catch (Exception e1) {
			        		 /*
			        		  * Something unexpected happened!
			        		  */
                            Toast.makeText(mContext, url, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        final Dialog d = new Dialog(mContext);
                        d.setTitle("oh!");
                        LayoutInflater inflater = (LayoutInflater)
                                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = inflater.inflate(R.layout.webview_wrong_tips, null);
                        TextView t = (TextView) v.findViewById(R.id.tips);
                        t.setText("It seems that you meet an irresponsible editor.So you click something that" +
                                " programs can't help you to handle it.");
                        EditText et = (EditText) v.findViewById(R.id.msg);
                        et.setText(url);
                        Button ok = (Button) v.findViewById(R.id.ok);
                        ok.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        d.setContentView(v);
                        d.show();
                    }

                }
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function _f(){\n" +
                        "if(iwant._getWidth()==0){\n" +
                        " setTimeout(_f,20);}\n" +
                        "else{\n" +
                        "var _img = document.getElementsByTagName('img');\n" +
                        "var _width = iwant._getWidth();\n" +
                        "var _margin = 12;\n" +
                        "document.body.style.padding= _margin;\n" +
                        /*"for(var i=0;i<_img.length;i++){\n" +
                        "\t_img[i].style.marginTop= _margin;\n" +
                        "\t_img[i].style.marginBottom= _margin;\n" +
                        "\t_img[i].width = _width - _margin*2;\n" +
                        "}\n" +*/
                        "document.body.style.display=\"\";\n" +
                        "}\n" +
                        "})();");
            }

        });
        getSettings().setJavaScriptEnabled(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        addJavascriptInterface(new iWantToGetWidth(), "iwant");
    }

    class iWantToGetWidth {
        @JavascriptInterface
        public int _getWidth() {
            return getWidth();
        }
    }

    public void setContent(String content, String filename) {
        //String html = "<a href=\":::::mailto:www.baidu.com\">英国政府于2006年推出一项国家儿童测量计划（NCMP），是一项针对在校儿童少年身体健康检测战略，分别在4岁至5岁10岁到和11岁测定并记录孩子们的身高和体重，并以现公认的BMI标准来划定健康、超重和肥胖标准。</a>" +
        //       "<img src=\"http://www.iconpng.com/png/social_media_ifa/qq.png\" />";
        String html = content;
        html = "<body>" + html + "</body>";
        html = "<head><meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\" /></head>"
                + html;
        try {
            FileInputStream fis = mContext.openFileInput(filename);
            fis.close();
        } catch (Exception e) {
            try {
                FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_WORLD_READABLE);
                fos.write(html.getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Log.d("============html===========",html);
        loadUrl("file://" + mContext.getFilesDir() + "/" + filename);
    }
}
