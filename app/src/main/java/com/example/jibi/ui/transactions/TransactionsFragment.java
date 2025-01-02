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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    //    private FirebaseFirestore db= FirebaseFirestore.getInstance();
//    private CollectionReference collectionReference=db.collection("Users");
    private String currentUserId;
    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize Firestore and fetch data
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        currentUserId=currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String userId = "sas"; // Replace with the actual user ID
//        Toast.makeText(getActivity(), "Before calling fetch()", Toast.LENGTH_SHORT).show();
        fetchAndProcessTransactions(db, currentUserId);
    }
    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
//        // Initialize Firestore and fetch data
//        firebaseAuth=FirebaseAuth.getInstance();
//        currentUser=firebaseAuth.getCurrentUser();
//        currentUserId=currentUser.getUid();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
////        String userId = "sas"; // Replace with the actual user ID
//        Toast.makeText(getActivity(), "Before calling fetch()", Toast.LENGTH_SHORT).show();
//        fetchAndProcessTransactions(db, currentUserId);
//
//        /*
//        fetchcollection(db, userId, "income");
//        fetchcollection(db, userId, "spend");
//
//        for(DocumentSnapshot documentSnapshot:transactions){
//            Toast.makeText(this,"nnn",Toast.LENGTH_SHORT).show();
//               // Retrieve Date object from Firestore
//                Date date = documentSnapshot.getDate("date");
//
//                // Retrieve String for 'type'
//                String name = documentSnapshot.getString("type");
//
//                // Format the Date object to a String
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy", Locale.FRENCH);
//                String formattedDate = dateFormat.format(date);
//
//                // Retrieve the 'value' field and convert it to a String
//                int value = documentSnapshot.getLong("value").intValue(); // Assuming 'value' is stored as a number in Firestore
//                String valueAsString = String.valueOf(value);
//
//                // Call your method
//                addTransaction(name, valueAsString, formattedDate);
//
//        }*/
//
//    }

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
//                        Toast.makeText(getActivity(), ""+document.getDouble("value"), Toast.LENGTH_SHORT).show();
                            if(document.getDouble("value")!=0){
//                            Toast.makeText(getActivity(), ""+document.getDouble("value"), Toast.LENGTH_SHORT).show();
                                transactions.add(document);
                            }
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
        if (collectionType.equals("spend")) {
            color = "#FF0000"; // Red for spent
        }

        LinearLayout transactionsContainer = getView().findViewById(R.id.transactions_container);

        LinearLayout transactionLayout = new LinearLayout(getActivity());
        transactionLayout.setOrientation(LinearLayout.HORIZONTAL);
        transactionLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        transactionLayout.setPadding(10, 10, 10, 10);

        // Create TextView for transaction description
        TextView descriptionTextView = new TextView(getActivity());
        descriptionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
        descriptionTextView.setText(String.format(" %s: added at %s", type, date));
        descriptionTextView.setTextColor(Color.parseColor("#42b9f5"));
        descriptionTextView.setTextSize(16);
        descriptionTextView.setPadding(10, 0, 0, 0);

        // Create TextView for transaction value
        TextView valueTextView = new TextView(getActivity());
        valueTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        valueTextView.setText(String.format("%s%s$", collectionType.equals("spend") ? "-" : "+", value));
        valueTextView.setTextColor(Color.parseColor(color));
        valueTextView.setTextSize(16);
        valueTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);


        // Add TextViews to the transaction layout
        transactionLayout.addView(descriptionTextView);
        transactionLayout.addView(valueTextView);

        // Add transaction layout to the container
        transactionsContainer.addView(transactionLayout);
    }








    private void fetchAndProcessTransactions(FirebaseFirestore db, String userId) {
        String[] collections = {"income", "spend"};
        ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();

        for (String collection : collections) {
            CollectionReference collectionRef = db.collection("Users").document(userId).collection(collection);
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
//                        Toast.makeText(getActivity(), "After calling the function (before for loop)", Toast.LENGTH_SHORT).show();
                        for (DocumentSnapshot document : documents) {
                            // Add the document with the collection flag
//                            Toast.makeText(getActivity(), ""+document.getDouble("value"), Toast.LENGTH_SHORT).show();
                            if(document.getDouble("value")==0)
                                continue;
                            try{
                                document.getReference().update("collectionType", collectionType);
                                if(document!=null)
                                    transactions.add(document);
                            }catch(Exception e){
//                            Toast.makeText(getActivity(), "eee:  "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }

                // Sort transactions by date
                transactions.sort((doc1, doc2) -> {
//                    Date date1 = doc1.getDate("date");
//                    Date date2 = doc2.getDate("date");
//                    return  date1.compareTo(date2)*(-1) ;
                    Timestamp date1=doc1.getTimestamp("date");
                    Timestamp date2=doc2.getTimestamp("date");
                    return date1.compareTo(date2)*(-1);
                });

                // Process sorted transactions
                for (DocumentSnapshot documentSnapshot : transactions) {
//                    Toast.makeText(getActivity(), ""+documentSnapshot.getDouble("value"), Toast.LENGTH_SHORT).show();
//                    if(documentSnapshot.getDouble("value")!=0){
//                        Toast.makeText(getActivity(), ""+documentSnapshot.getDouble("value"), Toast.LENGTH_SHORT).show();
////                        transactions.add(documents);
//                    }
                    try{
                        Date date = documentSnapshot.getDate("date");
                        String name = documentSnapshot.getString("type");

                        double value = documentSnapshot.getDouble("value");
                        String collectionType = documentSnapshot.getString("collectionType");  // "income" or "spend"

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy", Locale.FRENCH);
                        String formattedDate = dateFormat.format(date);

                        addTransaction(name, String.valueOf(value), formattedDate, collectionType);
                    }catch(Exception e){
//                    Toast.makeText(getActivity(), "eee:  "+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            } else {
                Toast.makeText(getActivity(), "Error fetching data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}