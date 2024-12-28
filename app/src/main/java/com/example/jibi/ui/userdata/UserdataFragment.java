package com.example.jibi.ui.userdata;

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
import com.example.jibi.TraitementUserdata;

public class UserdataFragment extends Fragment {

    private UserdataViewModel mViewModel;

    public static UserdataFragment newInstance() {
        return new UserdataFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_userdata, container, false);

        Button btn=view.findViewById(R.id.button_validate_userdata);
        EditText value_input_username=view.findViewById(R.id.editTextUsername);
        EditText value_input_password=view.findViewById(R.id.editTextPassword);
        EditText value_input_email=view.findViewById(R.id.editTextEmail);
        EditText value_input_datebirth=view.findViewById(R.id.editTextDateBirth);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string_value_username = value_input_username.getText().toString();
                String string_value_password = value_input_password.getText().toString();
                String string_value_email = value_input_email.getText().toString();
                String string_value_datebirth = value_input_datebirth.getText().toString();

              /*  if (string_value_username.isEmpty() || string_value_password.isEmpty()) {
                    // Optionnel : avertir l'utilisateur si le champ est vide
                    value_input_username.setError("verifi 7wayjk !");
                    value_input_password.setError("jri 3la ROU7K");
                    return;
                }

               */

                // Démarrer la nouvelle activité avec l'Intent
                Intent intent = new Intent(getContext(), TraitementUserdata.class);
                intent.putExtra("username_value_edit", string_value_username); // Ajouter l'information à l'Intent
                intent.putExtra("password_value_edit",string_value_password);
                intent.putExtra("email_value_edit", string_value_email); // Ajouter l'information à l'Intent
                intent.putExtra("datebirth_value_edit",string_value_datebirth);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserdataViewModel.class);
        // TODO: Use the ViewModel
    }

}