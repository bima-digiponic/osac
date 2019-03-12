package osac.digiponic.com.osac.view.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
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
import osac.digiponic.com.osac.model.DataVehicle;
import osac.digiponic.com.osac.view.adapter.VehicleRVAdapter;
import osac.digiponic.com.osac.view.ui.BrandSelection;
import osac.digiponic.com.osac.viewmodel.BrandActivityViewModel;
import osac.digiponic.com.osac.viewmodel.VehicleDialogViewModel;

public class VehicleListDialog extends DialogFragment implements VehicleRVAdapter.ItemClickListener {

    private RecyclerView recyclerView_Vehicle;
    private VehicleRVAdapter vehicleRVAdapter;
    private List<DataVehicle> mDataSet = new ArrayList<>();

    private BrandActivityViewModel brandActivityViewModel;
    private VehicleDialogViewModel vehicleDialogViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vehicle_list_dialog, container);

        vehicleDialogViewModel = ViewModelProviders.of(this).get(VehicleDialogViewModel.class);
        vehicleDialogViewModel.init(BrandSelection.BrandID);

        recyclerView_Vehicle = rootView.findViewById(R.id.rv_vehicle_list);
        recyclerView_Vehicle.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        Toast.makeText(getContext(), BrandSelection.BrandID, Toast.LENGTH_SHORT).show();

//        mDataSet.add(new DataVehicle("1", "a","a","a","a"));
        vehicleRVAdapter = new VehicleRVAdapter(this.getActivity(), vehicleDialogViewModel.getVehicleData().getValue());
        vehicleRVAdapter.setClickListener(this);
        new Handler().postDelayed(() -> {
            recyclerView_Vehicle.setAdapter(vehicleRVAdapter);
            vehicleRVAdapter.notifyDataSetChanged();
        }, 1000);

        this.getDialog().setTitle("Pilih Kendaraaan");

        return rootView;
    }


    @Override
    public void onVehicleItemClick(View view, int position) {
        Toast.makeText(this.getContext(), vehicleRVAdapter.getID(position), Toast.LENGTH_SHORT).show();
    }
}
