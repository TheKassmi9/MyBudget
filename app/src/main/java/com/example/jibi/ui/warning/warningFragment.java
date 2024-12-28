package com.example.jibi.ui.warning;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jibi.R;
import com.example.jibi.TraitementWarning;

public class warningFragment extends Fragment {

    private WarningViewModel mViewModel;

    public static warningFragment newInstance() {
        return new warningFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_warning, container, false);
        Button btn=view.findViewById(R.id.button_validate_warning);
        EditText value_input_warning=view.findViewById(R.id.input_warning);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string_value_warning = value_input_warning.getText().toString();
                if (string_value_warning.isEmpty()) {
                    // Optionnel : avertir l'utilisateur si le champ est vide
                    value_input_warning.setError("be careful, you must have limits !");
                    return;
                }

                // Démarrer la nouvelle activité avec l'Intent
                Intent intent = new Intent(getContext(), TraitementWarning.class);
                intent.putExtra("warning_value", string_value_warning); // Ajouter l'information à l'Intent
                startActivity(intent);
            }
        });

        return view;    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WarningViewModel.class);
        // TODO: Use the ViewModel
    }

}