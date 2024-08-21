package com.example.mealapp.feature.search.presenter;

import android.util.Log;

import com.example.mealapp.feature.meals_viewer.model.MealsViewModel;
import com.example.mealapp.feature.search.view.ISearch;
import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.network.SearchNetworkDelegate;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter, SearchNetworkDelegate {

    private final ISearch _view;
    private final MealRepository _repo;
    private final List<Ingredient> allIngredients = new ArrayList<>();
    private List<Country> allCountries = new ArrayList<>();
    private  List<Category> allCategories = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isLoading = false;

    private static final String TAG = "SearchPresenter";

    public SearchPresenter(ISearch view, MealRepository repo) {
        _view = view;
        _repo = repo;
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
        _repo.getMealsByCategory(this, category);
    }

    @Override
    public void getMealsByCountry(String country) {
        _repo.getMealsByCountry(this, country);
    }

    @Override
    public void search(String newText) {
        String searchLower = newText.toLowerCase();

        List<Ingredient> exactMatchIngredients = new ArrayList<>();
        List<Ingredient> partialMatchIngredients = new ArrayList<>();
        for (Ingredient ingredient : allIngredients) {
            String ingredientLower = ingredient.getStrIngredient().toLowerCase();
            if (ingredientLower.startsWith(searchLower)) {
                exactMatchIngredients.add(ingredient);
            } else if (ingredientLower.contains(searchLower)) {
                partialMatchIngredients.add(ingredient);
            }
        }
        List<Ingredient> filteredIngredients = new ArrayList<>(exactMatchIngredients);
        filteredIngredients.addAll(partialMatchIngredients);

        List<Country> exactMatchCountries = new ArrayList<>();
        List<Country> partialMatchCountries = new ArrayList<>();
        for (Country country : allCountries) {
            String countryLower = country.getStrArea().toLowerCase();
            if (countryLower.startsWith(searchLower)) {
                exactMatchCountries.add(country);
            } else if (countryLower.contains(searchLower)) {
                partialMatchCountries.add(country);
            }
        }
        List<Country> filteredCountries = new ArrayList<>(exactMatchCountries);
        filteredCountries.addAll(partialMatchCountries);

        List<Category> exactMatchCategories = new ArrayList<>();
        List<Category> partialMatchCategories = new ArrayList<>();
        for (Category category : allCategories) {
            String categoryLower = category.getStrCategory().toLowerCase();
            if (categoryLower.startsWith(searchLower)) {
                exactMatchCategories.add(category);
            } else if (categoryLower.contains(searchLower)) {
                partialMatchCategories.add(category);
            }
        }
        List<Category> filteredCategories = new ArrayList<>(exactMatchCategories);
        filteredCategories.addAll(partialMatchCategories);

        if (!filteredIngredients.isEmpty() || !filteredCountries.isEmpty() || !filteredCategories.isEmpty()) {
            _view.showFilteredContents(filteredIngredients, filteredCountries, filteredCategories);
        }
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        _repo.getMealsByIngredient(this, ingredient);
    }


    @Override
    public void onGetAllCategoriesSuccessResult(List<Category> categories) {
        Log.d(TAG, categories.toString());
        allCategories = categories;
        _view.showCategories(categories);
    }

    @Override
    public void onGetAllCountriesSuccessResult(List<Country> countries) {
        Log.d(TAG, countries.toString());
        allCountries = countries;
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
    public void onGetAllMealsByIngredientSuccessResult(List<PreviewMeal> meals) {
        MealsViewModel.getInstance().setMeals(meals);
        _view.ingredientClicked(meals);

    }

    @Override
    public void onGetAllIngredientsSuccessResult(List<Ingredient> ingredients) {
        allIngredients.clear();
        allIngredients.addAll(ingredients);
        currentIndex = 0;
        loadNextPage();
    }

    @Override
    public void getAllIngredients() {
        _repo.getAllIngredients(this);
    }

    @Override
    public void loadMoreIngredients() {
        if (!isLoading) {
            isLoading = true;
            loadNextPage();
        }
    }

    private void loadNextPage() {

        if (currentIndex < allIngredients.size()) {
            int PAGE_LIMIT = 20;
            int nextIndex = Math.min(currentIndex + PAGE_LIMIT, allIngredients.size());
            List<Ingredient> nextPage = allIngredients.subList(currentIndex, nextIndex);
            _view.addMoreIngredients(nextPage);
            currentIndex = nextIndex;
        }
        isLoading = false;
    }

    @Override
    public void onFailureResult(String errorMsg) {
        _view.onFailureResult(errorMsg);
    }
}
