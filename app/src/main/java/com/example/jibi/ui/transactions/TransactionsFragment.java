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
import android.graphics.Color;
import android.widget.LinearLayout;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.ArrayList;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
public class TransactionsFragment extends Fragment {
    ArrayList<DocumentSnapshot> transactions=new ArrayList<>();

    private TransactionsViewModel mViewModel;

    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = "sas"; // Replace with the actual user ID

        fetchAndProcessTransactions(db, userId);

        /*
        fetchcollection(db, userId, "income");
        fetchcollection(db, userId, "spend");

        for(DocumentSnapshot documentSnapshot:transactions){
            Toast.makeText(this,"nnn",Toast.LENGTH_SHORT).show();
               // Retrieve Date object from Firestore
                Date date = documentSnapshot.getDate("date");

                // Retrieve String for 'type'
                String name = documentSnapshot.getString("type");

                // Format the Date object to a String
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy", Locale.FRENCH);
                String formattedDate = dateFormat.format(date);
                    
                // Retrieve the 'value' field and convert it to a String
                int value = documentSnapshot.getLong("value").intValue(); // Assuming 'value' is stored as a number in Firestore
                String valueAsString = String.valueOf(value);

                // Call your method
                addTransaction(name, valueAsString, formattedDate);

        }*/

    }

    private void fetchcollection(FirebaseFirestore db, String userId, String collection_name) {
        // Reference to the user's income collection
        CollectionReference incomeRef = db.collection("users").document(userId).collection(collection_name);

        // Query the income collection
        incomeRef.get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                    // Process the income documents and add each transaction
                    for (DocumentSnapshot document : documents) {

                        transactions.add(document);
                        /*
                        String value = document.getString("value");
                        String name = document.getString("type");
                        String date = document.getString("date");
                        addTransaction(name, value, date);*/
                    }
                } else {
                    // Handle the error
                    Toast.makeText(getActivity(), "Error fetching incomes.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private String previous = "#9ef542";

    private void addTransaction(String type, String value, String date, String collectionType) {
    String color = "#32a893"; // Default to green for income

    if (collectionType=="spend") {
        color = "#FF0000"; // Red for spent
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
                newTextView.setText("> A new " + type + " added at " + date);
            else if (i == 1)
                {
                Toast.makeText(getActivity(),collectionType, Toast.LENGTH_SHORT).show();

                if (collectionType=="spend") {
                     newTextView.setText("-" + value + "$");
                }else{
                newTextView.setText("+" + value + "$");
            }
                
                
                }

            newTextView.setTextSize(originalTextView.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
            newTextView.setTextColor(Color.parseColor(color)); // Set the color based on collectionType

            newLayout.addView(newTextView);
        }
    }

    // Add the new layout with copied views to the parent layout
    parentLayout.addView(newLayout);
}







private void fetchAndProcessTransactions(FirebaseFirestore db, String userId) {
    String[] collections = {"income", "spend"};
    ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();

    for (String collection : collections) {
        CollectionReference collectionRef = db.collection("users").document(userId).collection(collection);
        tasks.add(collectionRef.get());
    }

    Tasks.whenAllComplete(tasks).addOnCompleteListener(allTasks -> {
        if (allTasks.isSuccessful()) {
            for (int i = 0; i < tasks.size(); i++) {
                Task<QuerySnapshot> task = tasks.get(i);
                if (task.isSuccessful() && task.getResult() != null) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    String collectionType = collections[i];  // "income" or "spend"

                    // Add each document with a flag indicating its collection
                    for (DocumentSnapshot document : documents) {
                        // Add the document with the collection flag
                        document.getReference().update("collectionType", collectionType);
                        transactions.add(document);
                    }
                }
            }

            // Sort transactions by date
            transactions.sort((doc1, doc2) -> {
                Date date1 = doc1.getDate("date");
                Date date2 = doc2.getDate("date");
                return date1 != null && date2 != null ? date1.compareTo(date2) : 0;
            });

            // Process sorted transactions
            for (DocumentSnapshot documentSnapshot : transactions) {
                Date date = documentSnapshot.getDate("date");
                String name = documentSnapshot.getString("type");
                int value = documentSnapshot.getLong("value").intValue();
                String collectionType = documentSnapshot.getString("collectionType");  // "income" or "spend"

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy", Locale.FRENCH);
                String formattedDate = dateFormat.format(date);

                addTransaction(name, String.valueOf(value), formattedDate, collectionType);
            }
        } else {
            Toast.makeText(getActivity(), "Error fetching data.", Toast.LENGTH_SHORT).show();
        }
    });
}

    
}
