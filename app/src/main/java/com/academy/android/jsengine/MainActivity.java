package com.academy.android.jsengine;

import com.eclipsesource.v8.V8;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    final String javaScriptCode = "var recursive = function(n) {\n"
            + "    if(n <= 2) {\n"
            + "        return 1;\n"
            + "    } else {\n"
            + "        return this.recursive(n - 1) + this.recursive(n - 2);\n"
            + "    }\n"
            + "};"
            + "recursive(35)";


    private long mStartTime;

    private V8 mRuntime;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRuntime = V8.createV8Runtime();
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
        int value = mRuntime.executeIntegerScript(javaScriptCode);
        Log.d(TAG, "onReceiveValue: " + value);
        long executionTime = System.currentTimeMillis() - mStartTime;
        ((TextView) findViewById(R.id.tv_result))
                .setText("Time took: " + String.valueOf(executionTime) + " ms");
    }

}
