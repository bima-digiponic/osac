package osac.digiponic.com.osac.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import osac.digiponic.com.osac.model.DataBrand;
import osac.digiponic.com.osac.model.DataItemMenu;
import osac.digiponic.com.osac.model.DataVehicle;
import osac.digiponic.com.osac.rest.ApiClient;
import osac.digiponic.com.osac.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static osac.digiponic.com.osac.view.ui.MainActivity.CONNECTION_TIMEOUT;
import static osac.digiponic.com.osac.view.ui.MainActivity.READ_TIMEOUT;

public class BrandRepository {

    private static BrandRepository instance;
    private ArrayList<DataBrand> dataSetBrand = new ArrayList<>();
    private ArrayList<DataVehicle> vehicleList = new ArrayList<>();
    private MutableLiveData<List<DataVehicle>> dataVehicle = new MutableLiveData<>();

    // Retrofit
    private Retrofit retrofit;
    private ApiInterface apiInterface;

    public void initRetrofit() {
        retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static BrandRepository getInstance() {
        if (instance == null) {
            instance = new BrandRepository();
        }
        return instance;
    }

    public MutableLiveData<List<DataVehicle>> getDataVehicle(String brandID) {
        new Async_GetDataVehicle().execute(brandID);
        dataVehicle.setValue(vehicleList);
        return dataVehicle;
    }

    public MutableLiveData<List<DataBrand>> getDataBrand() {
//        new Async_GetDataBrand().execute();
        dataSetBrand.add(new DataBrand("1", "2", "a", "http://app.digiponic.co.id/osac/public/uploads/2019-03/toyota.png", "toyota"));
        dataSetBrand.add(new DataBrand("1", "2", "a", "http://app.digiponic.co.id/osac/public/uploads/2019-03/toyota.png", "toyota"));
        MutableLiveData<List<DataBrand>> dataBrand = new MutableLiveData<>();

//         Get Data
        apiInterface.getBrand().enqueue(new Callback<List<DataBrand>>() {
            @Override
            public void onResponse(Call<List<DataBrand>> call, Response<List<DataBrand>> response) {
                dataBrand.setValue(response.body());
                for (int i = 0; i < response.body().size(); i++) {
                    Log.d("debugresponbody", response.body().get(i).getKeterangan());
                }
            }

            @Override
            public void onFailure(Call<List<DataBrand>> call, Throwable t) {

            }
        });

        dataBrand.setValue(dataSetBrand);
        Log.d("databranddebug", String.valueOf(dataBrand.getValue().size()));
        Log.d("datasetbrand", dataSetBrand.toString());
        return dataBrand;
    }

//    private class Async_GetDataBrand extends AsyncTask<Void, Void, Void> {
//
//        // Variable
//        HttpURLConnection conn;
//        URL url = null;
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            // Background process, Fetching data from API
//            try {
//                url = new URL("http://app.digiponic.co.id/osac/apiosac/api/merek");
//                Log.d("ConenctionTest", "connected url : " + url.toString());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                Log.d("ConenctionTest", "error url");
//            }
//            try {
//                // Setup HttpURLConnection
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(READ_TIMEOUT);
//                conn.setConnectTimeout(CONNECTION_TIMEOUT);
//                conn.setRequestMethod("GET");
//                conn.connect();
//                Log.d("ConenctionTest", "connected");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//                Log.d("ConenctionTest", "not connected");
//            }
//            try {
//                int response_code = conn.getResponseCode();
//
//                // Check Response Code
//                if (response_code == HttpURLConnection.HTTP_OK) {
//                    //Read data sent from server
//                    Log.d("ResponseCode", String.valueOf(response_code));
//                    InputStream input = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                    StringBuilder result = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        result.append(line);
//                    }
//                    String resultFromServer = "";
//                    JSONObject jsonObject = null;
//                    JSONArray jsonArray = null;
//                    try {
//                        jsonArray = new JSONArray(result.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    int jLoop = 0;
//                    while (jLoop < jsonArray.length()) {
//                        jsonObject = new JSONObject(jsonArray.get(jLoop).toString());
//                        dataSetBrand.add(new DataBrand(jsonObject.getString("id"), jsonObject.getString("kode_tipe"),
//                                jsonObject.getString("keterangan"), jsonObject.getString("gambar")));
//                        Log.d("datasetdebugbrand", dataSetBrand.get(jLoop).toString());
//                        jLoop += 1;
//                    }
//                } else {
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                conn.disconnect();
//            }
//            return null;
//        }
//    }

    private class Async_GetDataVehicle extends AsyncTask<String, String, String> {

        // Variable
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            vehicleList.clear();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String brandID = strings[0];

            try {
                url = new URL("http://app.digiponic.co.id/osac/apiosac/api/merek?id_merek=" + brandID);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    //Read data sent from server
                    Log.d("ResponseCode", String.valueOf(response_code));
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    String resultFromServer = "";
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(result.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int jLoop = 0;
                    while (jLoop < jsonArray.length()) {
                        jsonObject = new JSONObject(jsonArray.get(jLoop).toString());
                        vehicleList.add(new DataVehicle(jsonObject.getString("id"), jsonObject.getString("kode_general"),
                                jsonObject.getString("kode"), jsonObject.getString("jenis_kendaraan"), jsonObject.getString("keterangan")));
                        jLoop += 1;
                    }
                } else {
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }


            return null;
        }
    }
}
