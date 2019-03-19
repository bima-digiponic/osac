package osac.digiponic.com.osac.view.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import osac.digiponic.com.osac.PaymentSuccessActivity;
import osac.digiponic.com.osac.R;

public class PaymentActivity extends AppCompatActivity {

    private Button btnTunai, btnKartu, btnAmount1, btnAmount2, btnAmount3, btnAmount4, btnAmount5;
    private LinearLayout amountTunaiLayout, amountKartuLayout;
    private TextView amountTextView;

    private int TOTAL = 0;

    private int state = 0; // Tunai = 0; Kartu = 1;

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

        btnAmount3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPaymentSuccess = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                startActivity(toPaymentSuccess);
            }
        });

        amountTextView = findViewById(R.id.text_amount);

        amountTunaiLayout = findViewById(R.id.amount_btn_layout_tunai);
        amountKartuLayout = findViewById(R.id.amount_btn_layout_kartu);

        amountTunaiLayout.setVisibility(View.VISIBLE);
        amountKartuLayout.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            TOTAL = 0;
        } else {
            TOTAL = extras.getInt("TOTAL");
            Log.d("TOTALPAYMENT : PAYMENT" , String.valueOf(TOTAL));
            amountTextView.setText(String.valueOf(TOTAL));

        }


        int amount[] = new int[]{5000, 10000, 20000, 50000, 100000};
        int outsideCounter = 0;

        int total_temp = 330000;
        for (int i = 0; i < amount.length; i++) {
            if (total_temp <= amount[i]) {

            } else if (total_temp <= amount[i] && i == 3) {

            }
        }


    }
}
