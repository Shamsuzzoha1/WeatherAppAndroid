package com.apicall.weatherapp.model;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CityTempAPI {
    @GET("forecast.json")
    public Single<CityTemp> getCityTempData(@Query("key") String key, @Query("q") String city);
}
