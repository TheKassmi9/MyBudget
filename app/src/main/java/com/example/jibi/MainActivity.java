package com.example.jibi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextUsername = findViewById(R.id.input_username);
        EditText editTextPassword = findViewById(R.id.input_password);
        Button btnLogin = findViewById(R.id.button_send);

        btnLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            // Sauvegarder les données utilisateur dans SharedPreferences
            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("username", username)
                    .putString("password", password)
                    .apply();

            // Démarrer NavigationdrawerActivity
            Intent intent = new Intent(MainActivity.this, NavigationdrawerActivity.class);
            startActivity(intent);
        });
    }
}