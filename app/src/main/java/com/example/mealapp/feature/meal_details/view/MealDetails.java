package com.example.mealapp.feature.meal_details.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.meal_details.presenter.IMealDetailsPresenter;
import com.example.mealapp.feature.meal_details.presenter.MealDetailsPresenter;
import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MealDetails extends AppCompatActivity implements IMealDetails{

    private RecyclerView ingredientRecyclerView;

    private FloatingActionButton favoriteBtn;
    private ImageView mealImage;
    private TextView mealName;
    private TextView mealCountry;
    YouTubePlayerView youtubePlayerView;
    private RelativeLayout bannerNoInternet;

    private TextView mealInstructions;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_details);

        initUI();
        setUpListeners();

        IMealDetailsPresenter presenter = new MealDetailsPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance()));

        if (getIntent() != null && getIntent().hasExtra("MEAL_ID")) {
            String mealId = getIntent().getStringExtra("MEAL_ID");
            presenter.getMealDetails(mealId);
        }

        bannerNoInternet = findViewById(R.id.bannerNoInternet);

        checkInternetConnection();
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkInternetConnection();
            }
        };

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

    private void setUpListeners() {
        favoriteBtn.setOnClickListener(v -> {
            if(UserSessionHolder.isGuest()){
                SignIn signInFragment = new SignIn();
                signInFragment.show(getSupportFragmentManager(), "signInFragment");

            }else{
                Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealCountry = findViewById(R.id.mealCountry);
        ingredientRecyclerView = findViewById(R.id.ingredientRecycler);
        mealInstructions = findViewById(R.id.mealInstructions);
        favoriteBtn = findViewById(R.id.favoriteBtn);

        ingredientRecyclerView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false));
        youtubePlayerView = findViewById(R.id.mealVideo);
    }

    @Override
    public void setUpMealDetails(DetailedMeal meal) {
        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);
        mealName.setText(meal.getStrMeal());
        mealCountry.setText(meal.getStrArea());
        IngredientAdapter ingredientAdapter = new IngredientAdapter(meal.getIngredients());
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        mealInstructions.setText(meal.getStrInstructions());

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = meal.getStrYoutube().split("=")[1];
                youTubePlayer.cueVideo(videoId , 0);
            }
        });
    }

    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}