package osac.digiponic.com.osac.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import osac.digiponic.com.osac.R;
import osac.digiponic.com.osac.model.DataBrand;
import osac.digiponic.com.osac.view.adapter.BrandRVAdapter;
import osac.digiponic.com.osac.viewmodel.BrandActivityViewModel;

public class BrandSelection extends AppCompatActivity implements BrandRVAdapter.ItemClickListener {

    private RecyclerView recyclerView_Brand;

    private BrandRVAdapter brandRVAdapter;
    private BrandActivityViewModel brandActivityViewModel;

    private List<DataBrand> mDataSet = new ArrayList<>();

    // Variable
    private String VEHICLE_TYPE;
    private String VEHICLE_BRAND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_selection);

        // Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        brandActivityViewModel = ViewModelProviders.of(this).get(BrandActivityViewModel.class);
        brandActivityViewModel.init();

        brandActivityViewModel.getBrandData().observe(this, new Observer<List<DataBrand>>() {
            @Override
            public void onChanged(@Nullable List<DataBrand> dataBrands) {
                brandRVAdapter.notifyDataSetChanged();
            }
        });


        recyclerView_Brand = findViewById(R.id.rv_brands);
        recyclerView_Brand.setLayoutManager(new GridLayoutManager(this, 4));
        brandRVAdapter = new BrandRVAdapter(this, brandActivityViewModel.getBrandData().getValue());
        brandRVAdapter.setClickListener(this);
        recyclerView_Brand.setAdapter(brandRVAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent toMain = new Intent(BrandSelection.this, MainActivity.class);
        toMain.putExtra("vehicle_type", "VEHICLE_TYPE");
        toMain.putExtra("Brand", "BRAND");
        startActivity(toMain);
    }
}
