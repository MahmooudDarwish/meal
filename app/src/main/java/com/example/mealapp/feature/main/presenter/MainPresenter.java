package com.example.mealapp.feature.main.presenter;

import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepository;

public class MainPresenter implements IMainPresenter {


    MealRepository repo;

    public MainPresenter(MealRepository repo){
        this.repo = repo;
    }
    @Override
    public String getCurrentLang(){
        return repo.getPreference(ConstantKeys.LANGUAGE_KEY, false);
    }

}
