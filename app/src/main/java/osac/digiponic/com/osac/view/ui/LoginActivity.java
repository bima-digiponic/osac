package osac.digiponic.com.osac.view.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import osac.digiponic.com.osac.R;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindView();
    }

    private void bindView() {
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        submit = findViewById(R.id.button_login);


    }
}
