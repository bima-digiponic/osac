package osac.digiponic.com.osac;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DataItemMenu data = mDataItem.get(i);
        viewHolder._itemName.setText(data.get_itemName());
        viewHolder._itemPrice.setText(data.get_itemPrice());
    }

    @Override
    public int getItemCount() {
        return mDataItem.size();
    }

    String getItemName(int id) {
        return String.valueOf(mDataItem.get(id).get_itemName());
    }

    String getItemPrice(int id) {
        return String.valueOf(mDataItem.get(id).get_itemPrice());
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView _itemName, _itemPrice;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            mCardView = v.findViewById(R.id.cardView_itemMenu);
            _itemName = v.findViewById(R.id.text_itemName);
            _itemPrice = v.findViewById(R.id.text_itemPrice);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }



}
