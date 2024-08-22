package com.example.mealapp.feature.settings.view;

public interface ISettings {
    void showMessage(String msg);

    void showBackUpWarning();


    void showLoading();
    void hideLoading();

    String getStringFromRes(int resId);
}
