package com.example.jibi.ui.logout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jibi.MainActivity;
import com.example.jibi.R;
import com.example.jibi.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment {

    private LogoutViewModel mViewModel;
    private FirebaseAuth firebaseAuth;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_logout, container, false);
        // je sais pas pourkoi il ya erreur dans les buttons ici je vais les commanter pour  demain
        Button btn_valider=view.findViewById(R.id.button_valider_logout);
        Button btn_annuler=view.findViewById(R.id.button_annuler_logout);
//        btn_annuler.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Create an instance of HomeFragment
//
//            }
//        });
        // Bouton pour annuler et revenir au fragment Home
        btn_annuler.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.nav_home);});


        firebaseAuth=FirebaseAuth.getInstance();

        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Démarrer la nouvelle activité avec l'Intent
                clearPreferencesOnLogout();
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
//                Toast.makeText(getContext(), "Logout succes,Login now", Toast.LENGTH_LONG).show();
                startActivity(intent);

            }
        });

//        btn_annuler.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Naviguer vers le fragment Home
////                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_navigation_drawer);
////                navController.navigate(R.id.nav_home); // ID du HomeFragment dans navigation.xml
//
//                Toast.makeText(getContext(), "Retour à Home", Toast.LENGTH_LONG).show();
//            }
//        });


        return view;

    }
    // Define the shared preferences name
    private static final String PREFS_NAME = "user_profile_prefs";

    // Method to clear SharedPreferences
    private void clearPreferencesOnLogout() {
        // Access the shared preferences
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, requireContext().MODE_PRIVATE);

        // Edit the preferences and clear them
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Clears all data in SharedPreferences
        editor.apply(); // Apply the changes asynchronously
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        // TODO: Use the ViewModel
    }

}