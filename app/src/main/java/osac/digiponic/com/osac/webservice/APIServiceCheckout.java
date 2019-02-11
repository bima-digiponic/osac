package osac.digiponic.com.osac.webservice;

import com.google.gson.JsonObject;

import osac.digiponic.com.osac.Model.DataItemCheckout;
import osac.digiponic.com.osac.Model.DataItemMenu;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServiceCheckout {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("saveRawJSONData")
    Call<JsonObject> postRawJSON(@Body JsonObject jsonObject);

}

