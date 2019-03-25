package osac.digiponic.com.osac.view.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

import osac.digiponic.com.osac.R;
import osac.digiponic.com.osac.model.DataItemMenu;

import static osac.digiponic.com.osac.view.ui.MainActivity.BRAND;
import static osac.digiponic.com.osac.view.ui.MainActivity.DISCOUNT_TYPE;
import static osac.digiponic.com.osac.view.ui.MainActivity.POLICE_NUMBER;
import static osac.digiponic.com.osac.view.ui.MainActivity.VEHICLE_NAME;
import static osac.digiponic.com.osac.view.ui.MainActivity.VEHICLE_TYPE;
import static osac.digiponic.com.osac.view.ui.MainActivity.mDataCart;
import static osac.digiponic.com.osac.view.ui.MainActivity.total;

public class PaymentActivity extends AppCompatActivity {

    private Button btnTunai, btnKartu, btnAmount1, btnAmount2, btnAmount3, btnAmount4, btnAmount5, btnAmountDebit;
    private LinearLayout amountTunaiLayout, amountKartuLayout;
    private TextView amountTextView;

    public static int TOTAL = 0;

    private FragmentManager fragmentManager;
    private NumpadDialog numpadDialog;

    public static int state = 0; // Tunai = 0; Kartu = 1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Lock Screen to Horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        btnTunai = findViewById(R.id.btn_tunai);
        btnKartu = findViewById(R.id.btn_kartu);

        btnAmount1 = findViewById(R.id.amount_1);
        btnAmount2 = findViewById(R.id.amount_2);
        btnAmount3 = findViewById(R.id.amount_3);
        btnAmount4 = findViewById(R.id.amount_4);
        btnAmount5 = findViewById(R.id.amount_5);

        btnAmountDebit = findViewById(R.id.amount_debit);

        btnAmountDebit.setOnClickListener(v -> {
            NumpadDialog.amount = String.valueOf(TOTAL);
            toContinue();
        });

        btnAmount5.setOnClickListener(v -> numpadDialog.show(fragmentManager, "Numpad"));

        fragmentManager = getSupportFragmentManager();
        numpadDialog = new NumpadDialog();

        btnAmount3.setOnClickListener(v -> {
            Intent toPaymentSuccess = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
            startActivity(toPaymentSuccess);
        });

        btnAmount1.setOnClickListener(v -> {
            new HTTPAsyncTaskPOSTData().execute("http://app.digiponic.co.id/osac/apiosac/api/transaksi");
            toContinue();
        });

        amountTextView = findViewById(R.id.text_amount);

        amountTunaiLayout = findViewById(R.id.amount_btn_layout_tunai);
        amountKartuLayout = findViewById(R.id.amount_btn_layout_kartu);

        btnTunai.setOnClickListener(v -> {
            if (state != 0) {
                btnTunai.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnTunai.setTextColor(getResources().getColor(R.color.White));
                btnKartu.setBackground(getResources().getDrawable(R.drawable.border));
                btnKartu.setTextColor(getResources().getColor(R.color.colorPrimary));
                amountTunaiLayout.setVisibility(View.VISIBLE);
                amountKartuLayout.setVisibility(View.GONE);
                state = 0;
            }
        });
        btnKartu.setOnClickListener(v -> {
            if (state != 1) {

                btnKartu.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnKartu.setTextColor(getResources().getColor(R.color.White));
                btnTunai.setBackground(getResources().getDrawable(R.drawable.border));
                btnTunai.setTextColor(getResources().getColor(R.color.colorPrimary));
                amountKartuLayout.setVisibility(View.VISIBLE);
                amountTunaiLayout.setVisibility(View.GONE);
                state = 1;
            }
        });

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.setMaximumFractionDigits(3);

        amountTunaiLayout.setVisibility(View.VISIBLE);
        amountKartuLayout.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            TOTAL = 0;
        } else {
            TOTAL = extras.getInt("TOTAL");
            Log.d("TOTALPAYMENT : PAYMENT", String.valueOf(TOTAL));
            amountTextView.setText(String.valueOf(formatRupiah.format(TOTAL)));
        }

    }

    private void toContinue() {
        Intent toPaymentSuccess = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
        startActivity(toPaymentSuccess);
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
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 2. build JSON object
            JSONObject jsonObject = createJSON();

            // 3. add JSON content to POST request body
            setPostRequestContent(conn, jsonObject);

            // 4. make POST request to the given URL
            conn.connect();

            Log.d("responseCodePost", String.valueOf(conn.getResponseCode()));

            // Getting Response
            try {
                int response_code = conn.getResponseCode();

//                if (response_code == HttpURLConnection.HTTP_OK) {
//                    // Read Response
//
//                    InputStream input = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                    StringBuilder resultPost = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        resultPost.append(line);
//                    }
//                    String resultFromServer = "";
//                    JSONObject jsonObjectPostResponse = null;
//                    JSONArray jsonArray = null;
//                    try {
//                        jsonArray = new JSONArray(result.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    int jLoop = 0;
//                    while (jLoop < jsonArray.length()) {
//                        jsonObjectPostResponse = new JSONObject(jsonArray.get(jLoop).toString());
//
//                        jLoop += 1;
//                    }
//                }
                return ("success");
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
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

        // Get IDs
        int typesID = 0, serviceID = 0;
        String nama;


        int total_price = total;
        int discount = 0;
        if (mDataCart.size() > 0) {
            for (DataItemMenu item : mDataCart) {
                total_price += item.get_itemPrice();
            }
        }

        try {

            // Get ID's
//            for (DataVehicleType item : mDataVehicleType) {
//                Log.d("itemtipe", item.getId());
//                Log.d("itemtipe", item.getName());
//                Log.d("itemtipe", item.getTypes());
//            }

            // Data Masih Hardcoded
            String jenisKendaraan = null;
            switch (VEHICLE_TYPE) {
                case "25":
                    jenisKendaraan = "Besar";
                    break;
                case "26":
                    jenisKendaraan = "Kecil";
                    break;
                case "27":
                    jenisKendaraan = "Sedang";
                    break;
            }

            // Add Property
            jsonObject.accumulate("nomor_polisi", POLICE_NUMBER);
            jsonObject.accumulate("jenis_kendaraan", jenisKendaraan);
            jsonObject.accumulate("merek_kendaraan", BRAND);
            jsonObject.accumulate("nama_kendaraan", VEHICLE_NAME);
            jsonObject.accumulate("subtotal", total);
            jsonObject.accumulate("diskon_tipe", DISCOUNT_TYPE);
            jsonObject.accumulate("metode_pembayaran", "Tunai");
            jsonObject.accumulate("nominal_bayar", 1000);
            jsonObject.accumulate("diskon", 0);
            jsonObject.accumulate("total", total);

            //JsonArr
            JSONArray jsonArray = new JSONArray();
            for (DataItemMenu item : mDataCart) {
                JSONObject pnObj = new JSONObject();
                pnObj.accumulate("nama", item.get_itemName());
                pnObj.accumulate("harga", item.get_itemPrice());
                pnObj.accumulate("kategori", item.get_itemType());
                jsonArray.put(pnObj);
            }
            jsonObject.accumulate("penjualan_detail", jsonArray);
            Log.d("EXPORTJSON", jsonObject.toString());
        } catch (JsonIOException e) {
            Log.d("JSONERROREX", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
