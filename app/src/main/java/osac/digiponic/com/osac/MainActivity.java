package osac.digiponic.com.osac;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import osac.digiponic.com.osac.Adapter.InvoiceRVAdapter;
import osac.digiponic.com.osac.Adapter.MenuRVAdapter;
import osac.digiponic.com.osac.Model.DataItemMenu;

public class MainActivity extends AppCompatActivity implements MenuRVAdapter.ItemClickListener {

    // Dataset
    private List<DataItemMenu> mDataItem = new ArrayList<>();
    private List<DataItemMenu> mDataCart = new ArrayList<>();
    private List<DataItemMenu> dataCarWash = new ArrayList<>();
    private List<DataItemMenu> dataCarCare = new ArrayList<>();

    // Content
    private RecyclerView recyclerView_Menu, recyclerView_Invoice;
    private ImageView emptyCart;
    private Spinner typeFilter;
    private TextView total_tv, date_tv;
    private Dialog completeDialog, incompleteDialog, changeTypeDialog;
    private Button smallCar, mediumCar, bigCar, checkOutBtn;
    private SmoothProgressBar progressBar;
    private LinearLayout blackLayout;

    // Adapter
    private MenuRVAdapter menuRVAdapter;
    private InvoiceRVAdapter invoiceRVAdapter;

    // Variable
    public boolean dataFetched = false;
    private int pageState = 0;
    private String carType = "";
    private boolean resultChange;
    private String clickedType = "";

    // Constraint
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Initialize TextView Total
        total_tv = findViewById(R.id.total_textview_main);
        total_tv.setText("Rp. 0");

        // Initialize TextView Date
        date_tv = findViewById(R.id.date);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(date);
        date_tv.setText(formattedDate);

        // Setup Dialog
        completeDialog = new Dialog(this);
        setupCompleteDialog();
        incompleteDialog = new Dialog(this);
        setupUnCompleteDialog();
        changeTypeDialog = new Dialog(this);
        setupChangeTypeDialog();

        // Initialize Invoice Recyclerview Placeholder
        emptyCart = findViewById(R.id.img_emptyCart);

        // Setup Progress Bar
        progressBar = findViewById(R.id.progress_bar);
        blackLayout = findViewById(R.id.loading_layout);

        // Set Checkout Button
        checkOutBtn = findViewById(R.id.btn_checkout);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackLayout.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blackLayout.setVisibility(View.GONE);
                        if (mDataCart.size() == 0) {
                            incompleteDialog.show();
                        } else {
                            completeDialog.show();
                        }
                        new HTTPAsyncTaskPOSTData().execute("http://app.digiponic.co.id/osac/apiosac/public/transaction");
                        dataCartClear();
                        total_tv.setText("Rp. 0");
                        Log.d("datacartsize", String.valueOf(mDataCart.size()));
                    }
                }, 3000);
            }
        });

        // Button Select
        smallCar = findViewById(R.id.btn_smallCar);
        mediumCar = findViewById(R.id.btn_mediumCar);
        bigCar = findViewById(R.id.btn_largeCar);

        // Set State
        checkState();

        // Set Button onClick
        smallCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedType = "Small";
                changeTypeDialog.show();
            }
        });
        mediumCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedType = "Medium";
                changeTypeDialog.show();
            }
        });
        bigCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedType = "Big";
                changeTypeDialog.show();
            }
        });

        // Setup Spinner
        typeFilter = findViewById(R.id.spinner_filterType);
        String[] serviceType = new String[]{
                "Semua Jasa", "Car Wash", "Car Care"
        };
        final List<String> serviceList = new ArrayList<>(Arrays.asList(serviceType));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, serviceList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeFilter.setAdapter(spinnerArrayAdapter);
        typeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    filter("Car Wash");
                } else if (position == 2) {
                    filter("Car Care");
                } else if (position == 0) {
                    filter("All");
                }
                if (dataFetched) {
                    menuRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Get Data From API
        new Async_GetData().execute(carType);
        Log.d("mDataItem", mDataItem.toString());
    }

    /*
        Method ini masih ngebug karena ketika user menekan item dengan cepat. ketika program masih looping
       untuk nyari item buat di remove/di add, dia langsung ngejalanin loopingnya lagi sehingga mengubah
       isi variable position yang sedang di looping. jadi kalo di klik cepet cepet masih ngebug.
       solusinya pake sqlite, ketika user menekan item bukan dimasukkan ke mDataCart melainkan ke table
       order di SQLite (belum diinisialisasi disini), Tetapi muncul kendala baru yaitu data tidak langsung
        berubah secara real-time meskipun menggunakan notifyDataSetChanged.
    */

    @Override
    public void onItemClick(View view, int position) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int total = 0;
        if (!menuRVAdapter.isSelected(position)) {
            mDataCart.add(new DataItemMenu(menuRVAdapter.getItemName(position), menuRVAdapter.getItemPrice(position)));
            invoiceRVAdapter.notifyItemInserted(position);
            menuRVAdapter.setSelected(position, true);
            menuRVAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < mDataCart.size(); i++) {
                if (mDataCart.get(i).get_itemName().equalsIgnoreCase(menuRVAdapter.getItemName(position))) {
                    invoiceRVAdapter.removeAt(i);
                    invoiceRVAdapter.notifyItemRemoved(i);
                    menuRVAdapter.setSelected(position, false);
                    menuRVAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        Log.d("datacartsizeadd", String.valueOf(mDataCart.size()));

        for (DataItemMenu item : mDataCart) {
            total += item.get_itemPrice();
        }
        if (total > 0) {
            total_tv.setText(formatRupiah.format((double) total));
        }
    }

    private void filter(String type) {
        for (DataItemMenu item : mDataItem) {
            if (item.get_itemType().equals("Car Wash")) {
                dataCarWash.add(new DataItemMenu(item.get_itemID(), item.get_itemName(), String.valueOf(item.get_itemPrice()), item.get_itemVehicleType(), item.get_itemType(), item.isSelected()));
            } else {
                dataCarCare.add(new DataItemMenu(item.get_itemID(), item.get_itemName(), String.valueOf(item.get_itemPrice()), item.get_itemVehicleType(), item.get_itemType(), item.isSelected()));
            }
        }
        mDataItem.clear();
        if (type.equals("Car Wash")) {
            mDataItem.addAll(dataCarWash);
            dataCarWash.clear();
        } else if (type.equals("Car Care")) {
            mDataItem.addAll(dataCarCare);
            dataCarCare.clear();
        } else {
            mDataItem.addAll(dataCarCare);
            mDataItem.addAll(dataCarWash);
            dataCarCare.clear();
            dataCarWash.clear();
        }
    }

    private void setAdapterRV() {
        // Setup Menu Recyclerview
        recyclerView_Menu = findViewById(R.id.rv_menu);
        int numberOfColumns = 4;
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
        invoiceRVAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
        });
    }

    // Get Data
    private class Async_GetData extends AsyncTask<String, String, String> {

        // Variable
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Clear data before execute
            mDataItem.clear();
            dataCarCare.clear();
            dataCarWash.clear();
            mDataCart.clear();
            blackLayout.setVisibility(View.VISIBLE);
            total_tv.setText("Rp. 0");
            resultChange = false;
        }

        @Override
        protected String doInBackground(String... params) {
            // Background process, Fetching data from API
            String carType = params[0];
            try {
                url = new URL("http://app.digiponic.co.id/osac/api/public/service?vehicle=" + carType);
                Log.d("ConenctionTest", "connected url : " + url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("ConenctionTest", "error url");
                return "exception";
            }
            try {
                // Setup HttpURLConnection
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.connect();
                Log.d("ConenctionTest", "connected");
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.d("ConenctionTest", "not connected");
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();

                // Check Response Code
                if (response_code == HttpURLConnection.HTTP_OK) {
                    //Read data sent from server
                    Log.d("ResponseCode", String.valueOf(response_code));
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    String resultFromServer = "";
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(result.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int jLoop = 0;
                    while (jLoop < jsonArray.length()) {
                        jsonObject = new JSONObject(jsonArray.get(jLoop).toString());
                        mDataItem.add(new DataItemMenu(jsonObject.getString("id"),
                                jsonObject.getString("name"), jsonObject.getString("price"),
                                jsonObject.getString("vehicle"), jsonObject.getString("type"),
                                jsonObject.getString("images")));
                        jLoop += 1;
                    }
                    return (resultFromServer);
                } else {
                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setAdapterRV();
            blackLayout.setVisibility(View.GONE);
            dataFetched = true;
        }
    }

    // Post Data
    public class HTTPAsyncTaskPOSTData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }

        private String HttpPost(String myUrl) throws IOException, JSONException {
            String result = "";
            URL url = new URL(myUrl);

            // 1. create HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // 2. build JSON object
            JSONObject jsonObject = createJSON();

            // 3. add JSON content to POST request body
            setPostRequestContent(conn, jsonObject);

            // 4. make POST request to the given URL
            conn.connect();

            // 5. return response message
            return conn.getResponseMessage() + "";
        }

        private void setPostRequestContent(HttpURLConnection conn,
                                           JSONObject jsonObject) throws IOException {
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject.toString());
            Log.i(MainActivity.class.toString(), jsonObject.toString());
            writer.flush();
            writer.close();
            os.close();
        }
    }

    private JSONObject createJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        int total_price = 0;
        int discount = 0;
        if (mDataCart.size() > 0) {
            for (DataItemMenu item : mDataCart) {
                total_price += item.get_itemPrice();
            }
        }

        try {
            // Add Property
            jsonObject.accumulate("police_number", null);
            jsonObject.accumulate("generals_id", null);
            jsonObject.accumulate("total", total_price);
            jsonObject.accumulate("discount", discount);
            jsonObject.accumulate("grand_total", total_price);

            //JsonArr
            JSONArray jsonArray = new JSONArray();
            for (DataItemMenu item : mDataCart) {
                JSONObject pnObj = new JSONObject();
                pnObj.accumulate("types_id", 0);
                pnObj.accumulate("types_name", "Car Wash");
                pnObj.accumulate("services_id", 2);
                pnObj.accumulate("services_name", item.get_itemName());
                pnObj.accumulate("service_price", item.get_itemPrice());
                jsonArray.put(pnObj);
            }
            jsonObject.accumulate("service_detail", jsonArray);
            Log.d("EXPORTJSON", jsonObject.toString());
        } catch (JsonIOException e) {
            Log.d("JSONERROREX", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*
        Method bellow for supporting purpose
     */

    private void checkEmpty() {
        emptyCart.setVisibility(invoiceRVAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    private void checkState() {
        switch (pageState) {
            case 0:
                carType = "Kecil";
                pageState = 0;
                changeBtnBackgroound(smallCar);

                break;
            case 1:
                carType = "Sedang";
                pageState = 1;
                changeBtnBackgroound(mediumCar);
                break;
            case 2:
                carType = "Besar";
                pageState = 2;
                changeBtnBackgroound(bigCar);
                break;
        }
    }

    private void changeBtnBackgroound(Button button) {
        smallCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mediumCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bigCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
    }

    private void dataCartClear() {
        mDataCart.clear();
        for (DataItemMenu item : mDataItem) {
            item.setSelected(false);
        }
        invoiceRVAdapter.notifyDataSetChanged();
        menuRVAdapter.notifyDataSetChanged();
        total_tv.setText("");
    }

    private void setupCompleteDialog() {
        completeDialog.setContentView(R.layout.dialog_done);
        completeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        completeDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button okBtn = completeDialog.findViewById(R.id.dialog_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeDialog.dismiss();
            }
        });
    }

    private void setupUnCompleteDialog() {
        incompleteDialog.setContentView(R.layout.dialog_undone);
        incompleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        incompleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button okBtn = incompleteDialog.findViewById(R.id.dialog_ok_btn_undone);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incompleteDialog.dismiss();
            }
        });
    }

    private void setupChangeTypeDialog() {
        changeTypeDialog.setContentView(R.layout.change_type_dialog);
        changeTypeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeTypeDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancelBtn = changeTypeDialog.findViewById(R.id.dialog_cancel_btn_change);
        Button okBtn = changeTypeDialog.findViewById(R.id.dialog_ok_btn_change);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultChange = false;
                changeTypeDialog.dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultChange = true;
                changeTypeDialog.dismiss();
                changeType(clickedType);
            }
        });
    }

    private void changeType(String type) {
        switch (type) {
            case "Small":
                pageState = 0;
                checkState();
                new Async_GetData().execute(carType);
                typeFilter.setSelection(0);
                break;
            case "Medium":
                pageState = 1;
                checkState();
                new Async_GetData().execute(carType);
                typeFilter.setSelection(0);
                break;
            case "Big":
                pageState = 2;
                checkState();
                new Async_GetData().execute(carType);
                typeFilter.setSelection(0);
                break;
            default:

        }
    }
}
