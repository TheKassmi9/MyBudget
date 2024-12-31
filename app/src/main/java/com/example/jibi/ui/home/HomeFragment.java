package com.example.jibi.ui.home;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.jibi.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;


public class HomeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
        private FirebaseFirestore db;
//    private CollectionReference collectionReference=db.collection("Users");
    private String currentUserId;
    private LineChart lineChart;
    private PieChart pieChart;
    private ProgressBar progressBar;
    private TextView progressLabel;
    //Widgets
    TextView actual_budget;
    TextView tv_total_income;
    TextView tv_total_spending;

    private double income = 0;
    private double spending = 0;
    private double remainingBudget ;
    private double goal; // Replace with your goal amount
    private double progress ;
//    = (spending * 100) / (income - goal)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //database
        db= FirebaseFirestore.getInstance();
        // Initialize Firestore and fetch data
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        currentUserId=currentUser.getUid();
        tv_total_income= view.findViewById(R.id.tv_total_income);
        tv_total_spending=view.findViewById(R.id.tv_total_spending);
        actual_budget=view.findViewById(R.id.actual_budget);
        DocumentReference userDocRef = db.collection("Users").document(currentUserId);

        //fetching DAta from firestore
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                remainingBudget= documentSnapshot.getDouble("budget");
                goal=documentSnapshot.getDouble("goal");
                actual_budget.setText("$"+remainingBudget);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        fetchcollection(db,currentUserId,"income");
        fetchcollection(db,currentUserId,"spend");




        // Initialize views
        pieChart = view.findViewById(R.id.pie_chart);
        lineChart = view.findViewById(R.id.line_chart);
        progressBar = view.findViewById(R.id.budget_progress_bar);
        progressLabel = view.findViewById(R.id.progress_goal_text);
        // Set up the progress bar and label
//        progressBar.setProgress((int) progress);
//        progressLabel.setText(progress + "%");
//
//        if (remainingBudget < goal) {
//            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
//        } else {
//            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
//        }

        // Set up the line chart
        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Data Set");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();

        // Set up the pie chart
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(500f, "Food"));
        entries.add(new PieEntry(300f, "Rent"));
        entries.add(new PieEntry(200f, "Entertainment"));
        entries.add(new PieEntry(100f, "Transportation"));

        PieDataSet dataSet = new PieDataSet(entries, "Spending by Category");
        dataSet.setColors(new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW});
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Spending");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(16f);
        pieChart.animateY(1000);

        pieChart.invalidate();
    }

    private List<Entry> dataValues() {
        ArrayList<Entry> dataValue = new ArrayList<>();
        dataValue.add(new Entry(0, 10));
        dataValue.add(new Entry(1, 15));
        dataValue.add(new Entry(2, 20));
        dataValue.add(new Entry(3, 30));
        dataValue.add(new Entry(4, 25));
        return dataValue;
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

                            double value =   document.getDouble("value");
                            if(collection_name.equals("income")){
                                income+=value;

                            }
                            else{
                                spending+=value;
                            }
                        }
                        if(collection_name.equals("income")){
                            tv_total_income.setText("$"+income);
                        }
                        else{
                            tv_total_spending.setText("$"+spending);
                        }
                        progress= (spending* 100) / (income - goal);
                        progressBar.setProgress((int) progress);
                        progressLabel.setText((int)progress + "%");

                        if (income-spending < goal) {
                            progressBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.red)));
                        } else {
                            progressBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.green)));
                        }
                    } else {
                        // Handle the error
                        Toast.makeText(getActivity(), "Error fetching incomes.", Toast.LENGTH_SHORT).show();
                    }
                });
        income=0;
        spending=0;

    }

}