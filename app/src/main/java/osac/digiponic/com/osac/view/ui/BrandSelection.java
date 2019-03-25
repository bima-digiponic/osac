package osac.digiponic.com.osac.view.ui;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import osac.digiponic.com.osac.R;
import osac.digiponic.com.osac.model.DataBrand;
import osac.digiponic.com.osac.view.adapter.BrandRVAdapter;
import osac.digiponic.com.osac.view.adapter.VehicleRVAdapter;
import osac.digiponic.com.osac.viewmodel.BrandActivityViewModel;

public class BrandSelection extends AppCompatActivity implements BrandRVAdapter.ItemClickListener, VehicleRVAdapter.ItemClickListener {

    private RecyclerView recyclerView_Brand;
    private RecyclerView recyclerView_Vehicle;

    public static String BrandID = null;
    public static String BrandName = null;

    private BrandRVAdapter brandRVAdapter;
    private VehicleRVAdapter vehicleRVAdapter;
    private BrandActivityViewModel brandActivityViewModel;

    private List<DataBrand> mDataSet = new ArrayList<>();

    private Dialog vehicleDialog;

    private FragmentManager fragmentManager;
    private VehicleListDialog vehicleListDialog;

    private ShimmerRecyclerView shimmerRecyclerView;

    public static String PRINTER_MAC_ADDRESS;

    private boolean doubleBackToExitPressedOnce = false;

    // Variable
    private String VEHICLE_TYPE;
    private String VEHICLE_BRAND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_selection);

        // Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        shimmerRecyclerView = findViewById(R.id.brand_shimmer_recyclerView);
        shimmerRecyclerView.showShimmerAdapter();

        if (MainActivity.mDataCart != null) {
            dataCartClear();
        }

//        Bundle extras = getIntent().getExtras();
//        if (extras == null) {
//            PRINTER_MAC_ADDRESS = null;
//        } else {
//            PRINTER_MAC_ADDRESS = extras.getString("MAC_ADDRESS");
//        }

        fragmentManager = getSupportFragmentManager();
        vehicleListDialog = new VehicleListDialog();

        brandActivityViewModel = ViewModelProviders.of(this).get(BrandActivityViewModel.class);
        brandActivityViewModel.init();

        brandActivityViewModel.getBrandData().observe(this, dataBrands -> {
            brandRVAdapter.notifyDataSetChanged();
        });

        setRV();
    }

    private void dataCartClear() {
        MainActivity.mDataCart.clear();


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan tombol kembali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setRV() {
        recyclerView_Brand = findViewById(R.id.rv_brands);
        recyclerView_Brand.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_Brand.setHasFixedSize(true);
        brandRVAdapter = new BrandRVAdapter(this, brandActivityViewModel.getBrandData().getValue());
        Log.d("brandvalue", brandActivityViewModel.getBrandData().getValue().toString());
        brandRVAdapter.setClickListener(this);
        checkInternetData();

        Log.d("datasize", String.valueOf(brandRVAdapter.getItemCount()));
    }

    private void checkInternetData() {
        new Handler().postDelayed(() -> {
            if (brandRVAdapter.getItemCount() > 0) {
                shimmerRecyclerView.hideShimmerAdapter();
                recyclerView_Brand.setAdapter(brandRVAdapter);
            } else {
                checkInternetData();
            }
        }, 1000);
    }

    @Override
    public void onItemClick(View view, int position) {

        BrandID = String.valueOf(brandRVAdapter.getVehicleId(position));
        BrandName = String.valueOf(brandRVAdapter.getBrandName(position));
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
