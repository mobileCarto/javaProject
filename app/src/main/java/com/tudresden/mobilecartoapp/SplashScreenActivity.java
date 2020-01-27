package com.tudresden.mobilecartoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


//// Creates Splash while the app is loading to start/////
public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        startActivity(new Intent(SplashScreenActivity.this, IntroActivity.class));
        finish();
    }
}
