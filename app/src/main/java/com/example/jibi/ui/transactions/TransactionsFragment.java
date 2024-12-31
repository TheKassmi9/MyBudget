package com.example.jibi.ui.transactions;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jibi.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class TransactionsFragment extends Fragment {

    private TransactionsViewModel mViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
//    private FirebaseFirestore db= FirebaseFirestore.getInstance();
//    private CollectionReference collectionReference=db.collection("Users");
    private String currentUserId;

    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        // Initialize Firestore and fetch data
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        currentUserId=currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String userId = "sas"; // Replace with the actual user ID
        fetchcollection(db, currentUserId, "income");
        fetchcollection(db, currentUserId, "spend");

    }

    private void fetchcollection(FirebaseFirestore db, String userId, String collection_name) {
        // Reference to the user's income collection
        CollectionReference incomeRef = db.collection("Users").document(userId).collection(collection_name);

        // Query the income collection
        incomeRef.get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                    // Process the income documents and add each transaction
                    for (DocumentSnapshot document : documents) {
                        String value = document.getString("value");
                        String name = document.getString("type");
                        String date = document.getString("date");
                        addTransaction(name, value, date);
                    }
                } else {
                    // Handle the error
                    Toast.makeText(getActivity(), "Error fetching incomes.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private String previous = "#9ef542";

    private void addTransaction(String type, String value, String date) {
        if (previous.equals("#9ef542")) {
            previous = "#32a893";
        } else if (previous.equals("#32a893")) {
            previous = "#9ef542";
        }

        // Find the original LinearLayout and the parent layout
        LinearLayout originalLayout = getView().findViewById(R.id.original_view);
        LinearLayout parentLayout = getView().findViewById(R.id.linear_layout);

        // Create a new LinearLayout to hold the copied views
        LinearLayout newLayout = new LinearLayout(getActivity());
        newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        // Set background color
        int color = android.graphics.Color.parseColor(previous);
        newLayout.setBackgroundColor(color);

        // Iterate through all children of the original layout
        for (int i = 0; i < originalLayout.getChildCount(); i++) {
            View child = originalLayout.getChildAt(i);

            // Create a copy of each child based on its type
            if (child instanceof TextView) {
                TextView originalTextView = (TextView) child;
                TextView newTextView = new TextView(getActivity());

                // Copy properties
                newTextView.setLayoutParams(originalTextView.getLayoutParams());
                if (i == 0)
                    newTextView.setText(" > A new " + type + " added at " + date);
                else if (i == 1)
                    newTextView.setText("+" + value + "$");
                newTextView.setTextSize(originalTextView.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                newTextView.setTextColor(originalTextView.getTextColors());

                newLayout.addView(newTextView);
            }
        }

        // Add the new layout with copied views to the parent layout
        parentLayout.addView(newLayout);
    }
}
