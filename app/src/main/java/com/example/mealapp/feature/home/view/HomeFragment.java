package com.example.mealapp.feature.home.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.home.presenter.HomePresenter;
import com.example.mealapp.feature.home.presenter.IHomePresenter;
import com.example.mealapp.feature.main.view.MainScreen;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

import java.util.Objects;

public class HomeFragment extends Fragment implements IHome, OnMealItemClicked {

    ImageView mealOfDayImage, signOutIcon;
    TextView mealOfDayTitle, userName;
    CardView mealOfDayCard;
    RecyclerView mealsRecycler;
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressBar mealOfDayProgressBar, mealsProgressBar;


    MealsAdapter mealsAdapter;

    private BroadcastReceiver networkReceiver;

    IHomePresenter presenter;

    private static final String TAG = "HomeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity())));
        getActivity();
        SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences(ConstantKeys.USER_DATA, Context.MODE_PRIVATE);
        boolean stayLoggedIn = sharedPreferences.getBoolean(ConstantKeys.STAY_LOGGED_IN, false);
        String userNameStr = sharedPreferences.getString(ConstantKeys.USER_NAME, "");
        String userEmail = sharedPreferences.getString(ConstantKeys.USER_EMAIL, "");

        if (stayLoggedIn) {
            UserSessionHolder.getInstance().setUser(new User(userEmail, userNameStr));
            presenter.getCurrentUser();
        }

        if (!UserSessionHolder.isGuest()) {
            presenter.getDataFromFirebase();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (NetworkUtil.isConnected()) {
                    swipeRefreshLayout.setEnabled(true);
                    refreshUI();
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
                    mealOfDayProgressBar.setVisibility(View.GONE);
                    mealsProgressBar.setVisibility(View.GONE);
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

    private void refreshUI() {
        if (NetworkUtil.isConnected()) {
            mealOfDayProgressBar.setVisibility(View.VISIBLE);
            mealsProgressBar.setVisibility(View.VISIBLE);
            mealOfDayCard.setVisibility(View.GONE);
            mealsAdapter.clearMeals();
            presenter.getRandomMeal();
            presenter.getRandomMeals();
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initUI(View v) {
        signOutIcon = v.findViewById(R.id.signOutIcon);
        userName = v.findViewById(R.id.userName);
        mealOfDayCard = v.findViewById(R.id.mealOfTheDayCard);
        mealOfDayImage = v.findViewById(R.id.mealImage);
        mealOfDayTitle = v.findViewById(R.id.mealName);
        mealOfDayProgressBar = v.findViewById(R.id.mealOfDayProgress);
        mealsProgressBar = v.findViewById(R.id.mealsProgress);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        mealsRecycler = v.findViewById(R.id.mealsRecycler);
        mealsRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mealsAdapter = new MealsAdapter(this);
        mealsRecycler.setAdapter(mealsAdapter);


        if (UserSessionHolder.isGuest()) {
            signOutIcon.setVisibility(View.GONE);
            userName.setText(R.string.guest);
        } else {
            String userNameStr = UserSessionHolder.getInstance().getUser().getName();
            userName.setText(userNameStr);
        }
    }

    private void setUpListeners(String mealId) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (NetworkUtil.isConnected()) {
                refreshUI();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
            }
        });

        mealOfDayCard.setOnClickListener(v -> onMealItemClicked(mealId));

        signOutIcon.setOnClickListener(v -> showSignOutDialog());
    }

    private void showSignOutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.sign_out))
                .setMessage(getString(R.string.are_you_sure_sign_out))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> signOut())
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void signOut() {
        UserSessionHolder.getInstance().setUser(null);
        SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences(ConstantKeys.USER_DATA, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        presenter.signOut();
        Intent intent = new Intent(getActivity(), MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();

    }

    @Override
    public void showRandomMeal(PreviewMeal meal) {
        mealOfDayTitle.setText(meal.getStrMeal());
        Glide.with(this).load(meal.getStrMealThumb()).into(mealOfDayImage);
        Animation fadeIn = AnimationUtils.loadAnimation(mealOfDayImage.getContext(), R.anim.fade_in);
        mealOfDayImage.startAnimation(fadeIn);
        Animation slideInBottom = AnimationUtils.loadAnimation(mealOfDayTitle.getContext(), R.anim.slide_in_bottom);
        mealOfDayTitle.startAnimation(slideInBottom);

        mealOfDayProgressBar.setVisibility(View.GONE);
        setUpListeners(meal.getIdMeal());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getCurrentUserSuccessfully(User user) {
        UserSessionHolder.getInstance().getUser().setUid(user.getUid());
    }

    @Override
    public void getCurrentUserFailed() {
        Toast.makeText(getContext(), getString(R.string.error_try_sign_in_again), Toast.LENGTH_SHORT).show();
        signOut();
        SignIn signInFragment = new SignIn();
        signInFragment.show(getParentFragmentManager(), "signInFragment");
    }


    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        mealsProgressBar.setVisibility(View.GONE);
        mealOfDayCard.setVisibility(View.VISIBLE);
        mealOfDayProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRandomMeals(PreviewMeal meal) {
        mealsAdapter.updateMeals(meal);
        mealsProgressBar.setVisibility(View.GONE);
        mealOfDayCard.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(networkReceiver);
    }


    @Override
    public void onMealItemClicked(String mealId) {
        Intent intent = new Intent(requireActivity(), MealDetails.class);
        intent.putExtra(ConstantKeys.MEAL_ID, mealId);
        startActivity(intent);

    }
}