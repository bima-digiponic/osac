package osac.digiponic.com.osac.model;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import osac.digiponic.com.osac.R;
import osac.digiponic.com.osac.view.adapter.VehicleRVAdapter;
import osac.digiponic.com.osac.view.ui.BrandSelection;
import osac.digiponic.com.osac.viewmodel.BrandActivityViewModel;

public class VehicleListDialog extends DialogFragment {

    private RecyclerView recyclerView_Vehicle;
    private VehicleRVAdapter vehicleRVAdapter;
    private List<DataVehicle> mDataSet = new ArrayList<>();

    private BrandActivityViewModel brandActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vehicle_list_dialog, container);

        brandActivityViewModel = ViewModelProviders.of(this).get(BrandActivityViewModel.class);
        brandActivityViewModel.init();


        recyclerView_Vehicle = rootView.findViewById(R.id.rv_vehicle_list);
        recyclerView_Vehicle.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        Toast.makeText(getContext(), BrandSelection.BrandID, Toast.LENGTH_SHORT).show();

        mDataSet.add(new DataVehicle("1", "a","a","a","a"));
        mDataSet.add(new DataVehicle("1", "a","a","a","a"));
        mDataSet.add(new DataVehicle("1", "a","a","a","a"));
        vehicleRVAdapter = new VehicleRVAdapter(this.getActivity(), mDataSet);
        recyclerView_Vehicle.setAdapter(vehicleRVAdapter);

        this.getDialog().setTitle("Pilih Kendaraaan");

        return rootView;
    }
}
