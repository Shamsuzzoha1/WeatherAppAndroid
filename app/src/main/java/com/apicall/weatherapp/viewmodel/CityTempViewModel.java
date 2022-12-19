package com.apicall.weatherapp.viewmodel;

import android.location.LocationListener;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apicall.weatherapp.model.CityTemp;
import com.apicall.weatherapp.model.CityTempService;
import com.apicall.weatherapp.model.Constant;
import com.apicall.weatherapp.model.Hour;
import com.apicall.weatherapp.model.Latlong;
import com.apicall.weatherapp.view.MainActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CityTempViewModel extends ViewModel {
    private final MutableLiveData<CityTemp> cityTempLive = new MutableLiveData<>();
    private final CityTempService cityTempService = CityTempService.getInstance();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<CityTemp> getCityTempLiveData() {
        return (LiveData<CityTemp>) cityTempLive;
    }

    public void callWeatherApi(Latlong latlong) {
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

    public void callWeatherApi(String cityName) {
        isLoading.setValue(true);
        compositeDisposable.add(cityTempService.getCityByName(cityName)
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

    public LiveData<Boolean> getIsLoading() {
        return (LiveData<Boolean>) isLoading;
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }
}