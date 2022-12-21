package com.apicall.weatherapp.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CityTempAPI {
    @GET("forecast.json")
    public Single<CityTemp> getCityTempData(@Query("key") String key, @Query("q") String city);

    @GET("search.json")
    public Single<List<CityAutocom>> getCityAutocomplete(@Query("key") String key, @Query("q") String text);
}
