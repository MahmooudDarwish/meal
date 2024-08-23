package com.example.mealapp.feature.meal_details.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.example.mealapp.utils.shared_preferences.SharedPreferencesManager;
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

    private TextView countryText;
    private TextView ingredientsText;
    private TextView instructionsText;
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

        presenter = new MealDetailsPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(this), SharedPreferencesManager.getInstance(this)));

        if (getIntent() != null && getIntent().hasExtra(ConstantKeys.MEAL_ID)) {
            String mealId = getIntent().getStringExtra(ConstantKeys.MEAL_ID);
            presenter.getMealDetails(mealId);
        } else if (getIntent() != null && getIntent().hasExtra(ConstantKeys.FAVORITE_MEAL_ID)) {
            String mealId = getIntent().getStringExtra(ConstantKeys.FAVORITE_MEAL_ID);
            presenter.getFavoriteMeal(mealId);
        }else if(getIntent() != null && getIntent().hasExtra(ConstantKeys.PLAN_MEAL_ID)){
            String mealId = getIntent().getStringExtra(ConstantKeys.PLAN_MEAL_ID);
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
        if (NetworkUtil.isConnected()) {
            bannerNoInternet.setVisibility(View.GONE);
            youtubePlayerView.setVisibility(View.VISIBLE);
        } else {
            bannerNoInternet.setVisibility(View.VISIBLE);
            youtubePlayerView.setVisibility(View.GONE);


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
                    showAddToPlannerDialog();
            }

        });
    }

    private void initUI() {
        countryText = findViewById(R.id.countryText);
        ingredientsText = findViewById(R.id.ingredientsText);
        instructionsText = findViewById(R.id.instructionsText);
        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealCountry = findViewById(R.id.mealCountry);
        ingredientRecyclerView = findViewById(R.id.ingredientRecycler);
        mealInstructions = findViewById(R.id.mealInstructions);
        favoriteBtn = findViewById(R.id.favoriteBtn);
        youtubePlayerView = findViewById(R.id.mealVideo);
        addToPlanBtn = findViewById(R.id.addToPlanBtn);

        ingredientRecyclerView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setUpMealDetails(DetailedMeal meal, boolean isFavorite, boolean isPlan) {
        runOnUiThread(() -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }

            this.meal = meal;
            updateFavoriteIcon(isFavorite);

            if (!isFinishing() && !isDestroyed()) {
                Glide.with(this)
                        .load(meal.getStrMealThumb())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mealImage);
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
                        if (youtubeUrl != null) {
                            String videoId = extractVideoId(youtubeUrl);
                            if (videoId != null) {
                                youTubePlayer.cueVideo(videoId, 0);
                            }
                        }
                    }
                });
            }
        });
        setUpAnimation();

    }

    void setUpAnimation(){
        Animation fadeIn = AnimationUtils.loadAnimation(mealImage.getContext(), R.anim.fade_in);
        Animation slideOutLeft = AnimationUtils.loadAnimation(mealImage.getContext(), R.anim.slide_in_left);
        Animation slideOutBottom = AnimationUtils.loadAnimation(mealName.getContext(), R.anim.slide_in_bottom);
        Animation slideOutTop = AnimationUtils.loadAnimation(mealName.getContext(), R.anim.slide_in_top);
        Animation slideOutRight = AnimationUtils.loadAnimation(mealName.getContext(), R.anim.slide_in_right);

        mealImage.startAnimation(slideOutLeft);
        mealName.startAnimation(slideOutBottom);
        favoriteBtn.startAnimation(slideOutTop);
        addToPlanBtn.startAnimation(slideOutLeft);
        mealCountry.startAnimation(slideOutRight);
        mealInstructions.startAnimation(slideOutRight);
        countryText.startAnimation(fadeIn);
        ingredientsText.startAnimation(fadeIn);
        instructionsText.startAnimation(fadeIn);
        youtubePlayerView.startAnimation(fadeIn);
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
    public String getStringFromRes(int resId) {
        return getString(resId);
    }

    @Override
    public void showWarningMealPlanExist(DetailedMeal meal) {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(R.string.meal_already_exist)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    presenter.saveMealPlan(meal);
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show());
    }

    @Override
    public void showWarningMealCannotBeAdded() {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(R.string.cannot_add_meal)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show());
    }
    @Override
    public void showToast(String msg){
      runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    private void showAddToPlannerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.popup_food_planner, null);
        builder.setView(dialogView);

        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        RadioGroup mealTypeGroup = dialogView.findViewById(R.id.mealTypeGroup);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        long currentTimeMillis = System.currentTimeMillis();

        datePicker.setMinDate(currentTimeMillis);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        long maxDateMillis = calendar.getTimeInMillis();

        datePicker.setMaxDate(maxDateMillis);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            int selectedMealTypeId = mealTypeGroup.getCheckedRadioButtonId();
            if (selectedMealTypeId == -1) {
                Toast.makeText(this, getString(R.string.please_select_meal_type), Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedMealType = dialogView.findViewById(selectedMealTypeId);

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                String formattedDate = formatDate(day, month, year);
                String mealType = selectedMealType.getText().toString();

                checkMealExistence(formattedDate, mealType);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String formatDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }

    private void checkMealExistence(String date, String mealType) {
        meal.setMealType(mealType);
        meal.setMealDate(date);
        presenter.checkPlanExist(meal);
    }

}
