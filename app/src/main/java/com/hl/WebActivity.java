package com.hl;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HL on 2016/12/29.
 */

public class WebActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        setContentView(mWebView);
        setTitle("Web");

        load();
    }

    private void load() {
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        mWebView.addJavascriptInterface(new JsObject(), "jsObj");

//        mWebView.loadUrl("http://www.qq.com/");
        mWebView.loadUrl("file:///android_asset/test.html");
//        mWebView.loadUrl("file:///mnt/sdcard/9/test.html");


//        mWebView.loadUrl("content://com.android.htmlfileprovider/sdcard/9/test.html");

    }

    class JsObject {
        @JavascriptInterface
        public String HtmlcallJava() {
            Toast.makeText(WebActivity.this, "HtmlcallJava", Toast.LENGTH_SHORT).show();
            return "HtmlcallJava";
        }

        @JavascriptInterface
        public String HtmlcallJava2(final String param) {
            Toast.makeText(WebActivity.this, "HtmlcallJava2" + param, Toast.LENGTH_SHORT).show();
            return "Html call Java : " + param;
        }

        @JavascriptInterface
        public void JavacallHtml() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: showFromHtml()");
                    Toast.makeText(WebActivity.this, "clickBtn", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public void JavacallHtml2() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: showFromHtml2('IT-homer blog')");
                    Toast.makeText(WebActivity.this, "clickBtn2", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
