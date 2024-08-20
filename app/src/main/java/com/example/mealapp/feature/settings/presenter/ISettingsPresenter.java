package com.example.mealapp.feature.settings.presenter;

import androidx.lifecycle.LifecycleOwner;

public interface ISettingsPresenter {
    void signOut();
     void uploadDataToFirebase(LifecycleOwner owner);

}
