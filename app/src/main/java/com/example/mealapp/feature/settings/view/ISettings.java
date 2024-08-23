package com.example.mealapp.feature.settings.view;

public interface ISettings {
    void showMessage(String msg);

    void showBackUpWarning();

    void setLocale(String lang);
    void setLanguage(boolean isEnglish);

    void showLoading();
    void hideLoading();
    void onSignOut();

        String getStringFromRes(int resId);
}
