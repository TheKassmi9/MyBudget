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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jibi.R;

public class spendingFragment extends Fragment {

    private SpendingViewModel mViewModel;

    public static spendingFragment newInstance() {
        return new spendingFragment();
    }


    private RadioGroup radioGroupSpending;
    private Button buttonValidateSpending;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gonfler le layout du fragment
        return inflater.inflate(R.layout.fragment_spending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des éléments de l'interface
        radioGroupSpending = view.findViewById(R.id.group_spending);
        buttonValidateSpending = view.findViewById(R.id.button_validate_spending);
        EditText value_input_spending=view.findViewById(R.id.input_spend);

        // Gérer la sélection des RadioButtons
        radioGroupSpending.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = view.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String selectedValue = selectedRadioButton.getText().toString();
                Toast.makeText(getContext(), "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
            }
            String string_value_spending=value_input_spending.getText().toString();
            if (string_value_spending.isEmpty()) {
                // Optionnel : avertir l'utilisateur si le champ est vide
                value_input_spending.setError("enter data!!");
                return;
            }
        });

        // Bouton de validation
        buttonValidateSpending.setOnClickListener(v -> {
            int selectedId = radioGroupSpending.getCheckedRadioButtonId();
            if (selectedId != -1) {  // Si un RadioButton est sélectionné
                RadioButton selectedRadioButton = view.findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                String spendingValue=value_input_spending.getText().toString();
                // Exemple : envoyer la valeur sélectionnée à une autre activité
                Intent intent = new Intent(getActivity(), TraitementSpending.class);
                intent.putExtra("selectedSpendingType", selectedValue);
                intent.putExtra("spendingValue",spendingValue);
                startActivity(intent);

            } else {
                // Si aucun RadioButton n'est sélectionné
                Toast.makeText(getContext(), "Please select a spending type", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SpendingViewModel.class);
        // TODO: Use the ViewModel
    }

}