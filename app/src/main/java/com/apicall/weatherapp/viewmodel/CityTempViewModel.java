package com.apicall.weatherapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apicall.weatherapp.model.CityAutocom;
import com.apicall.weatherapp.model.CityTemp;
import com.apicall.weatherapp.model.CityTempService;
import com.apicall.weatherapp.model.Constant;
import com.apicall.weatherapp.model.Latlong;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CityTempViewModel extends ViewModel {
    private final MutableLiveData<CityTemp> cityTempLive = new MutableLiveData<>();
    private final CityTempService cityTempService = CityTempService.getInstance();
    private final MutableLiveData<List<CityAutocom>> cityAutocompleteLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final DecimalFormat decfor = new DecimalFormat("0.000");

    public LiveData<CityTemp> getCityTempLiveData() {
        return (LiveData<CityTemp>) cityTempLive;
    }

    private HashSet<String> userLatlongSet = new HashSet<>();

    public void callWeatherApi(Latlong latlong) {


        //if user location already exists in the set i.e. same location is retrieved by GPS
        //don't call the API for data
        //if (!userLatlongSet.contains(latlong.getLatitude().floatValue() + " " + latlong.getLongitude().floatValue())) {
        //    Log.d(Constant.LOG_TAG + " not same loc", String.valueOf(userLatlongSet.size()));
        //    Log.d(Constant.LOG_TAG + "locfree", decfor.format(latlong.getLatitude()) + " " + decfor.format(latlong.getLongitude()));
        //}

        userLatlongSet.add(latlong.getLatitude() + " " + latlong.getLongitude());
        isLoading.setValue(true);
        compositeDisposable.add(cityTempService.getCityTemp(latlong)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CityTemp>() {
                                   @Override
                                   public void onSuccess(CityTemp cityTemp) {
                                       Log.d(Constant.LOG_TAG + "lat: ", String.valueOf(cityTemp.getLocation().getLat()));
                                       Log.d(Constant.LOG_TAG + "long: ", String.valueOf(cityTemp.getLocation().getLon()));
                                       isLoading.setValue(false);
                                       cityTempLive.setValue(cityTemp);
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       String err = (e.getMessage() == null) ? "Professional error message" : e.getMessage();
                                       Log.d(Constant.LOG_TAG, err);
                                       e.printStackTrace();
                                   }
                               }
                ));
    }

    public void callWeatherAPIByName(String cityName) {
        isLoading.setValue(true);
        compositeDisposable.add(cityTempService.getCityByName(cityName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CityTemp>() {
                                   @Override
                                   public void onSuccess(CityTemp cityTemp) {
                                       isLoading.setValue(false);
                                       cityTempLive.setValue(cityTemp);
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       String err = (e.getMessage() == null) ? "Professional error message" : e.getMessage();
                                       Log.d(Constant.LOG_TAG, err);
                                       e.printStackTrace();
                                   }
                               }
                ));
    }

    public void callAutocompleteApi(String text) {
        compositeDisposable.add(cityTempService.getCityAutocomplete(text).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CityAutocom>>() {
                                   @Override
                                   public void onSuccess(List<CityAutocom> cityAutocoms) {
                                       if (cityAutocoms.size() == 0) {
                                           return;
                                       }

                                       Log.d(Constant.LOG_TAG + "autocom", cityAutocoms.get(0).getName());
                                       cityAutocompleteLiveData.setValue(cityAutocoms);
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       Log.d(Constant.LOG_TAG + "autocomerr", e.getMessage());
                                   }
                               }
                ));
    }

    public LiveData<Boolean> getIsLoading() {
        return (LiveData<Boolean>) isLoading;
    }

    public MutableLiveData<List<CityAutocom>> getCityAutocompleteLiveData() {
        return cityAutocompleteLiveData;
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }
}