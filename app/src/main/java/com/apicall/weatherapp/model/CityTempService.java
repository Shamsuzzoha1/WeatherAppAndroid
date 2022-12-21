package com.apicall.weatherapp.model;

import androidx.viewbinding.BuildConfig;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityTempService {
    static CityTempService cityTempService;

    private CityTempService() {
    }

    OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
            .addInterceptor(
                    chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("Accept", "Application/JSON").build();
                        return chain.proceed(request);
                    }).build();


    private final CityTempAPI cityTempAPI = new Retrofit.Builder()
            .client(defaultHttpClient)
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CityTempAPI.class);

    public static CityTempService getInstance() {
        if (cityTempService == null) {
            return new CityTempService();
        }

        return cityTempService;
    }

    public Single<CityTemp> getCityTemp(Latlong latlong) {
        String cityLatlong = latlong.getLatitude() + "," + latlong.getLongitude();
        return cityTempAPI.getCityTempData(Constant.apiKey, cityLatlong);
    }

    public Single<CityTemp> getCityByName(String name) {
        return cityTempAPI.getCityTempData(Constant.apiKey, name);
    }

    public Single<List<CityAutocom>> getCityAutocomplete(String text){
        return cityTempAPI.getCityAutocomplete(Constant.apiKey, text);
    }
}
