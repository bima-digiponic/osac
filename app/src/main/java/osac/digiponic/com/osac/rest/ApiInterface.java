package osac.digiponic.com.osac.rest;

import java.util.List;

import osac.digiponic.com.osac.model.DataBrand;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/osac/apiosac/api/merek")
    Call<List<DataBrand>> getBrand();

}
