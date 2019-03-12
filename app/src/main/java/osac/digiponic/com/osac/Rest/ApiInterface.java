package osac.digiponic.com.osac.Rest;

import java.util.List;

import osac.digiponic.com.osac.Model.DataBrand;
import osac.digiponic.com.osac.Model.DataItemMenu;
import osac.digiponic.com.osac.Model.DataVehicle;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/osac/apiosac/api/jasa")
    Call<List<DataItemMenu>> getJasa();


    @GET("/osac/apiosac/api/merek")
    Call<List<DataBrand>> getMerek();

    @GET("osac/apiosac/api/merek={id}")
    Call<List<DataVehicle>> getVehicle(@Path("id") int id);

}