package osac.digiponic.com.osac;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import osac.digiponic.com.osac.Adapter.VehicleRVAdapter;
import osac.digiponic.com.osac.Model.DataVehicle;
import osac.digiponic.com.osac.R;
import osac.digiponic.com.osac.Rest.ApiClient;
import osac.digiponic.com.osac.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VehicleListDialog extends DialogFragment {

    private RecyclerView recyclerView_Vehicle;
    private VehicleRVAdapter vehicleRVAdapter;
    private List<DataVehicle> mDataSet = new ArrayList<>();

    // Retrofit
    private Retrofit retrofit;
    private ApiInterface apiInterface;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vehicle_list_dialog, container);

        // Init Retrofit
        retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);

        recyclerView_Vehicle = rootView.findViewById(R.id.rv_vehicle_list);
        recyclerView_Vehicle.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        Toast.makeText(getContext(), BrandSelection.BrandID, Toast.LENGTH_SHORT).show();

        vehicleRVAdapter = new VehicleRVAdapter(this.getActivity(), mDataSet);
        recyclerView_Vehicle.setAdapter(vehicleRVAdapter);

        this.getDialog().setTitle("Pilih Kendaraaan");

        getDataVehicle("30");



        return rootView;
    }

    private void getDataVehicle(String id) {
        new Async_GetDataVehicle().execute(id);
    }

    private class Async_GetDataVehicle extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            int id = Integer.parseInt(strings[0]);
            apiInterface.getVehicle(id).enqueue(new Callback<List<DataVehicle>>() {
                @Override
                public void onResponse(Call<List<DataVehicle>> call, Response<List<DataVehicle>> response) {
                    assert response.body() != null;
                    mDataSet.addAll(response.body());
                    Log.d("mDataVehicle", mDataSet.toString());
                }

                @Override
                public void onFailure(Call<List<DataVehicle>> call, Throwable t) {

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}