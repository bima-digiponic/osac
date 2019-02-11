package osac.digiponic.com.osac;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import osac.digiponic.com.osac.Model.DataItemMenu;
import osac.digiponic.com.osac.webservice.APIServiceCheckout;
import osac.digiponic.com.osac.webservice.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MenuRVAdapter.ItemClickListener {

    private List<DataItemMenu> mDataItem = new ArrayList<>();
    private List<DataItemMenu> mDataCart = new ArrayList<>();
    private List<DataItemMenu> mDataItemFiltered = new ArrayList<>();

    private RecyclerView recyclerView_Menu, recyclerView_Invoice;
    private Button smallCar, mediumCar, bigCar, checkOutBtn;
    private MenuRVAdapter menuRVAdapter;
    private InvoiceRVAdapter invoiceRVAdapter;
    private ImageView emptyCart;
    private int pageState = 0;
    private Spinner typeFilter;
    private String carType = "";
    private JsonObject jsonObject;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Item " + menuRVAdapter.getItemName(position), Toast.LENGTH_SHORT).show();

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Initialize Invoice Recyclerview Placeholder
        emptyCart = findViewById(R.id.img_emptyCart);

        // Set Checkout Button
        checkOutBtn = findViewById(R.id.btn_checkout);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJSON();
                dataCartClear();
                sendData();
//                new Async_PostData();
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
                pageState = 0;
                checkState();
                new Async_GetData().execute(carType);
            }
        });
        mediumCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageState = 1;
                checkState();
                new Async_GetData().execute(carType);
            }
        });
        bigCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageState = 2;
                checkState();
                new Async_GetData().execute(carType);
            }
        });

        // Setup Spinner
        typeFilter = findViewById(R.id.spinner_filterType);
        String[] serviceType = new String[]{
                "Select Service...", "Car Wash", "Car Care"
        };
        final List<String> serviceList = new ArrayList<>(Arrays.asList(serviceType));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, serviceList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.LTGRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeFilter.setAdapter(spinnerArrayAdapter);
        typeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get Data From API
        new Async_GetData().execute(carType);
        Log.d("mDataItem", mDataItem.toString());


    }

    private void createJSON() {
        int total_price = 0;
        int discount = 0;
        if (mDataCart.size() > 0) {
            for (DataItemMenu item : mDataCart) {
                total_price += item.get_itemPrice();
            }
        }
        try {
            jsonObject = new JsonObject();

            // Add Property
            jsonObject.addProperty("police_number", "");
            jsonObject.addProperty("generals_id", "");
            jsonObject.addProperty("total_price", total_price);
            jsonObject.addProperty("discount", discount);
            jsonObject.addProperty("grand_total_price", total_price);

            //JsonArr
            JsonArray jsonArray = new JsonArray();
            for (DataItemMenu item : mDataCart) {
                JsonObject pnObj = new JsonObject();
                pnObj.addProperty("types_id", 0);
                pnObj.addProperty("types_name", item.get_itemType());
                pnObj.addProperty("services_id", item.get_itemID());
                pnObj.addProperty("services_name", item.get_itemName());
                pnObj.addProperty("service_price", item.get_itemPrice());
                jsonArray.add(pnObj);
            }
            jsonObject.add("service_detail", jsonArray);
            Log.d("EXPORTJSON", jsonObject.toString());
        } catch (JsonIOException e) {
            Log.d("JSONERROREX", e.toString());
        }

    }

    private void sendData() {
        APIServiceCheckout jsonPostService = ServiceGenerator.createService(APIServiceCheckout.class, "http://app.digiponic.co.id/osac/apiosac/api/transaction/");
        Call<JsonObject> call = jsonPostService.postRawJSON(jsonObject);
        Log.d("jsonSendData", jsonObject.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.d("respones-success", response.body().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("respones-failure", call.toString());
            }
        });
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

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDataItem.clear();

        }

        @Override
        protected String doInBackground(String... params) {

            String carType = params[0];

            try {
                url = new URL("http://app.digiponic.co.id/osac/apiosac/api/service?vehicle=" + carType);
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
                                jsonObject.getString("vehicle"), jsonObject.getString("type")));
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
        }
    }

    // Post Data
    private class Async_PostData extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://app.digiponic.co.id/osac/apiosac/api/transaction");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }

            try {
                int response_code = conn.getResponseCode();

                // Check Response Code
                if (response_code == HttpURLConnection.HTTP_OK) {
                    //Read data sent from server
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
                                jsonObject.getString("vehicle"), jsonObject.getString("type")));
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
    }


    private void checkEmpty() {
        emptyCart.setVisibility(invoiceRVAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);

    }

    private void checkState() {
        switch (pageState) {
            case 0:
                carType = "Small";
                pageState = 0;
                changeBtnBackgroound(smallCar);

                break;
            case 1:
                carType = "Medium";
                pageState = 1;
                changeBtnBackgroound(mediumCar);
                break;
            case 2:
                carType = "Big";
                pageState = 2;
                changeBtnBackgroound(bigCar);
                break;
        }
    }

    private void changeBtnBackgroound(Button button) {
        smallCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mediumCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bigCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

    }

    private void dataCartClear() {
        mDataCart.clear();
        for (DataItemMenu item : mDataItem) {
            item.setSelected(false);
        }
        invoiceRVAdapter.notifyDataSetChanged();
        menuRVAdapter.notifyDataSetChanged();
    }


}
