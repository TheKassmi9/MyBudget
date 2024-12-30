package com.example.jibi;
import java.util.List;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import android.graphics.Color;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;


import java.util.HashMap;
import java.util.Map;


import com.example.jibi.databinding.ActivityMainBinding;

public class TraitementTransactions extends AppCompatActivity {
    
   
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traitement_transactions);
        
        




























        
        // Initialize Firestore
        //FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Example user ID
        String userId = "sas"; // Replace with the actual user ID
        //fetchIncomes( db,  userId, "income");
       // addField(db,userId,"goal","1000");
        // Add income to the user's income collection
        //addToCollection(db, userId, "200", "Freelance Project", "2024-12-22","spent");
        /*
         TextView text=findViewById(R.id.success);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        // Fetch all documents in the 'users' collection
        usersRef.get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documentSnapshots = task.getResult();
            for (DocumentSnapshot document : documentSnapshots) {
                // Access data from each document
                String name = document.getString("name");
                String email = document.getString("email");
               

                // Do something with the data
                text.setText("User"+ " Name: " + name + ", Email: " + email);
            }
        } else {
            text.setText("rror getting documents.");
        }
    });*/
    }
    //fetch a user field:
    private String previous="#9ef542";
private void  addTransaction(String type,String value,String date){
                if(previous=="#9ef542")previous="#32a893";
                else if(previous=="#32a893")previous="#9ef542";


         // Find the original LinearLayout and the parent layout
        LinearLayout originalLayout = findViewById(R.id.original_view);
        LinearLayout parentLayout = findViewById(R.id.linear_layout);

        // Create a new LinearLayout to hold the copied views
        LinearLayout newLayout = new LinearLayout(this);
        newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        // set background:
            int color = Color.parseColor(previous);
                 newLayout.setBackgroundColor(color);
        
        // Iterate through all children of the original layout
        for (int i = 0; i < originalLayout.getChildCount(); i++) {
            View child = originalLayout.getChildAt(i);

            // Create a copy of each child based on its type
            if (child instanceof TextView) {
                TextView originalTextView = (TextView) child;
                TextView newTextView = new TextView(this);

                // Copy properties
                newTextView.setLayoutParams(originalTextView.getLayoutParams());
                if(i==0)
                    newTextView.setText(" > A new income added at "+date);
                else if(i==1)
                    newTextView.setText("+"+value+"$");
                newTextView.setTextSize(originalTextView.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                newTextView.setTextColor(originalTextView.getTextColors());

                newLayout.addView(newTextView);
            } else if (child instanceof Button) {
                Button originalButton = (Button) child;
                Button newButton = new Button(this);

                // Copy properties
                newButton.setLayoutParams(originalButton.getLayoutParams());
                newButton.setText(originalButton.getText());

                newLayout.addView(newButton);
            }
            // Handle other view types as needed (e.g., ImageView, EditText)
        }

        // Add the new layout with copied views to the parent layout
        parentLayout.addView(newLayout);
}
private void fetchUserName(String userId,String field) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Reference to the user's document
    DocumentReference userRef = db.collection("users").document(userId);

    // Fetch the document
    userRef.get()
        .addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists, get the 'name' field
                String userName = documentSnapshot.getString(field);

                // Show the name in a Toast or use it however you need
                Toast.makeText(this, "User Name: " + userName, Toast.LENGTH_SHORT).show();
            } else {
                // Document doesn't exist
                Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(e -> {
            // Handle error
            Toast.makeText(this, "Failed to fetch user name: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
}
    //fetch collection:
    private void fetchIncomes(FirebaseFirestore db, String userId,String collection_name) {
        // Reference to the user's income collection
        CollectionReference incomeRef = db.collection("users").document(userId).collection(collection_name);

        // Query the income collection (you can add sorting or filtering as needed)
        incomeRef.get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    
                    // Process the income documents
                    for (DocumentSnapshot document : documents) {
                        String value = document.getString("value");
                        String name = document.getString("type");
                        String date = document.getString("date");
                        addTransaction(name,value,date);
                        // Display or handle the data
                       // Toast.makeText(this, "IncomeData"+ "Value: " + value + ", Name: " + name + ", Date: " + date, Toast.LENGTH_SHORT).show();

                        
                    }
                } else {
                    // Handle the error
                    Toast.makeText(TraitementTransactions.this, "Error fetching incomes.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    // add a field to a user:
    private void addField(FirebaseFirestore db, String userId,String field, String value) {
    // Reference to the specific user's document
    DocumentReference userRef = db.collection("users").document(userId);

    // Update the user's document with the new goal field
    userRef.update(field, value)
        .addOnSuccessListener(aVoid -> {
            // Success - Goal added
            Toast.makeText(this, "Goal successfully added!", Toast.LENGTH_SHORT).show();
            TextView textView=findViewById(R.id.income);
            String str="you Goal now is: "+value;
            textView.setText(str);
        })
        .addOnFailureListener(e -> {
            // Failure - Error occurred
            Toast.makeText(this, "Failed to add goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
}

    private void addToCollection(FirebaseFirestore db, String userId, String value, String name, String date,String collection_name) {
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
                Toast.makeText(this, "Income added with ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                // Failure - Error occurred
                Toast.makeText(this, "Income note added with ID: ",Toast.LENGTH_SHORT).show();
            });
    }

    
}