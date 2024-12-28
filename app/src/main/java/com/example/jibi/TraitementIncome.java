package com.example.jibi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TraitementIncome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traitement_income);
        // Récupérer le TextView où afficher la valeur
        TextView resultTextView = findViewById(R.id.TextResult);

        // Récupérer la valeur envoyée par l'Intent
        String incomeValue = getIntent().getStringExtra("income_value");

        // Afficher la valeur dans le TextView
        resultTextView.setText("INCOME THAT YOU ENTER IS " + incomeValue);
    }
}