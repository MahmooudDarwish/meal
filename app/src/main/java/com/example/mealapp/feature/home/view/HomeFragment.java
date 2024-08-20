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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.feature.home.presenter.HomePresenter;
import com.example.mealapp.feature.home.presenter.IHomePresenter;
import com.example.mealapp.feature.main.view.MainScreen;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
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

    MealsAdapter mealsAdapter;

    private BroadcastReceiver networkReceiver;

    IHomePresenter presenter;

    private static final String TAG = "HomeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: sssss");
        presenter = new HomePresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity())));
        getActivity();
        SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences("user_data", Context.MODE_PRIVATE);
        boolean stayLoggedIn = sharedPreferences.getBoolean("stay_logged_in", false);
        String userNameStr = sharedPreferences.getString("user_name", "");
        String userEmail = sharedPreferences.getString("user_email", "");

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

        Log.i(TAG, "onViewCreated: sssss");
        initUI(view);

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
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        Objects.requireNonNull(requireActivity()).registerReceiver(networkReceiver, filter);
    }





    private void refreshUI() {
        if (NetworkUtil.isConnected(requireActivity())) {
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

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (NetworkUtil.isConnected(requireActivity())) {
                refreshUI();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpListeners(String mealId) {
        mealOfDayCard.setOnClickListener(v -> {
            Log.i(TAG, "setUpListeners: mealOfDayCard Clicked");
            Intent intent = new Intent(getContext(), MealDetails.class);
            intent.putExtra("MEAL_ID", mealId);
            startActivity(intent);
        });

        signOutIcon.setOnClickListener(v -> {
            UserSessionHolder.getInstance().setUser(null);
            SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences("user_data", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            presenter.signOut();
            Intent intent = new Intent(getActivity(), MainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void showRandomMeal(PreviewMeal meal) {
        mealOfDayTitle.setText(meal.getStrMeal());
        Glide.with(this).load(meal.getStrMealThumb()).into(mealOfDayImage);
        setUpListeners(meal.getIdMeal());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getCurrentUserSuccessfully(User user) {
        UserSessionHolder.getInstance().getUser().setUid(user.getUid());

        Log.i(TAG, "getCurrentUserSuccessfully: " + UserSessionHolder.getInstance().getUser().getUid());

    }


    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRandomMeals(PreviewMeal meal) {
        mealsAdapter.updateMeals(meal);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (networkReceiver != null) {
            Objects.requireNonNull(requireActivity()).unregisterReceiver(networkReceiver);
        }
    }

    @Override
    public void onMealItemClicked(String mealId) {
        Intent intent = new Intent(requireActivity(), MealDetails.class);
        intent.putExtra("MEAL_ID", mealId);
        startActivity(intent);

    }
}