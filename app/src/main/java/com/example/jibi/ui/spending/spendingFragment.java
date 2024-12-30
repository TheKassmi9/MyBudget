package com.example.jibi.ui.spending;


import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.jibi.TraitementSpending;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jibi.R;

public class spendingFragment extends Fragment {

    private SpendingViewModel mViewModel;

    public static spendingFragment newInstance() {
        return new spendingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_spending, container, false);
        Button btn=view.findViewById(R.id.button_send);
        EditText value_input_income=view.findViewById(R.id.input_spend);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string_value_income = value_input_income.getText().toString();
                if (string_value_income.isEmpty()) {
                    // Optionnel : avertir l'utilisateur si le champ est vide
                    value_input_income.setError("enter income !");
                    return;
                }

                // Démarrer la nouvelle activité avec l'Intent
                Intent intent = new Intent(getContext(), TraitementSpending.class);
                intent.putExtra("spent_value", string_value_income); // Ajouter l'information à l'Intent
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SpendingViewModel.class);
        // TODO: Use the ViewModel
    }

}