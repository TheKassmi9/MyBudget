package com.example.jibi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TraitementWarning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traitement_warning);


        // Récupérer le TextView où afficher la valeur
        TextView resultTextView = findViewById(R.id.TextResult);

        // Récupérer la valeur envoyée par l'Intent
        String warningValue = getIntent().getStringExtra("warning_value");

        // Afficher la valeur dans le TextView
        resultTextView.setText("THE POURCENT FOR ALERT IS " + warningValue +"%");
    }
}