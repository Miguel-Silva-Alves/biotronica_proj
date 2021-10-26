package no.nordicsemi.android.nrfthingy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class ResgisterActivity extends AppCompatActivity {

    private EditText editName, editLastName, editPhone, editEmail, editPassword;
    private Button buttonRegister;
    String[] message = {"Preencha todos  os campos!", "Cadastro realizado com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);

        initComponents();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                String lastName = editLastName.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                String pass = editPassword.getText().toString();

                if (name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, message[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else if (name.length() > 30) {
                    editName.requestFocus();
                    editName.setError("Seu nome deve conter menos que 30 caracteres!");
                } else if (lastName.length() > 40) {
                    editLastName.requestFocus();
                    editLastName.setError("Seu nome deve conter menos que 40 caracteres!");
                } else if (phone.length() != 11) {
                    editPhone.requestFocus();
                    editPhone.setError("Número de celular inválido!");
                } else if (!isValidEmailAddress(email)) {
                    editEmail.requestFocus();
                    editEmail.setError("E-mail inválido!");
                } else if (pass.length() < 8 || pass.length() > 20) {
                    editPassword.requestFocus();
                    editPassword.setError("Sua senha deve conter de 8 a 20 caracteres!!");
                } else {
                    // Open or create database
                    SQLiteDatabase bd = openOrCreateDatabase( "biotronica.db", MODE_PRIVATE, null );
                    String cmd;

                    // Create table user, if table not exists
                    cmd = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, last_name VARCHAR, phone VARCHAR, email VARCHAR, password VARCHAR)";
                    bd.execSQL( cmd );

                    // Insert user data
                    cmd = "insert into user (name, last_name, phone, email, password)  values('"+name+"' , '"+lastName+"' , '"+phone+"' , '"+email+"' , '"+pass+"');";
                    bd.execSQL(cmd);

                    Snackbar snackbar = Snackbar.make(view, message[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                    LoginScreen();
                }
            }
        });
    }

    private void LoginScreen() {
        Intent intent = new Intent(ResgisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return result;
    }

    private void initComponents() {
        editName = findViewById(R.id.editName);
        editLastName = findViewById(R.id.editLastName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
    }
}