package com.example.mealapp.feature.splash.presenter;

import android.os.Handler;

import com.example.mealapp.feature.splash.view.ISplash;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.constants.ConstantKeys;

import java.util.Locale;

public class SplashPresenter implements ISplashPresenter {

    private final ISplash view;
    private final MealRepository repo;

    public SplashPresenter(ISplash view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void start() {
        checkLanguageSetting();
        new Handler().postDelayed(view::navigateToHome, 3000);
    }

    @Override
    public String getCurrentLang(){
        return repo.getPreference(ConstantKeys.LANGUAGE_KEY, false);
    }
    private void checkLanguageSetting() {
        String savedLanguage = repo.getPreference(ConstantKeys.LANGUAGE_KEY, false);
        if (savedLanguage != null) {
            view.setLocale(savedLanguage);
        } else {
            String currentLanguage = Locale.getDefault().getLanguage();
            view.setLocale(currentLanguage);
            repo.savePreference(ConstantKeys.LANGUAGE_KEY, currentLanguage, false);
        }
    }

}
