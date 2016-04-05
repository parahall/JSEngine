package com.academy.android.jsengine;

import com.eclipsesource.v8.V8;

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

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ValueCallback<String> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static int SEQUENCE_NUMBER = 28;

    private WebView mWebView;

    private long mStartTime;

    private V8 mRuntime;

    private int mTimesToExecute;

    private int currentExecution;

    private long[] mWebViewTimes;

    private long[] mNativeTimes;

    private long[] mV8Times;

    final String javaScriptCode = "var recursive = function(n) {\n"
            + "    if(n <= 2) {\n"
            + "        return 1;\n"
            + "    } else {\n"
            + "        return this.recursive(n - 1) + this.recursive(n - 2);\n"
            + "    }\n"
            + "};"
            + "recursive(" + SEQUENCE_NUMBER + ")";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = new WebView(this);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.evaluateJavascript(javaScriptCode, this);
        mRuntime = V8.createV8Runtime();
        mTimesToExecute = 1000;
        mWebViewTimes = new long[mTimesToExecute];
        mNativeTimes = new long[mTimesToExecute];
        mV8Times = new long[mTimesToExecute];

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentExecution = 0;
                evaluateJavaScript();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void evaluateJavaScript() {
        mStartTime = System.currentTimeMillis();
        mWebView.evaluateJavascript(javaScriptCode, this);
    }

    @Override
    public void onReceiveValue(String value) {
        Log.d(TAG, "onReceiveValue: " + value);
        long executionTime = System.currentTimeMillis() - mStartTime;
        mWebViewTimes[currentExecution] = executionTime;
        if (mStartTime > 0) {
            ((TextView) findViewById(R.id.tv_result))
                    .setText("Time took: " + String.valueOf(executionTime) + " ms");
        }

        Fibonacchi fibonacchi = new Fibonacchi();
        mStartTime = System.currentTimeMillis();
        value = "" + fibonacchi.calculate(SEQUENCE_NUMBER);
        Log.d(TAG, "V8 onReceiveValue: " + value);
        executionTime = System.currentTimeMillis() - mStartTime;
        mNativeTimes[currentExecution] = executionTime;

        ((TextView) findViewById(R.id.tv_native_result))
                .setText("Native Time took: " + String.valueOf(executionTime) + " ms");

        mStartTime = System.currentTimeMillis();
        value = "" + mRuntime.executeIntegerScript(javaScriptCode);
        Log.d(TAG, "V8 onReceiveValue: " + value);
        executionTime = System.currentTimeMillis() - mStartTime;
        ((TextView) findViewById(R.id.tv_v8_result))
                .setText("Time took: " + String.valueOf(executionTime) + " ms");
        mV8Times[currentExecution] = executionTime;
        currentExecution++;
        if (currentExecution < mTimesToExecute) {
            Log.d(TAG, "Current: " + currentExecution);
            evaluateJavaScript();
        } else {
            Log.d(TAG, "Finished");
            Log.d(TAG, "Webview: " + Arrays.toString(mWebViewTimes));
            Log.d(TAG, "Native: " + Arrays.toString(mNativeTimes));
            Log.d(TAG, "v8: " + Arrays.toString(mV8Times));
        }
    }

}
