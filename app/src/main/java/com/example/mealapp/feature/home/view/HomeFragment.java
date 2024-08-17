package com.example.mealapp.feature.home.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

import java.util.Objects;

public class HomeFragment extends Fragment implements IHome {

    ImageView mealOfDayImage, signOutIcon;
    TextView mealOfDayTitle, userName;
    CardView mealOfDayCard;
    SwipeRefreshLayout swipeRefreshLayout;

    private BroadcastReceiver networkReceiver;

    IHomePresenter presenter;

    private static final String TAG = "HomeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity();
        SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String userNameStr = sharedPreferences.getString("user_name", "");
        String userEmail = sharedPreferences.getString("user_email", "");
        UserSessionHolder.getInstance().setUser(new User(userEmail, userNameStr));

        if (UserSessionHolder.isGuest()) {
            presenter.getCurrentUser();
        }

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
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        Objects.requireNonNull(requireActivity()).registerReceiver(networkReceiver, filter);

        refreshUI();
    }



    private void refreshUI() {
        if (NetworkUtil.isConnected(requireActivity())) {
            presenter.getRandomMeal();
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
            Intent intent = new Intent(getActivity(), Home.class);
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
        UserSessionHolder.getInstance().setUser(user);

        if (!UserSessionHolder.isGuest()) {
            String userNameStr = UserSessionHolder.getInstance().getUser().getName();
            userName.setText(userNameStr);
            signOutIcon.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (networkReceiver != null) {
            Objects.requireNonNull(requireActivity()).unregisterReceiver(networkReceiver);
        }
    }
}