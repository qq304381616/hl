package com.hl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * WebView
 */
public class WebActivity extends AppCompatActivity {

    private WebView mWebView;

    //    private static final String URL = "http://www.qq.com/";
    private static final String URL = "file:///android_asset/test.html";
//    private static final String URL = "file:///mnt/sdcard/9/test.html";
//    private static final String URL = "content://com.android.htmlfileprovider/sdcard/9/test.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        setContentView(mWebView);
        setTitle("Web");

        load();
    }

    private void load() {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级

        // 设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局

        // LOAD_DEFAULTCACHE_ONLY: 不使用网络，只读取本地缓存数据
        // LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存

        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式*/

        newWin(webSettings);
        saveData(webSettings);

        mWebView.requestFocusFromTouch(); //支持获取手势焦点，输入用户名、密码或其他
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.addJavascriptInterface(new JsObject(), "jsObj");

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });

        mWebView.loadUrl(URL);

    }

    /**
     * Http和Https混合问题
     */
    private void httpAndHttps() {
        // MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
        // MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
        // MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
        // 在5.0以下 Android 默认是 全允许，
        // 但是到了5.0以上，就是不允许，实际情况下很我们很难确定所有的网页都是https的，所以就需要这一步的操作。
        // 在Android 5.0上 Webview 默认不允许加载 Http 与 Https 混合内容：
        // 解决办法：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * webView 一些控制方法
     */
    private void control() {
        mWebView.goBack();//后退
        mWebView.goForward();//前进
        mWebView.goBackOrForward(1); //以当前的index为起始点前进或者后退到历史记录中指定的steps，如果steps为负数则为后退，正数则为前进

        mWebView.canGoForward();//是否可以前进
        mWebView.canGoBack(); //是否可以后退

        mWebView.clearCache(true);//清除网页访问留下的缓存，由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        mWebView.clearHistory();//清除当前webview访问的历史记录，只会webview访问历史记录里的所有记录除了当前访问记录.
        mWebView.clearFormData();//这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据。

        mWebView.onResume(); //激活WebView为活跃状态，能正常执行网页的响应
        mWebView.onPause();//当页面被失去焦点被切换到后台不可见状态，需要执行onPause动过， onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。

        mWebView.pauseTimers();//当应用程序被切换到后台我们使用了webview， 这个方法不仅仅针对当前的webview而是全局的全应用程序的webview，它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        mWebView.resumeTimers();//恢复pauseTimers时的动作。

        mWebView.destroy();//销毁，关闭了Activity时，音乐或视频，还在播放。就必须销毁。

        if (mWebView.getContentHeight() * mWebView.getScale() == (mWebView.getHeight() + mWebView.getScrollY())) {
            //已经处于底端
        }

        if (mWebView.getScrollY() == 0) {
            //处于顶端
        }
    }

    /**
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * HTML5数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        mWebSettings.setDatabaseEnabled(true); //开启 database storage API 功能
        mWebSettings.setAppCacheEnabled(true); //开启 Application Caches 功能
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath); //设置  Application Caches 缓存目录
    }

    /**
     * js 回调
     */
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

        @JavascriptInterface
        public void vibration(final String callback) {
            Toast.makeText(WebActivity.this, "vibration", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mWebView.loadUrl("javascript: " + callback + "()");
                }
            });
        }
    }

    /**
     * WebViewClient就是帮助WebView处理各种通知、请求事件的。
     * 打开网页时不调用系统浏览器， 而是在本WebView中显示：
     */
    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
            // 比如获取url，查看url.contains(“add”)，进行添加操作
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
            //重写此方法才能够处理在浏览器中的按键事件。
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //这个事件就是开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //在页面加载结束时调用。同样道理，我们可以关闭loading 条，切换程序动作。
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            // 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
            // 拦截替换网络请求数据,  从API 21开始引入
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
            // 拦截替换网络请求数据,  API 11开始引入，API 21弃用
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            // (报告错误信息)
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            //(更新历史记录)
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);
            //(应用程序重新请求网页数据)
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            //（获取返回信息授权请求）
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            //重写此方法可以让webview处理https请求。
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            // (WebView发生改变时调用)
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
            //（Key事件未被加载时调用）
        }
    };

    /**
     * WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等 :
     */
    WebChromeClient mWebChromeClient = new WebChromeClient() {

        //获得网页的加载进度，显示在右上角的TextView控件中
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress < 100) {
                String progress = newProgress + "%";
            } else {
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        //处理alert弹出框
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        //处理confirm弹出框
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        //处理prompt弹出框
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers(); //小心这个！！！暂停整个 WebView 所有布局、解析、JS。
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * cookie相关
     * 从服务器的返回头中取出 cookie 根据Http请求的客户端不同，获取 cookie 的方式也不同，请自行获取。
     * Cookie存入该应用程序data / data / package_name / app_WebView / Cookies.db
     */
    static class CookieUtils {

        /**
         * 将cookie设置到 WebView
         *
         * @param url    要加载的 url
         * @param cookie 要同步的 cookie
         */
        public static void syncCookie(Context context, String url, String cookie) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(context);
            }
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        }

        /**
         * 获取指定 url 的cookie
         */
        public static String syncCookie(String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            return cookieManager.getCookie(url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void removeCookie(Context context) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    // 清除结果
                }
            });
        }

        /**
         * 清除Cookie:
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void clear() {
            // 这个两个在 API level 21 被抛弃
            CookieManager.getInstance().removeSessionCookie();
            CookieManager.getInstance().removeAllCookie();
            // 推荐使用这两个， level 21 新加的
            CookieManager.getInstance().removeSessionCookies(null);// 移除所有过期 cookie
            CookieManager.getInstance().removeAllCookies(null); // 移除所有的 cookie
        }
    }

}
