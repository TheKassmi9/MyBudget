package com.example.jibi;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class TraitementSpending extends AppCompatActivity {
     TextView resultTextView;
    TextView statusTextView;
    String incomeValue;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    //    private FirebaseFirestore db= FirebaseFirestore.getInstance();
//    private CollectionReference collectionReference=db.collection("Users");
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        currentUserId=currentUser.getUid();
        setContentView(R.layout.activity_traitement_spending);
       // Récupérer le TextView où afficher la valeur
        resultTextView = findViewById(R.id.income);
        statusTextView = findViewById(R.id.success);
        // Initialize Firestore

        try{
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Example user ID
//        String userId = "sas"; // Replace with the actual user ID

        

        // Récupérer la valeur envoyée par l'Intent

            incomeValue = getIntent().getStringExtra("spendingValue");
             String selectedValue  = getIntent().getStringExtra("selectedSpendingType");
       // addField(db,userId,"goal","1000");
        // Add income to the user's income collection
       addToCollection(db, currentUserId,Double.parseDouble(incomeValue), selectedValue, new Timestamp(new Date()),"spend");
        }catch(Exception e){
            statusTextView.setText("Operation Failed");  
        }

        
    }

     // add a field to a user:
    private void addField(FirebaseFirestore db, String userId,String field, double value) {
    // Reference to the specific user's document
    DocumentReference userRef = db.collection("Users").document(userId);

    // Update the user's document with the new goal field
    userRef.update(field, value)
        .addOnSuccessListener(aVoid -> {
            // Success - Goal added
                Toast.makeText(TraitementSpending.this,"success",Toast.LENGTH_SHORT).show();


        })
        .addOnFailureListener(e -> {
            // Failure - Error occurred
                Toast.makeText(TraitementSpending.this,"error",Toast.LENGTH_SHORT).show();

        });
}

      private void addToCollection(FirebaseFirestore db, String userId, double value, String name, Timestamp date, String collection_name) {
        // Reference to the user's income collection

        CollectionReference incomeRef = db.collection("Users").document(userId).collection(collection_name);
        // update the budget:
        // Reference to the user's document
    DocumentReference userRef = db.collection("Users").document(userId);

// Retrieve the document
    userRef.get().addOnCompleteListener(task -> {
    if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
            // Retrieve the specific field, e.g., "name"
            double budget = document.getDouble("budget");
            addField(db,currentUserId,"budget",budget-value);
            
        } else {
                Toast.makeText(TraitementSpending.this,"error",Toast.LENGTH_SHORT).show();
        }
    } else {
                Toast.makeText(TraitementSpending.this,"error",Toast.LENGTH_SHORT).show();
    }
    });
        // Create an income object
        Map<String, Object> income = new HashMap<>();
        income.put("value", value);
        income.put("type", name);
        income.put("date", date);
        Spend sp=new Spend(date, name, value);

        // Add the income object to Firestore
        incomeRef.add(sp)
            .addOnSuccessListener(documentReference -> {
                // Success - Income added
            // Afficher la valeur dans le TextView
                statusTextView.setText("Operation Completed");
                resultTextView.setText("You Have spent around " + incomeValue);


    
                      })
            .addOnFailureListener(e -> {
                // Failure - Error occurred
            statusTextView.setText("Operation Failed");    

                resultTextView.setText("");      

            });
    }
}