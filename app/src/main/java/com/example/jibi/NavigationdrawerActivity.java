package com.example.jibi;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jibi.databinding.ActivityNavigationdrawerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NavigationdrawerActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationdrawerBinding binding;
    NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private String currentUserId;
    private static final String PREFS_NAME = "user_profile_prefs";
    private static final String KEY_PROFILE_IMAGE_URI = "profile_image_uri";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Utilisation de ViewBinding pour lier l'UI
        binding = ActivityNavigationdrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView=binding.navView;
        View headerView=navigationView.getHeaderView(0);
        TextView userName=headerView.findViewById(R.id.user_name);
        TextView etEmail=headerView.findViewById(R.id.etEmail);
        ImageView profile_image= headerView.findViewById(R.id.profile_image);
//        loadImageFromPreferences(profile_image);
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        String savedImageUriString = prefs.getString(KEY_PROFILE_IMAGE_URI, "");


//        userName.setText("Assim");
        db= FirebaseFirestore.getInstance();
        // Initialize Firestore and fetch data
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        currentUserId=currentUser.getUid();
        DocumentReference userDocRef = db.collection("Users").document(currentUserId);
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String usern=documentSnapshot.getString("userName");
                String email=documentSnapshot.getString("email");
                userName.setText(usern);
                etEmail.setText(email);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        // Configuration de la barre d'outils (Toolbar)
        setSupportActionBar(binding.appBarNavigationdrawer.toolbar);

        // Configuration du Floating Action Button (FAB)
        binding.appBarNavigationdrawer.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab) // Ancre pour éviter la superposition
                        .show()
        );

        // Initialisation du DrawerLayout et du NavigationView
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Définition des destinations principales (menu de navigation)
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow // Ajoute d'autres IDs si nécessaire
        ).setOpenableLayout(drawer).build();

        // Configuration du NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigationdrawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                int menuID=navDestination.getId();
                if (menuID == R.id.nav_home) {
                    Toast.makeText(NavigationdrawerActivity.this, "Home", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_gallery) {
                    Toast.makeText(NavigationdrawerActivity.this, "Gallery", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_slideshow) {
                    Toast.makeText(NavigationdrawerActivity.this, "Slideshow", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_income) {
                    Toast.makeText(NavigationdrawerActivity.this, "Income", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_goal) {
                    Toast.makeText(NavigationdrawerActivity.this, "Goal", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_spending) {
                    Toast.makeText(NavigationdrawerActivity.this, "Spending", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_warning) {
                    Toast.makeText(NavigationdrawerActivity.this, "Warning", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_logout) {
                    Toast.makeText(NavigationdrawerActivity.this, "Logout", Toast.LENGTH_LONG).show();
                } else if (menuID == R.id.nav_userdata) {
                    Toast.makeText(NavigationdrawerActivity.this, "User DATA", Toast.LENGTH_LONG).show();
                } 
                 else if (menuID == R.id.nav_transactions) {
                    Toast.makeText(NavigationdrawerActivity.this, "Transaction", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(NavigationdrawerActivity.this, "Unknown Destination", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        binding = ActivityNavigationdrawerBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        navigationView=binding.navView;
//        View headerView=navigationView.getHeaderView(0);
//        TextView userName=headerView.findViewById(R.id.user_name);
//        TextView etEmail=headerView.findViewById(R.id.etEmail);
//        ImageView profile_image= headerView.findViewById(R.id.profile_image);
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        String savedImageUriString = prefs.getString(KEY_PROFILE_IMAGE_URI, null);
//
//        if (savedImageUriString != null && !savedImageUriString.isEmpty()) {
//            try {
//                Uri savedImageUri = Uri.parse(savedImageUriString);
//                // Debug log
//                System.out.println("Saved Image URI: " + savedImageUri.toString());
//                Glide.with(this)
//                        .load(savedImageUri)
//                        .circleCrop()
//                        .into(profile_image);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Failed to load profile image.", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            System.out.println("No Image URI found in SharedPreferences.");
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Chargement du menu pour la Toolbar
        getMenuInflater().inflate(R.menu.navigationdrawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Gestion de la navigation "Up" (retour) dans la barre d'outils
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigationdrawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}