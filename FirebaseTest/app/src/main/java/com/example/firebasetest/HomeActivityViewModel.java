package com.example.firebasetest;
// 홈에서 사용합니다

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeActivityViewModel extends ViewModel {
    private MutableLiveData<List<BannerItem>> _bannerItemList = new MutableLiveData<>();
    private MutableLiveData<Integer> _currentPosition = new MutableLiveData<>();

    public LiveData<List<BannerItem>> getBannerItemList() {
        return _bannerItemList;
    }

    public LiveData<Integer> getCurrentPosition() {
        return _currentPosition;
    }

    public void init() {
        _currentPosition.setValue(0);
    }

    public void setBannerItems(List<BannerItem> list) {
        _bannerItemList.setValue(list);
    }

    public void setCurrentPosition(int position) {
        _currentPosition.setValue(position);
    }

    public Integer getCurrentPositionValue() {
        return _currentPosition.getValue();
    }
}
