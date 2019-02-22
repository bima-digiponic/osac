package osac.digiponic.com.osac.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import osac.digiponic.com.osac.Model.DataDevice;
import osac.digiponic.com.osac.R;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private Context mContext;
    private List<DataDevice> mDataDevice;
    ItemClickListener mClickListener;

    public DeviceAdapter(Context mContext, List<DataDevice> mDataDevice) {
        this.mContext = mContext;
        this.mDataDevice = mDataDevice;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DataDevice data = mDataDevice.get(i);
        viewHolder._deviceName.setText(data.getDeviceName());
        viewHolder._deviceAddress.setText(data.getDeviceAddress());


    }

    @Override
    public int getItemCount() {
        return mDataDevice.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(DeviceAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView _deviceName, _deviceAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCardView = itemView.findViewById(R.id.cardView_device);
            _deviceName = itemView.findViewById(R.id.text_device_name);
            _deviceAddress = itemView.findViewById(R.id.text_device_address);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());

        }
    }
}
