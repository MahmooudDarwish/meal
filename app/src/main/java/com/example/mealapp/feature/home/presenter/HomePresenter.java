package com.example.mealapp.feature.home.presenter;

import android.util.Log;

import com.example.mealapp.feature.home.view.IHome;
import com.example.mealapp.feature.meals_viewer.model.MealsViewModel;
import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.network.HomeNetworkDelegate;

import java.util.List;

public class HomePresenter implements IHomePresenter, HomeNetworkDelegate {

    private final IHome _view;
    private final MealRepository _repo;

    private static final String TAG = "HomePresenter";

    public HomePresenter(IHome view, MealRepository repo) {
        _view = view;
        _repo = repo;
    }
    @Override
    public void getRandomMeal() {
        _repo.getRandomMeal(this);
    }

    @Override
    public void getAllCategories() {
        _repo.getAllCategories(this);
    }

    @Override
    public void getAllCountries() {
        _repo.getAllCountries(this);
    }

    @Override
    public void getMealsByCategory(String category) {
        _repo.getMealsByCategory( this, category);

    }

    @Override
    public void getMealsByCountry(String country) {
        _repo.getMealsByCountry( this, country);
    }

    @Override
    public void onGetRandomMealSuccessResult(PreviewMeal previewMeal) {
        _view.showRandomMeal(previewMeal);

    }

    @Override
    public void onGetAllCategoriesSuccessResult(List<Category> categories) {
        Log.d(TAG, categories.toString());
        _view.showCategories(categories);
    }

    @Override
    public void onGetAllCountriesSuccessResult(List<Country> countries) {
        Log.d(TAG, countries.toString());
        _view.showCountries(countries);
    }

    @Override
    public void onGetAllMealsByCategorySuccessResult(List<PreviewMeal> meals) {
        MealsViewModel.getInstance().setMeals(meals);
        _view.categoryClicked(meals);

    }

    @Override
    public void onGetAllMealsByCountrySuccessResult(List<PreviewMeal> meals) {
        MealsViewModel.getInstance().setMeals(meals);
        _view.countryClicked(meals);
    }

    @Override
    public void onFailureResult(String errorMsg) {
        _view.onFailureResult(errorMsg);

    }
}
