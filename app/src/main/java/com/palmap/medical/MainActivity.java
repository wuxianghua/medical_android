package com.palmap.medical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.palmap.exhibition.launcher.SdkLauncher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SdkLauncher.launcher(this,null,"测试医疗产品");
    }
}
