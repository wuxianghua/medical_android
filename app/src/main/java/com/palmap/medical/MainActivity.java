package com.palmap.medical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.palmap.exhibition.launcher.SdkLauncher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launcherMap(View view) {
        SdkLauncher.launcher(this,null,"测试医疗产品");
    }
}
