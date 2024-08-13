package com.example.mealapp.feature.meal_details.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.feature.meal_details.presenter.IMealDetailsPresenter;
import com.example.mealapp.feature.meal_details.presenter.MealDetailsPresenter;
import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MealDetails extends AppCompatActivity implements IMealDetails{

    private RecyclerView ingredientRecyclerView;
    private ImageView mealImage;
    private TextView mealName;
    private TextView mealCountry;
    YouTubePlayerView youtubePlayerView;

    private TextView mealInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_details);

        initUI();

        IMealDetailsPresenter presenter = new MealDetailsPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance()));

        if (getIntent() != null && getIntent().hasExtra("MEAL_ID")) {
            String mealId = getIntent().getStringExtra("MEAL_ID");
            presenter.getMealDetails(mealId);
        }

    }

    private void initUI() {
        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealCountry = findViewById(R.id.mealCountry);
        ingredientRecyclerView = findViewById(R.id.ingredientRecycler);
        mealInstructions = findViewById(R.id.mealInstructions);

        ingredientRecyclerView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false));
        youtubePlayerView = findViewById(R.id.mealVideo);
        getLifecycle().addObserver(youtubePlayerView);

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