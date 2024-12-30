package com.example.jibi;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
public class TraitementIncome extends AppCompatActivity {
    TextView resultTextView;
    TextView statusTextView;
    String incomeValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traitement_income);
        // Récupérer le TextView où afficher la valeur
         resultTextView = findViewById(R.id.income);
        statusTextView = findViewById(R.id.income_status);
        incomeValue= getIntent().getStringExtra("income_value");
        // Initialize Firestore
        try{
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Example user ID
        String userId = "sas"; // Replace with the actual user ID

        

        // Récupérer la valeur envoyée par l'Intent
         
       // addField(db,userId,"goal","1000");
        // Add income to the user's income collection
        addToCollection(db, userId, Integer.parseInt(incomeValue), "Freelance Project", new Date(),"income");
        }catch(Exception e){
            statusTextView.setText("Operation Failed");  
        }

        
    }

     private void addToCollection(FirebaseFirestore db, String userId, int value, String name, Date date,String collection_name) {
        // Reference to the user's income collection

        CollectionReference incomeRef = db.collection("users").document(userId).collection(collection_name);

        // Create an income object
        Map<String, Object> income = new HashMap<>();
        income.put("value", value);
        income.put("type", name);
        income.put("date", date);

        // Add the income object to Firestore
        incomeRef.add(income)
            .addOnSuccessListener(documentReference -> {
                // Success - Income added
            // Afficher la valeur dans le TextView
                statusTextView.setText("Operation Completed");    
                resultTextView.setText("INCOME THAT YOU ENTER IS " + incomeValue);  
                  
    
                      })
            .addOnFailureListener(e -> {
                // Failure - Error occurred
            statusTextView.setText("Operation Failed");    

                resultTextView.setText("");      

            });
    }
}