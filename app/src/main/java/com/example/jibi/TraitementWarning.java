package com.example.jibi;

import android.os.Bundle;
import android.widget.TextView;

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

public class TraitementWarning extends AppCompatActivity {
     String warningValue;
     TextView resultTextView;
    TextView statusTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traitement_warning);


        // Récupérer le TextView où afficher la valeur
         resultTextView = findViewById(R.id.income);
         statusTextView = findViewById(R.id.success);

        // Récupérer la valeur envoyée par l'Intent
         warningValue = getIntent().getStringExtra("warning_value");
         try{
                // Initialize Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Example user ID
                String userId = "sas"; // Replace with the actual user ID
                //  fetchIncomes( db,  userId, "income");
                addField(db,userId,"warning",warningValue);
                // Afficher la valeur dans le TextView
            }catch(Exception e){
            resultTextView.setText("Operation Failed");  
        }


    }
     // add a field to a user:
    private void addField(FirebaseFirestore db, String userId,String field, String value) {
    // Reference to the specific user's document
    DocumentReference userRef = db.collection("users").document(userId);

    // Update the user's document with the new goal field
    userRef.update(field, value)
        .addOnSuccessListener(aVoid -> {
            // Success - Goal added
        statusTextView.setText("Operation Compeleted ");

        resultTextView.setText("Warning for  " + warningValue +"");

        })
        .addOnFailureListener(e -> {
            // Failure - Error occurred
            statusTextView.setText("Failed to add warning: " + e.getMessage());

        });
}

}