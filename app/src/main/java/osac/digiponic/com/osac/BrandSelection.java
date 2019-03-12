package osac.digiponic.com.osac;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import osac.digiponic.com.osac.Adapter.BrandRVAdapter;
import osac.digiponic.com.osac.Adapter.VehicleRVAdapter;
import osac.digiponic.com.osac.Model.DataBrand;
import osac.digiponic.com.osac.Model.DataItemMenu;
import osac.digiponic.com.osac.R;
import osac.digiponic.com.osac.Rest.ApiClient;
import osac.digiponic.com.osac.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BrandSelection extends AppCompatActivity implements BrandRVAdapter.ItemClickListener, VehicleRVAdapter.ItemClickListener {

    private RecyclerView recyclerView_Brand;
    private RecyclerView recyclerView_Vehicle;

    public static String BrandID = null;

    private BrandRVAdapter brandRVAdapter;
    private VehicleRVAdapter vehicleRVAdapter;

    private List<DataBrand> mDataSet = new ArrayList<>();

    private VehicleListDialog vehicleDialog;

    private FragmentManager fragmentManager;
    private VehicleListDialog vehicleListDialog;

    // Variable
    private String VEHICLE_TYPE;
    private String VEHICLE_BRAND;

    // Retrofit
    private Retrofit retrofit;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_selection2);

// Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button debugBtn = findViewById(R.id.add_debug_btn);
        debugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandRVAdapter.notifyDataSetChanged();
//                brandRVAdapter.notifyItemInserted(mDataSet.size());
            }
        });
        fragmentManager = getSupportFragmentManager();
        vehicleDialog = new VehicleListDialog();

        // Init Retrofit
        retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);

        getDataBrand();
        setRV();

    }

    private void getDataBrand() {
        new Async_GetDataBrand().execute();
    }

    // Get Data
    private class Async_GetDataBrand extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            apiInterface.getMerek().enqueue(new Callback<List<DataBrand>>() {
                @Override
                public void onResponse(Call<List<DataBrand>> call, Response<List<DataBrand>> response) {
                    assert response.body() != null;
                    mDataSet.addAll(response.body());
                    Log.d("mDataBrandSize", mDataSet.size() + "");
                }

                @Override
                public void onFailure(Call<List<DataBrand>> call, Throwable t) {

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDataSet.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            brandRVAdapter.notifyDataSetChanged();
            Log.d("mDataSetSize", mDataSet.size() + "");
        }
    }

    private void setRV() {
        recyclerView_Brand = findViewById(R.id.rv_brands);
        recyclerView_Brand.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_Brand.setHasFixedSize(true);
        brandRVAdapter = new BrandRVAdapter(this, mDataSet);
        brandRVAdapter.setClickListener(this);
        recyclerView_Brand.setAdapter(brandRVAdapter);
        brandRVAdapter.notifyDataSetChanged();
        Log.d("datasize", String.valueOf(brandRVAdapter.getItemCount()));
    }


    @Override
    public void onItemClick(View view, int position) {
        vehicleListDialog.show(fragmentManager, "Daftar Kendaraan");
    }

    @Override
    public void onVehicleItemClick(View view, int position) {

    }
}
