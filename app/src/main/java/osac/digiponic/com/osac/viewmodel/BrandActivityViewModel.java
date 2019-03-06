package osac.digiponic.com.osac.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import osac.digiponic.com.osac.model.DataBrand;
import osac.digiponic.com.osac.repository.BrandRepository;

public class BrandActivityViewModel extends ViewModel {

    private MutableLiveData<List<DataBrand>> mBrandData;
    private BrandRepository brandRepository;

    public void init() {
        if (mBrandData != null) {
            return;
        }
        brandRepository = BrandRepository.getInstance();
        mBrandData = brandRepository.getDataBrand();
    }

    public LiveData<List<DataBrand>> getBrandData() {
        return mBrandData;
    }

}
