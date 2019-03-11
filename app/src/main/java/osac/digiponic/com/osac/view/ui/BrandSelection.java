package osac.digiponic.com.osac.view.ui;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import osac.digiponic.com.osac.R;
import osac.digiponic.com.osac.model.DataBrand;
import osac.digiponic.com.osac.model.VehicleListDialog;
import osac.digiponic.com.osac.view.adapter.BrandRVAdapter;
import osac.digiponic.com.osac.view.adapter.VehicleRVAdapter;
import osac.digiponic.com.osac.viewmodel.BrandActivityViewModel;

public class BrandSelection extends AppCompatActivity implements BrandRVAdapter.ItemClickListener, VehicleRVAdapter.ItemClickListener {

    private RecyclerView recyclerView_Brand;
    private RecyclerView recyclerView_Vehicle;

    public static String BrandID = null;

    private BrandRVAdapter brandRVAdapter;
    private VehicleRVAdapter vehicleRVAdapter;
    private BrandActivityViewModel brandActivityViewModel;

    private List<DataBrand> mDataSet = new ArrayList<>();

    private Dialog vehicleDialog;

    private FragmentManager fragmentManager;
    private VehicleListDialog vehicleListDialog;


    // Variable
    private String VEHICLE_TYPE;
    private String VEHICLE_BRAND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_selection);

        // Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button debugBtn = findViewById(R.id.add_debug_btn);
        debugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandActivityViewModel.addNewData(new DataBrand("1", "2", "a", "http://app.digiponic.co.id/osac/public/uploads/2019-03/toyota.png", "toyota"));
            }
        });
        fragmentManager = getSupportFragmentManager();
        vehicleListDialog = new VehicleListDialog();

        brandActivityViewModel = ViewModelProviders.of(this).get(BrandActivityViewModel.class);
        brandActivityViewModel.init();

        brandActivityViewModel.getBrandData().observe(this, dataBrands -> {
            brandRVAdapter.notifyDataSetChanged();
            Log.d("isidebugdata", String.valueOf(brandRVAdapter.getItemCount()));
            Toast.makeText(BrandSelection.this, "onChanged", Toast.LENGTH_SHORT).show();
        });

        setRV();
    }

    private void setRV() {
        recyclerView_Brand = findViewById(R.id.rv_brands);
        recyclerView_Brand.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_Brand.setHasFixedSize(true);
        brandRVAdapter = new BrandRVAdapter(this, brandActivityViewModel.getBrandData().getValue());
        Log.d("brandvalue", brandActivityViewModel.getBrandData().getValue().toString());
        brandRVAdapter.setClickListener(this);
        recyclerView_Brand.setAdapter(brandRVAdapter);
        brandRVAdapter.notifyDataSetChanged();
        Log.d("datasize", String.valueOf(brandRVAdapter.getItemCount()));
    }

    @Override
    public void onItemClick(View view, int position) {

        BrandID = "30";
        vehicleListDialog.show(fragmentManager, "Daftar Kendaraan");
    }

//    public void setID(String id) {
//        this.BrandID = id;
//    }
//
//    public String getID() {
//        return this.BrandID;
//    }

    @Override
    public void onVehicleItemClick(View view, int position) {

    }
}
