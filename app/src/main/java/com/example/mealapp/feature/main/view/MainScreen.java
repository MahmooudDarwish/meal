package com.example.mealapp.feature.main.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends AppCompatActivity {

    private RelativeLayout bannerNoInternet;
    private BroadcastReceiver networkReceiver;
    private NavController navController;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


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

    private void checkInternetConnection() {
        if (NetworkUtil.isConnected(this)) {
            bannerNoInternet.setVisibility(View.GONE);
        } else {
            bannerNoInternet.setVisibility(View.VISIBLE);
        }
    }
}
