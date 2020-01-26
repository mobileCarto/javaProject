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
        firstSliderPage.setTitle("TRACE");
        firstSliderPage.setDescription("this is slide 1");
        firstSliderPage.setImageDrawable(R.drawable.logo_light);
        firstSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.slide1));
        addSlide(AppIntroFragment.newInstance(firstSliderPage));

        SliderPage secondSliderPage = new SliderPage();
        secondSliderPage.setTitle("TRACE");
        secondSliderPage.setDescription("this is slide 2");
        secondSliderPage.setImageDrawable(R.drawable.logo_dark);
        secondSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.slide2));
        addSlide(AppIntroFragment.newInstance(secondSliderPage));

        SliderPage thirdSliderPage = new SliderPage();
        thirdSliderPage.setTitle("TRACE");
        thirdSliderPage.setDescription("this is slide 3");
        thirdSliderPage.setImageDrawable(R.drawable.logo_light);
        thirdSliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.slide3));
        addSlide(AppIntroFragment.newInstance(thirdSliderPage));

        // METHODS
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
