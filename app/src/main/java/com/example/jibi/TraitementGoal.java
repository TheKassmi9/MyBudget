package com.example.jibi;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
public class TraitementGoal extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
//    private FirebaseFirestore db= FirebaseFirestore.getInstance();
//    private CollectionReference collectionReference=db.collection("Users");
    private String currentUserId;
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traitement_goal);
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        currentUserId=currentUser.getUid();

         // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Example user ID
//        String userId = "sas"; // Replace with the actual user ID
      //  fetchIncomes( db,  userId, "income");
        String goalValue = getIntent().getStringExtra("goal_value");

        addField(db,currentUserId,"goal",Double.parseDouble(goalValue));
/*      
        // Récupérer le TextView où afficher la valeur
        TextView resultTextView = findViewById(R.id.TextResult);

        // Récupérer la valeur envoyée par l'Intent
        String goalValue = getIntent().getStringExtra("goal_value");

        // Afficher la valeur dans le TextView
        resultTextView.setText("YOUR GOAL IS " + goalValue );*/
    }

     // add a field to a user:
    private void addField(FirebaseFirestore db, String userId,String field, double value) {
    // Reference to the specific user's document
    DocumentReference userRef = db.collection("Users").document(userId);

    // Update the user's document with the new goal field
    userRef.update(field, value)
        .addOnSuccessListener(aVoid -> {
            // Success - Goal added
            TextView textView=findViewById(R.id.income);
            String str="your Goal now is: "+value+"$";
            textView.setText(str);
        })
        .addOnFailureListener(e -> {
            // Failure - Error occurred
            Toast.makeText(this, "Failed to add goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
}

}