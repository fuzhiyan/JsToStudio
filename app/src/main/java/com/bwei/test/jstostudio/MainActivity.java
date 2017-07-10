package com.bwei.test.jstostudio;

import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import android.widget.Toast;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
//webView_main_loaddetail

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.forum_context);
        WebSettings web = webView.getSettings();
        web.setJavaScriptEnabled(true);
        //http https混合解决版本不显示图片问题
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        String as = web.getUserAgentString();
        web.setUserAgentString(as + " bwei");
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("login://") && url.contains("userlogin")) {
                    //true 不交给webView处理
                    //false webView处理url

                    Uri uri = Uri.parse(url);
                    String userName = uri.getQueryParameter("userName");
//                    Toast.makeText(this, userName, Toast.LENGTH_LONG).show();
                    showToast(userName);
                    nativeToJs();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);

            }
        });
        webView.addJavascriptInterface(new MyJavaScriptInterface(),"myApp");
        initData();
    }

    private void showToast(String userName) {
        Toast.makeText(this, userName, Toast.LENGTH_LONG).show();
    }

    private void initData() {
        String user = "fuzhiyan";
        webView.loadUrl("file:///android_asset/www/index.html" + "?username=" + user);
    }


    class MyJavaScriptInterface {
        //注入（4.4以上）
        @JavascriptInterface
        public void showToast(String values) {

            MainActivity.this.showToast(values);
        }
    }

    public void nativeToJs() {
        if (Build.VERSION.SDK_INT < 19) {
            webView.loadUrl("javascript:onNativeToJs('from native')");
        } else {
            webView.evaluateJavascript("javascript:onNativeToJs('from native')", null);
        }

    }
}
