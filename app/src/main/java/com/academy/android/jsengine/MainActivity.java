package com.academy.android.jsengine;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ValueCallback<String> {

    private static final String TAG = MainActivity.class.getSimpleName();

    final String javaScriptCode = "var recursive = function(n) {\n"
            + "    if(n <= 2) {\n"
            + "        return 1;\n"
            + "    } else {\n"
            + "        return this.recursive(n - 1) + this.recursive(n - 2);\n"
            + "    }\n"
            + "};"
            + "recursive(43)";
    private WebView mWebView;

    private long mStartTime;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.evaluateJavascript(javaScriptCode,this);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateJavaScript();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void evaluateJavaScript() {
        mStartTime = System.currentTimeMillis();
        mWebView.evaluateJavascript(javaScriptCode,this);
    }

    @Override
    public void onReceiveValue(String value) {
        Log.d(TAG, "onReceiveValue: "+value);
        if(mStartTime>0) {
            ((TextView) findViewById(R.id.tv_result))
                    .setText(
                            "Time took: " + String.valueOf(System.currentTimeMillis() - mStartTime)+" ms");
        }
    }
}
