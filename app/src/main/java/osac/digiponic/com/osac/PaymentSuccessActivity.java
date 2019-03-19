package osac.digiponic.com.osac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import osac.digiponic.com.osac.view.ui.BrandSelection;

public class PaymentSuccessActivity extends AppCompatActivity {

    private Button btnDone, btnCetak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        btnDone = findViewById(R.id.btn_selesai);

        btnDone.setOnClickListener(v -> {
            Intent toBrand = new Intent(PaymentSuccessActivity.this, BrandSelection.class);
            startActivity(toBrand);
            finish();
        });


    }
}
