package com.example.mealapp.feature.meals_viewer.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import com.example.mealapp.R;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.feature.meals_viewer.presenter.MealsViewerPresenter;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;

public class MealsViewer extends AppCompatActivity implements IMealsViewer, OnMealItemClicked {

    private RecyclerView mealsRecycler;
    private MealsAdapter mealsAdapter;
    private MealsViewerPresenter presenter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meals_viewer);

        initUI();

        presenter = new MealsViewerPresenter(this);
        presenter.loadMeals();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onSearchQuery(newText);
                return true;
            }
        });
    }

    private void initUI() {
        mealsRecycler = findViewById(R.id.mealsRecycler);
        mealsRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);
    }

    @Override
    public void displayMeals(List<PreviewMeal> meals) {
        if (mealsAdapter == null) {
            mealsAdapter = new MealsAdapter(meals, this);
            mealsRecycler.setAdapter(mealsAdapter);
        } else {
            mealsAdapter.updateMeals(meals);
        }
    }

    @Override
    public void onMealItemClick(String mealId) {
        Intent intent = new Intent(this, MealDetails.class);
        intent.putExtra("MEAL_ID", mealId);
        startActivity(intent);
    }
}
