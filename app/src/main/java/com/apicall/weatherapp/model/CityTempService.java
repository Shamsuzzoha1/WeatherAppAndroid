package com.apicall.weatherapp.model;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityTempService {
    static CityTempService cityTempService;

    private CityTempService(){}
    
    private final CityTempAPI cityTempAPI = new Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CityTempAPI.class);

    public static CityTempService getInstance() {
        if(cityTempService == null){
            return new CityTempService();
        }

        return cityTempService;
    }

    public Single<CityTemp> getCityTemp(Latlong latlong){
        String cityLatlong = latlong.getLatitude() + "," + latlong.getLongitude();
        return cityTempAPI.getCityTempData(Constant.apiKey, cityLatlong);
    }

    public Single<CityTemp> getCityByName(String name){
        return cityTempAPI.getCityTempData(Constant.apiKey, name);
    }
}
