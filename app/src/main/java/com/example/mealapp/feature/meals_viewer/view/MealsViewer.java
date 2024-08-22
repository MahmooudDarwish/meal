package com.example.mealapp.feature.meals_viewer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealapp.R;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.feature.meals_viewer.presenter.MealsViewerPresenter;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.constants.ConstantKeys;

import java.util.List;

public class MealsViewer extends AppCompatActivity implements IMealsViewer, OnMealItemClicked {

    private RecyclerView mealsRecycler;
    private MealsAdapter mealsAdapter;
    private MealsViewerPresenter presenter;
    private SearchView searchView;
    private RelativeLayout bannerNoInternet;
    private BroadcastReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meals_viewer);

        initUI();

        presenter = new MealsViewerPresenter(this);
        checkInternetConnection();

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

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkInternetConnection();
            }
        };
    }

    private void initUI() {
        mealsRecycler = findViewById(R.id.mealsRecycler);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mealsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }else{
            mealsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        }
        searchView = findViewById(R.id.searchView);
        bannerNoInternet = findViewById(R.id.bannerNoInternet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    private void checkInternetConnection() {
        if (NetworkUtil.isConnected()) {
            bannerNoInternet.setVisibility(View.GONE);
            presenter.loadMeals();
        } else {
            bannerNoInternet.setVisibility(View.VISIBLE);
        }
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
        intent.putExtra(ConstantKeys.MEAL_ID, mealId);
        startActivity(intent);
    }
}
