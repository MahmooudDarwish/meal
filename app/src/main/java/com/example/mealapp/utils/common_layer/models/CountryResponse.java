package com.example.mealapp.utils.common_layer.models;

import com.example.mealapp.feature.search.model.Country;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {
    @SerializedName("meals")
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }
}
