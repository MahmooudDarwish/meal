package com.example.mealapp.feature.main.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.main.presenter.MainPresenter;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.example.mealapp.utils.shared_preferences.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainScreen extends AppCompatActivity {

    private RelativeLayout bannerNoInternet;
    private BroadcastReceiver networkReceiver;
    private NavController navController;
    BottomNavigationView bottomNavigationView;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            String lang = savedInstanceState.getString(ConstantKeys.LANGUAGE_KEY);
            setLocale(lang);
        }
        presenter = new MainPresenter(
                MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(),
                        MealLocalDataSourceImpl.getInstance(this),
                        SharedPreferencesManager.getInstance(this)
                ));

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        setUpListeners();
        bannerNoInternet = findViewById(R.id.bannerNoInternet);
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkInternetConnection();
            }
        };
    }

    void setUpListeners() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.homeFragment) {
                navController.navigate(R.id.homeFragment);
            } else if (itemId == R.id.searchFragment) {
                navController.navigate(R.id.searchFragment);
            } else if (itemId == R.id.favouritesFragment) {
                navController.navigate(R.id.favouritesFragment);
                showLogin();
                Log.i("TAG", "favouritesFragment clicke" + "d");
            } else if (itemId == R.id.foodPlannedPreviewFragment) {
                navController.navigate(R.id.foodPlannedPreviewFragment);
                showLogin();
                Log.i("TAG", "foodPlannedPreviewFragment clicked");
            } else if (itemId == R.id.settingsFragment) {
                navController.navigate(R.id.settingsFragment);
                Log.i("TAG", "settingsFragment clicked");

            }

            return true;
        });
    }

    private void showLogin() {
        if (UserSessionHolder.isGuest()) {
            SignIn signInFragment = new SignIn();
            signInFragment.show(this.getSupportFragmentManager(), "signInFragment");
        }
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String lang = presenter.getCurrentLang();
        outState.putString(ConstantKeys.LANGUAGE_KEY, lang);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String lang = presenter.getCurrentLang();
        setLocale(lang);

        Log.i("TAG", "onConfigurationChanged: hooooome");
    }

    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void checkInternetConnection() {
        if (NetworkUtil.isConnected()) {
            bannerNoInternet.setVisibility(View.GONE);
        } else {
            bannerNoInternet.setVisibility(View.VISIBLE);
        }
    }
}
