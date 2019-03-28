package osac.digiponic.com.osac.view.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import osac.digiponic.com.osac.R;

public class PaymentSuccessActivity extends AppCompatActivity {

    private Button btnDone, btnCetak;
    private TextView textView_amount_ps, textView_ps_1;

    private Locale localeID = new Locale("in", "ID");
    private DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(localeID);
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    private long kembalian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        formatRupiah.setMaximumFractionDigits(3);
        textView_amount_ps = findViewById(R.id.text_amount_ps);
        textView_ps_1 = findViewById(R.id.text_ps_1);

        if (PaymentActivity.state == 0) {
            textView_ps_1.setText("Pembayaran dengan Tunai berhasil dilakukan");
        } else {
            textView_ps_1.setText("Pembayaran dengan Debit berhasil dilakukan");
        }

        if (NumpadDialog.amount != null &&   !NumpadDialog.amount.equals("")) {
            kembalian = Long.valueOf(NumpadDialog.amount) - (long) PaymentActivity.TOTAL;
        } else {
            kembalian = 0;
        }
        textView_amount_ps.setText(String.valueOf(formatRupiah.format(kembalian)));

        btnDone = findViewById(R.id.btn_selesai);

        btnDone.setOnClickListener(v -> {
            Intent toBrand = new Intent(PaymentSuccessActivity.this, BrandSelection.class);
            NumpadDialog.amount = "0";
            toBrand.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toBrand.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toBrand);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (NumpadDialog.amount != null) {
            NumpadDialog.amount = "";
        }
        Intent toBrand = new Intent(PaymentSuccessActivity.this, BrandSelection.class);
        toBrand.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toBrand.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toBrand);
        finish();
    }
}
