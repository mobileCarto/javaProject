package com.tudresden.mobilecartoapp;

import android.content.Intent;
import android.graphics.Color;
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
        firstSliderPage.setTitle("\n \n WELCOME \n TO \n TRACE");
        firstSliderPage.setDescription("this is slide 1");
        firstSliderPage.setImageDrawable(R.drawable.logo_light);
        firstSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.slide1));
        addSlide(AppIntroFragment.newInstance(firstSliderPage));

        SliderPage secondSliderPage = new SliderPage();
        secondSliderPage.setTitle("wow");
        secondSliderPage.setDescription("To begin, \n open the app");
        secondSliderPage.setImageDrawable(R.drawable.globe2);
        secondSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.slide2));
        addSlide(AppIntroFragment.newInstance(secondSliderPage));

        SliderPage thirdSliderPage = new SliderPage();
        thirdSliderPage.setTitle("Heatmap");
        thirdSliderPage.setDescription("this is slide 3");
        thirdSliderPage.setImageDrawable(R.drawable.map2);
        thirdSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.slide3));
        addSlide(AppIntroFragment.newInstance(thirdSliderPage));

        SliderPage forthSliderPage = new SliderPage();
        forthSliderPage.setTitle("TRACE");
        forthSliderPage.setDescription("GET STARTED");
        forthSliderPage.setImageDrawable(R.drawable.plane2);
        forthSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.slide4));
        addSlide(AppIntroFragment.newInstance(forthSliderPage));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#404040"));
        setSeparatorColor(Color.parseColor("#606060"));

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
