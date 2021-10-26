package no.nordicsemi.android.nrfthingy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import no.nordicsemi.android.nrfthingy.configuration.InitialConfigurationActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView registerText;
    private EditText editEmail, editPassword;
    private Button buttonSignIn;
    private ProgressBar progressBar;
    String[] message = {"Preencha todos os campos!", "Usuário não cadastrado!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResgisterActivity.class);
                startActivity(intent);
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String pass = editPassword.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, message[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    SQLiteDatabase bd = openOrCreateDatabase( "biotronica.db", MODE_PRIVATE, null );
                    String cmd = "select id from user where email like '" + email + "' and password like '" + pass + "' ;";
                    Cursor data = bd.rawQuery(cmd, null);

                    if(data.moveToNext()) {
                        SharedPreferences prefs;
                        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor ed = prefs.edit();
                        ed.putString("email", email);
                        ed.apply();

                        finish();
                        InitialConfigurationScreen();
                    } else {
                        Snackbar snackbar = Snackbar.make(view, message[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            }
        });
    }

    private void InitialConfigurationScreen() {
        Intent intent = new Intent(LoginActivity.this, InitialConfigurationActivity.class);
        startActivity(intent);
        finish();
    }

    private void initComponents() {
        registerText = findViewById(R.id.registerText);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonSignIn  = findViewById(R.id.buttonSignIn );
        progressBar  = findViewById(R.id.progressBar);
    }
}