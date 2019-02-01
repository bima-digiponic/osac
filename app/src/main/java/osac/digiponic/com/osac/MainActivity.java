package osac.digiponic.com.osac;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuRVAdapter.ItemClickListener{

    private List<DataItemMenu> mDataItem = new ArrayList<>();
    private List<DataItemMenu> mDataCart = new ArrayList<>();
    private RecyclerView recyclerView_Menu, recyclerView_Invoice;
    private MenuRVAdapter menuRVAdapter;
    private InvoiceRVAdapter invoiceRVAdapter;

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Item " + menuRVAdapter.getItemName(position), Toast.LENGTH_SHORT).show();

        // Add to Invoice
        mDataCart.add(new DataItemMenu(menuRVAdapter.getItemName(position), menuRVAdapter.getItemPrice(position)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mDataItem.add(new DataItemMenu("Car Wash", "Rp 1000"));
        mDataItem.add(new DataItemMenu("Hand Wash", "Rp 2000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataItem.add(new DataItemMenu("Spray Wash", "Rp 3000"));


        mDataCart.add(new DataItemMenu("Spray Wash", "Rp 3000"));
        mDataCart.add(new DataItemMenu("Hand Wash", "Rp 2000"));

        // Setup Menu Recyclerview
        recyclerView_Menu = findViewById(R.id.rv_menu);
        int numberOfColumns = 5;
        recyclerView_Menu.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        menuRVAdapter = new MenuRVAdapter(this, mDataItem);
        menuRVAdapter.setClickListener(this);
        recyclerView_Menu.setAdapter(menuRVAdapter);

        // Setup Invoice Recyclerview
        recyclerView_Invoice = findViewById(R.id.rv_invoiceItem);
        recyclerView_Invoice.setLayoutManager(new LinearLayoutManager(this));
        invoiceRVAdapter = new InvoiceRVAdapter(this, mDataCart);
        invoiceRVAdapter.setClickListener(this);
        invoiceRVAdapter.notifyDataSetChanged();
        recyclerView_Invoice.setAdapter(invoiceRVAdapter);




    }
}
