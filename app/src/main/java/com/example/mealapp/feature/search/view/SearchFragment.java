package com.example.mealapp.feature.search.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mealapp.R;
import com.example.mealapp.feature.meals_viewer.view.MealsViewer;
import com.example.mealapp.feature.search.presenter.ISearchPresenter;
import com.example.mealapp.feature.search.presenter.SearchPresenter;
import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements ISearch, OnCategoryClickedListener, OnCountryClickedListener, LoadMoreListener {

    RecyclerView categoriesRecycler;
    RecyclerView countriesRecycler;
    RecyclerView ingredientsRecycler;

    CountriesAdapter countriesAdapter;
    CategoriesAdapter categoriesAdapter;
    IngredientAdapter ingredientsAdapter;

    BroadcastReceiver networkReceiver;

    EditText searchEditText;

    SwipeRefreshLayout swipeRefreshLayout;

    ISearchPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                if (NetworkUtil.isConnected(requireContext())) {
                    swipeRefreshLayout.setEnabled(true);
                    refreshUI();
                } else {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        Objects.requireNonNull(requireActivity()).registerReceiver(networkReceiver, filter);

        refreshUI();
    }

    private void filterData(String query) {




        if (ingredientsAdapter != null) {
            ingredientsAdapter.getFilter().filter(query);
        }
    }

            private void setUpListeners() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::refreshUI);

        ingredientsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    presenter.loadMoreIngredients();
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

        searchEditText = v.findViewById(R.id.searchEditText);
        categoriesRecycler = v.findViewById(R.id.categoriesRecycler);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ingredientsRecycler = v.findViewById(R.id.ingeridentsRecycler);
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        ingredientsAdapter = new IngredientAdapter(this);
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
        Log.i("Search", "addMoreIngredients" + ingredients.get(0).getStrIngredient());
        if (!ingredientsRecycler.isComputingLayout())
        {
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
    public void onLoadMore() {
        presenter.loadMoreIngredients();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(requireActivity()).unregisterReceiver(networkReceiver);
    }

}
