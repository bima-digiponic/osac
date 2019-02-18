package osac.digiponic.com.osac.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import osac.digiponic.com.osac.Model.DataItemMenu;
import osac.digiponic.com.osac.R;

public class MenuRVAdapter extends RecyclerView.Adapter<MenuRVAdapter.ViewHolder> {

    private List<DataItemMenu> mDataItem;
    private Context mContext;
    ItemClickListener mClickListener;

    public MenuRVAdapter(Context mContext, List<DataItemMenu> mDataItem) {
        this.mDataItem = mDataItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        DataItemMenu data = mDataItem.get(i);
        viewHolder._itemName.setText(data.get_itemName());
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        viewHolder._itemPrice.setText(formatRupiah.format((double)data.get_itemPrice()));
        if (isSelected(i)) {
            viewHolder._deleteLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder._deleteLayout.setVisibility(View.GONE);
        }
//        switch (Integer.parseInt(data.get_itemID())) {
//            case 7:
//                viewHolder._itemImage.setImageResource(R.drawable.ganti_ban);
//                break;
//            case 8:
//                viewHolder._itemImage.setImageResource(R.drawable.cuci_salju);
//                break;
//            case 3:
//                viewHolder._itemImage.setImageResource(R.drawable.cuci_mobil_kering);
//                break;
//            case 4:
//                viewHolder._itemImage.setImageResource(R.drawable.cuci_body);
//                break;
//            case 6:
//                viewHolder._itemImage.setImageResource(R.drawable.ganti_oli);
//                break;
//            case 2:
//                viewHolder._itemImage.setImageResource(R.drawable.cuci_mobil_kering);
//                break;
//            case 5:
//                viewHolder._itemImage.setImageResource(R.drawable.poles_body);
//                break;
//        }
        switch (data.get_itemVehicleType()) {
            case "Small":
                viewHolder._itemImage.setImageResource(R.drawable.small);
                break;
            case "Medium":
                viewHolder._itemImage.setImageResource(R.drawable.medium);
                break;
            case "Big":
                viewHolder._itemImage.setImageResource(R.drawable.big);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataItem.size();
    }

    public String getItemName(int id) {
        return String.valueOf(mDataItem.get(id).get_itemName());
    }

    public String getItemPrice(int id) {
        return String.valueOf(mDataItem.get(id).get_itemPrice());
    }

    public void setSelected(int id, boolean input) {
        mDataItem.get(id).setSelected(input);
    }

    public boolean isSelected(int id) {
        return mDataItem.get(id).isSelected();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView _itemName, _itemPrice;
        public LinearLayout _deleteLayout;
        public ImageView _itemImage;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            _deleteLayout = v.findViewById(R.id.layout_invoice_delete);
            mCardView = v.findViewById(R.id.cardView_itemMenu);
            _itemName = v.findViewById(R.id.text_itemName);
            _itemPrice = v.findViewById(R.id.text_itemPrice);
            _itemImage = v.findViewById(R.id.img_itemImg);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());



        }
    }



}
