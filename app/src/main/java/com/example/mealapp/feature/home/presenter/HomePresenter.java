package com.example.mealapp.feature.home.presenter;

import android.util.Log;

import com.example.mealapp.feature.home.view.IHome;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.firebase.FirebaseManager;
import com.example.mealapp.utils.firebase.OnUserRetrieveData;
import com.example.mealapp.utils.network.HomeNetworkDelegate;


public class HomePresenter implements IHomePresenter, HomeNetworkDelegate {

    private final IHome _view;
    private final MealRepository _repo;

    private static final String TAG = "HomePresenter";

    public HomePresenter(IHome view, MealRepository repo) {
        _view = view;
        _repo = repo;
    }
    @Override
    public void getRandomMeal() {
        _repo.getRandomMeal(this);
    }


    @Override
    public void getCurrentUser() {
        FirebaseManager.getInstance().getCurrentUser(
                new OnUserRetrieveData() {
                    public void onUserDataRetrieved(User user) {
                        if (user != null) {
                            _view.getCurrentUserSuccessfully(user);
                        } else {
                            Log.i(TAG, "User is null");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(TAG, "Error getting user data: " + e.getMessage());
                    }
                });
    }

    @Override
    public void signOut() {
        FirebaseManager.getInstance().signOut();
    }
    @Override
    public void onGetRandomMealSuccessResult(PreviewMeal previewMeal) {
        _view.showRandomMeal(previewMeal);
    }
    @Override
    public void onFailureResult(String errorMsg) {
        _view.onFailureResult(errorMsg);

    }
}
