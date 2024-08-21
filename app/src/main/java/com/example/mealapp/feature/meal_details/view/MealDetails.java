package com.example.mealapp.feature.meal_details.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.meal_details.presenter.IMealDetailsPresenter;
import com.example.mealapp.feature.meal_details.presenter.MealDetailsPresenter;
import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MealDetails extends AppCompatActivity implements IMealDetails {

    private RecyclerView ingredientRecyclerView;
    private FloatingActionButton favoriteBtn;
    private Button addToPlanBtn;
    private ImageView mealImage;
    private TextView mealName;
    private TextView mealCountry;
    private YouTubePlayerView youtubePlayerView;
    private RelativeLayout bannerNoInternet;
    private TextView mealInstructions;
    private BroadcastReceiver networkReceiver;


    private DetailedMeal meal;
    private IMealDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_details);

        initUI();
        setUpListeners();

        presenter = new MealDetailsPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(this)));

        if (getIntent() != null && getIntent().hasExtra("MEAL_ID")) {
            String mealId = getIntent().getStringExtra("MEAL_ID");
            presenter.getMealDetails(mealId);
        } else if (getIntent() != null && getIntent().hasExtra("FAVORITE_MEAL_ID")) {
            String mealId = getIntent().getStringExtra("FAVORITE_MEAL_ID");
            presenter.getFavoriteMeal(mealId);
        } else if (getIntent() != null && getIntent().hasExtra("PLAN_MEAL_ID")) {
            String mealId = getIntent().getStringExtra("PLAN_MEAL_ID");
            presenter.getMealPlan(mealId);
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
            if (UserSessionHolder.isGuest()) {
                SignIn signInFragment = new SignIn();
                signInFragment.show(getSupportFragmentManager(), "signInFragment");
            } else if (meal != null) {
                presenter.toggleFavoriteStatus(meal);
            }
        });

        addToPlanBtn.setOnClickListener(v -> {
            if (UserSessionHolder.isGuest()) {
                SignIn signInFragment = new SignIn();
                signInFragment.show(getSupportFragmentManager(), "signInFragment");
            } else if (meal != null) {
                if (addToPlanBtn.getText() == getString(R.string.add_to_plan)) {
                    showAddToPlannerDialog();
                } else {
                    presenter.toggleMealPlan(meal);
                }
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
        youtubePlayerView = findViewById(R.id.mealVideo);
        addToPlanBtn = findViewById(R.id.addToPlanBtn);

        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setUpMealDetails(DetailedMeal meal, boolean isFavorite, boolean isPlan) {
        runOnUiThread(() -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }

            this.meal = meal;
            updateFavoriteIcon(isFavorite);
            updateAddPlanBtnText(isPlan);

            if (!isFinishing() && !isDestroyed()) {
                Glide.with(this).load(meal.getStrMealThumb()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mealImage);
            }

            mealName.setText(meal.getStrMeal());
            mealCountry.setText(meal.getStrArea());
            IngredientAdapter ingredientAdapter = new IngredientAdapter(meal.getIngredients());
            ingredientRecyclerView.setAdapter(ingredientAdapter);
            mealInstructions.setText(meal.getStrInstructions());

            if (!isFinishing() && !isDestroyed()) {
                youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String youtubeUrl = meal.getStrYoutube();
                        Log.i("MealDetails", "youtubeUrl: " + youtubeUrl);
                        if (youtubeUrl != null) {
                            String videoId = extractVideoId(youtubeUrl);
                            Log.i("MealDetails", "urlId: " + videoId);
                            if (videoId != null) {
                                youTubePlayer.cueVideo(videoId, 0);
                            } else {
                                Log.e("MealDetails", "Invalid YouTube URL: " + youtubeUrl);
                            }
                        } else {
                            Log.e("MealDetails", "YouTube URL is null");
                        }
                    }
                });
            }
        });
    }

    private String extractVideoId(String url) {
        Uri uri = Uri.parse(url);
        String videoId = null;

        if (uri.getQueryParameter("v") != null) {
            videoId = uri.getQueryParameter("v");
        }
        return videoId;
    }

    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFavoriteIcon(boolean isFavorite) {

        if (isFavorite) {
            favoriteBtn.setImageResource(R.drawable.filled_heart);

        } else {
            favoriteBtn.setImageResource(R.drawable.empty_heart);
        }
    }

    @Override
    public void updateAddPlanBtnText(boolean isPlan) {
        if (isPlan) {
            addToPlanBtn.setText(R.string.remove_from_plan);
        } else {
            addToPlanBtn.setText(R.string.add_to_plan);
        }
    }

    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    private void showAddToPlannerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.popup_food_planner, null);
        builder.setView(dialogView);

        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        RadioGroup mealTypeGroup = dialogView.findViewById(R.id.mealTypeGroup);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            int selectedMealTypeId = mealTypeGroup.getCheckedRadioButtonId();
            RadioButton selectedMealType = dialogView.findViewById(selectedMealTypeId);

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            String formattedDate = formatDate(day, month, year);
            String mealType = selectedMealType.getText().toString();

            addMealToPlanner(formattedDate, mealType);
            dialog.dismiss();
        });
        dialog.show();
    }

    private String formatDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    private void addMealToPlanner(String date, String mealType) {
        meal.setMealType(mealType);
        meal.setMealDate(date);
        presenter.toggleMealPlan(meal);
    }

}
