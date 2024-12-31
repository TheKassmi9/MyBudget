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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    LinearLayout btnSignUp;
    TextView toLogIn;
    LinearLayout btnGoogleSignIn;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        etUsername= findViewById(R.id.etUsername);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnSignUp=findViewById(R.id.btnSignUp);
        toLogIn=findViewById(R.id.toLogIn);
        //Firebase Auth
//        btnGoogleSignIn=findViewById(R.id.btnGoogleSignIn);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser= firebaseAuth.getCurrentUser();
                //check the user if logged in or not
                if(currentUser !=null){
                    //user Already logged in
                }else{
                    //the user signed out
                }
            }
        };
        toLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(etUsername.getText().toString())
                && !TextUtils.isEmpty(etEmail.getText().toString())
                && !TextUtils.isEmpty(etPassword.getText().toString())){
                    String email= etEmail.getText().toString().trim();
                    String password=etPassword.getText().toString().trim();
                    String username=etUsername.getText().toString().trim();
                    createUserEmailAccount(email,password,username);

                }
                else {
                    Toast.makeText(SignUpActivity.this, "Cannot Sign Up with empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createUserEmailAccount(String email,
                                        String password,
                                        String username){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(username)){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        initializeUserDocument(username, email);
                        Intent i= new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    else{

//                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Information", Snackbar.LENGTH_SHORT);
//
//// Get the Snackbar's view to customize it
//                        View snackbarView = snackbar.getView();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Information", Snackbar.LENGTH_SHORT);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(Color.RED); // Set the background color to red

// Customize the text color
                        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE); // Set the text color to white


// Set the Snackbar to the top
                        ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
                        if (params instanceof FrameLayout.LayoutParams) {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;
                            layoutParams.gravity = Gravity.TOP; // Position at the top
                            snackbarView.setLayoutParams(layoutParams);
                        }
                        TextView txtView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

// Customize the text appearance
                        txtView.setTextColor(Color.WHITE);
                        txtView.setTextSize(16); // Set font size
                        txtView.setTypeface(Typeface.DEFAULT_BOLD); // Make the text bold
//                        txtView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0); // Add an icon (left)
                        txtView.setCompoundDrawablePadding(16); // Add padding between text and icon

// Add padding to the Snackbar view
                        snackbarView.setPadding(24, 16, 24, 16); // Left, Top, Right, Bottom padding

// Optionally adjust Snackbar elevation for a modern look
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            snackbarView.setElevation(6f); // Add shadow effect
                        }
                        snackbar.show();

// Finally, show the Snackbar

                    }
                }
            });
        }
    }
    private void initializeUserDocument(String username, String email){
        currentUserId= firebaseAuth.getCurrentUser().getUid();
        DocumentReference userdoc= collectionReference.document(currentUserId);
        //check if the document exists
        userdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
//                    //Create the basic format of a User
//                    Map<String, Object> initialData= new HashMap<>();
//                    initialData.put("userName",username);
//                    initialData.put("email",email);
//                    initialData.put("budget",new Integer(0));
                    Income ic=new Income(new Timestamp(new Date()),"",0);
                    Spend sp= new Spend(new Timestamp(new Date()), "",0);
                    UserAccount initData=new UserAccount(0,email,username,0);

                    userdoc.collection("income").add(ic)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(SignUpActivity.this, "Income collection initialized.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "Firestore Failed to initialize income collection", Toast.LENGTH_SHORT).show();
                                }
                            });
                    userdoc.collection("spend").add(sp).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(SignUpActivity.this, "spends collection initialized.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Firestore Failed to initialize income collection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    userdoc.set(initData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SignUpActivity.this, "User journal document initialized", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Error initializing user document", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}