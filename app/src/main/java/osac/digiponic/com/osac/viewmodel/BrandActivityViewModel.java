package osac.digiponic.com.osac.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import osac.digiponic.com.osac.model.DataBrand;
import osac.digiponic.com.osac.model.DataVehicle;
import osac.digiponic.com.osac.repository.BrandRepository;
import osac.digiponic.com.osac.view.ui.BrandSelection;

public class BrandActivityViewModel extends ViewModel {

    private MutableLiveData<List<DataBrand>> mBrandData;
    private BrandRepository brandRepository;
    private MutableLiveData<List<DataVehicle>> mVehiclelData;

    public void init() {
        if (mBrandData != null) {
            return;
        }

        brandRepository = BrandRepository.getInstance();
        brandRepository.initRetrofit();
        mBrandData = brandRepository.getDataBrand();
        mVehiclelData = brandRepository.getDataVehicle("");
    }

    public LiveData<List<DataBrand>> getBrandData() {
        return mBrandData;
    }

    public void addNewData(DataBrand dataBrand) {
        List<DataBrand> currentData = mBrandData.getValue();
        currentData.add(dataBrand);
        mBrandData.postValue(currentData);
    }

    public LiveData<List<DataVehicle>> getVehicleData(String brandID) {
        mVehiclelData = brandRepository.getDataVehicle(brandID);
        return mVehiclelData;
    }

}
