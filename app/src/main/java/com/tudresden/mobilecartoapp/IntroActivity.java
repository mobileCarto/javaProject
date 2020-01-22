package com.tudresden.mobilecartoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(firstFragment);
        //addSlide(secondFragment);
        //addSlide(thirdFragment);
        //addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
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

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

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
