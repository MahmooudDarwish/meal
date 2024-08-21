package com.example.mealapp.feature.search.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealapp.R;
import com.example.mealapp.feature.meals_viewer.view.MealsViewer;
import com.example.mealapp.feature.search.presenter.ISearchPresenter;
import com.example.mealapp.feature.search.presenter.SearchPresenter;
import com.example.mealapp.feature.search.model.Category;
import com.example.mealapp.feature.search.model.Country;
import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

import java.util.List;

public class SearchFragment extends Fragment implements ISearch, OnCategoryClickedListener, OnCountryClickedListener, LoadMoreListener, OnIngredientClickedListener {

    RecyclerView categoriesRecycler;
    RecyclerView countriesRecycler;
    RecyclerView ingredientsRecycler;

    CountriesAdapter countriesAdapter;
    CategoriesAdapter categoriesAdapter;
    IngredientAdapter ingredientsAdapter;

    BroadcastReceiver networkReceiver;

    SearchView searchView;

    SwipeRefreshLayout swipeRefreshLayout;

    ISearchPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        initUI(view);
        setUpListeners();

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (NetworkUtil.isConnected()) {
                    swipeRefreshLayout.setEnabled(true);
                    refreshUI();
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }


    private void setUpListeners() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.search(newText);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::refreshUI);

        ingredientsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (gridLayoutManager != null) {
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();

                    if (lastVisibleItemPosition >= totalItemCount - 1) {
                        presenter.loadMoreIngredients();
                    }
                }
            }
        });

    }

    private void refreshUI() {
        presenter.getAllCategories();
        presenter.getAllCountries();
        presenter.getAllIngredients();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initUI(View v) {

        searchView = v.findViewById(R.id.searchView);
        searchView.clearFocus();
        categoriesRecycler = v.findViewById(R.id.categoriesRecycler);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ingredientsRecycler = v.findViewById(R.id.ingeridentsRecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        ingredientsRecycler.setLayoutManager(gridLayoutManager);

        ingredientsAdapter = new IngredientAdapter(this, this);
        ingredientsRecycler.setAdapter(ingredientsAdapter);

        countriesRecycler = v.findViewById(R.id.countriesRecycler);
        countriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoriesAdapter = new CategoriesAdapter(categories, this);
        categoriesRecycler.setAdapter(categoriesAdapter);
    }

    @Override
    public void showCountries(List<Country> countries) {
        countriesAdapter = new CountriesAdapter(countries, this);
        countriesRecycler.setAdapter(countriesAdapter);
    }

    @Override
    public void addMoreIngredients(List<Ingredient> ingredients) {
        if (!ingredientsRecycler.isComputingLayout()) {
            ingredientsAdapter.addIngredients(ingredients);
        }
    }

    @Override
    public void countryClicked(List<PreviewMeal> meals) {
        Intent intent = new Intent(getContext(), MealsViewer.class);
        startActivity(intent);
    }

    @Override
    public void categoryClicked(List<PreviewMeal> meals) {
        Intent intent = new Intent(getContext(), MealsViewer.class);
        startActivity(intent);
    }

    @Override
    public void onCategoryClicked(String categoryName) {
        presenter.getMealsByCategory(categoryName);
    }

    @Override
    public void onCountryClicked(String countryName) {
        presenter.getMealsByCountry(countryName);
    }

    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFilteredContents(
            List<Ingredient> filteredIngredients,
            List<Country> filteredCountries,
            List<Category> filteredCategories) {
        if (!filteredIngredients.isEmpty()) {
            ingredientsAdapter.setIngredients(filteredIngredients);
        }
        if (!filteredCountries.isEmpty()) {
            countriesAdapter.setCountries(filteredCountries);
        }
        if (!filteredCategories.isEmpty()) {
            categoriesAdapter.setCategories(filteredCategories);
        }
    }

    @Override
    public void ingredientClicked(List<PreviewMeal> meals) {
        Intent intent = new Intent(getContext(), MealsViewer.class);
        startActivity(intent);

    }


    @Override
    public void onLoadMore() {
        presenter.loadMoreIngredients();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(networkReceiver);
    }

    @Override
    public void onIngredientClicked(String ingredient) {
        presenter.getMealsByIngredient(ingredient);
    }
}
