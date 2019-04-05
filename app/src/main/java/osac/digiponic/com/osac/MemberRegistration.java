package osac.digiponic.com.osac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import osac.digiponic.com.osac.view.ui.PoliceNumberInput;

public class MemberRegistration extends AppCompatActivity {

    private EditText nama, email, alamat, no_telp, no_polisi;
    private Button registBtn;
    public static boolean fromMember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_registration);

        bindView();

        registBtn.setOnClickListener(v -> {
            Intent toInputNoPol = new Intent(MemberRegistration.this, PoliceNumberInput.class);
            fromMember = true;
            startActivity(toInputNoPol);
        });
    }

    private void bindView() {
        nama = findViewById(R.id.input_member_nama);
        email = findViewById(R.id.input_member_email);
        alamat = findViewById(R.id.input_member_alamat);
        no_polisi = findViewById(R.id.input_member_no_polisi);
        no_telp = findViewById(R.id.input_member_no_telp);

        registBtn = findViewById(R.id.button_regist_member);
    }
}
