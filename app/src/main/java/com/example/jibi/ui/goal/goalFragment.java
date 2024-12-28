package com.example.jibi.ui.goal;

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
import com.example.jibi.TraitementGoal;

public class goalFragment extends Fragment {

    private GoalViewModel mViewModel;

    public static goalFragment newInstance() {
        return new goalFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_goal, container, false);
        Button btn=view.findViewById(R.id.button_validate_goal);
        EditText value_input_goal=view.findViewById(R.id.input_goal);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string_value_goal = value_input_goal.getText().toString();
                if (string_value_goal.isEmpty()) {
                    // Optionnel : avertir l'utilisateur si le champ est vide
                    value_input_goal.setError("you must have a goal in your life !");
                    return;
                }

                // Démarrer la nouvelle activité avec l'Intent
                Intent intent = new Intent(getContext(), TraitementGoal.class);
                intent.putExtra("goal_value", string_value_goal); // Ajouter l'information à l'Intent
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        // TODO: Use the ViewModel
    }

}