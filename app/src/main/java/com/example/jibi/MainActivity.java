package com.example.jibi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    TextView text_signup;
    EditText editTextUsername;
    EditText editTextPassword;
    Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.input_username);
         editTextPassword = findViewById(R.id.input_password);
         btnLogin = findViewById(R.id.button_send);
        text_signup=findViewById(R.id.text_signup);
        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        //Firebase Authentication
        firebaseAuth= FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Sauvegarder les données utilisateur dans SharedPreferences
                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("username", username)
                        .putString("password", password)
                        .apply();
                loginEmailPassUser(
                        editTextUsername.getText().toString().trim(),
                        editTextPassword.getText().toString().trim()
                );
            }
        });
//        btnLogin.setOnClickListener(v -> {
//            String username = editTextUsername.getText().toString();
//            String password = editTextPassword.getText().toString();
//
//            // Sauvegarder les données utilisateur dans SharedPreferences
//            getSharedPreferences("UserPrefs", MODE_PRIVATE)
//                    .edit()
//                    .putString("username", username)
//                    .putString("password", password)
//                    .apply();
//
////            // Démarrer NavigationdrawerActivity
////            Intent intent = new Intent(MainActivity.this, NavigationdrawerActivity.class);
////            startActivity(intent);
//        });
    }
    private void loginEmailPassUser(String email, String password){
        //checking for empty texts
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You Logged in successfully!", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Optional: Handle click on the action button
                                }
                            });

                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.GREEN); // Set the background color to green (success)

// Customize the text color and appearance
                    TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE); // Set the text color to white
                    textView.setTextSize(16); // Set font size
                    textView.setTypeface(Typeface.DEFAULT_BOLD); // Make the text bold
                    textView.setCompoundDrawablePadding(16); // Add padding between text and any icon

// Set the Snackbar to the top of the screen
                    ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
                    if (params instanceof FrameLayout.LayoutParams) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;
                        layoutParams.gravity = Gravity.TOP; // Position the Snackbar at the top
                        snackbarView.setLayoutParams(layoutParams);
                    }

// Add padding to the Snackbar view
                    snackbarView.setPadding(24, 16, 24, 16); // Left, Top, Right, Bottom padding

// Optionally adjust Snackbar elevation for a modern look
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        snackbarView.setElevation(6f); // Add shadow effect
                    }

// Show the Snackbar
                    snackbar.show();

                    Intent intent = new Intent(MainActivity.this, NavigationdrawerActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Could not login verify your Email or Password", Toast.LENGTH_SHORT).show();
                    editTextUsername.setText("");
                    editTextPassword.setText("");
                }
            });
        }
        else{
            Toast.makeText(this, "!!Cannot Log in with empty fields...", Toast.LENGTH_SHORT).show();
        }
    }
}