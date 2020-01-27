package com.tudresden.mobilecartoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Slides
        SliderPage firstSliderPage = new SliderPage();
        firstSliderPage.setTitle("\n WELCOME \n TO \n TRACE");
        firstSliderPage.setDescription("TRACE your footsteps \n wherever you go");
        firstSliderPage.setImageDrawable(R.drawable.logo_tr);
        firstSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(firstSliderPage));

        SliderPage secondSliderPage = new SliderPage();
        secondSliderPage.setTitle("Let it TRACE you \n on the go");
        secondSliderPage.setDescription("To begin, open the app \n and it will start recording \n your current location");
        secondSliderPage.setImageDrawable(R.drawable.globe);
        secondSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(secondSliderPage));

        SliderPage thirdSliderPage = new SliderPage();
        thirdSliderPage.setTitle("Your personal \n Heat map");
        thirdSliderPage.setDescription("View all the places \n you have been to with TRACE \n as a density map");
        thirdSliderPage.setImageDrawable(R.drawable.map);
        thirdSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(thirdSliderPage));

        SliderPage forthSliderPage = new SliderPage();
        forthSliderPage.setTitle("LET'S GO");
        forthSliderPage.setDescription("GET STARTED");
        forthSliderPage.setImageDrawable(R.drawable.plane);
        forthSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(forthSliderPage));

        //Slide animation
        setZoomAnimation();

        // METHODS
        // Override bar/separator color.
        setBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        setSeparatorColor(ContextCompat.getColor(getApplicationContext(), R.color.light_gray));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
