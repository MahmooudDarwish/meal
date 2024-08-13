package com.example.mealapp.feature.meals_viewer.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mealapp.R;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.feature.meals_viewer.model.MealsViewModel;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import java.util.List;

public class MealsViewerFragment extends Fragment implements OnMealItemClicked {

    private RecyclerView mealsRecycler;
    private MealsAdapter mealsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meals_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);

        // Fetch the meals from MealsViewModel
        MealsViewModel viewModel = MealsViewModel.getInstance();
        List<PreviewMeal> meals = viewModel.getMeals();

        // Set up the RecyclerView
        mealsAdapter = new MealsAdapter(meals, this);
        mealsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mealsRecycler.setAdapter(mealsAdapter);
    }

    private void initUI(View v) {
        mealsRecycler = v.findViewById(R.id.mealsRecycler);
    }

    @Override
    public void onMealItemClick(String mealId) {
        Intent intent = new Intent(getContext(), MealDetails.class);
        intent.putExtra("MEAL_ID", mealId);
        startActivity(intent);

    }
}
