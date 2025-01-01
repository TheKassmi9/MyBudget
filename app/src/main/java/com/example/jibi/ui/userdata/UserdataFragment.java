package com.example.jibi.ui.userdata;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.android.gms.tasks.Tasks;
import com.example.jibi.R;
import com.example.jibi.TraitementUserdata;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserdataFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    //    private CollectionReference collectionReference=db.collection("Users");
    private String currentUserId;
    TextView about;
    TextView username;
    TextView emailT;
    private UserdataViewModel mViewModel;

    public static UserdataFragment newInstance() {
        return new UserdataFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_userdata, container, false);

//        Button btn=view.findViewById(R.id.button_validate_userdata);
//        EditText value_input_username=view.findViewById(R.id.editTextUsername);
//        EditText value_input_password=view.findViewById(R.id.editTextPassword);
//        EditText value_input_email=view.findViewById(R.id.editTextEmail);
//        EditText value_input_datebirth=view.findViewById(R.id.editTextDateBirth);


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String string_value_username = value_input_username.getText().toString();
//                String string_value_password = value_input_password.getText().toString();
//                String string_value_email = value_input_email.getText().toString();
//                String string_value_datebirth = value_input_datebirth.getText().toString();

              /*  if (string_value_username.isEmpty() || string_value_password.isEmpty()) {
                    // Optionnel : avertir l'utilisateur si le champ est vide
                    value_input_username.setError("verifi 7wayjk !");
                    value_input_password.setError("jri 3la ROU7K");
                    return;
                }

               */

                // Démarrer la nouvelle activité avec l'Intent
//                Intent intent = new Intent(getContext(), TraitementUserdata.class);
//                intent.putExtra("username_value_edit", string_value_username); // Ajouter l'information à l'Intent
//                intent.putExtra("password_value_edit",string_value_password);
//                intent.putExtra("email_value_edit", string_value_email); // Ajouter l'information à l'Intent
//                intent.putExtra("datebirth_value_edit",string_value_datebirth);
//                startActivity(intent);
//            }
//        });

        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //database
        db= FirebaseFirestore.getInstance();
        // Initialize Firestore and fetch data
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        currentUserId=currentUser.getUid();
        about= view.findViewById(R.id.about_text);
        username =view.findViewById(R.id.user_name);
        emailT = view.findViewById(R.id.user_email);
        DocumentReference userDocRef = db.collection("Users").document(currentUserId);

        //fetching DAta from firestore
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName=documentSnapshot.getString("userName");
                String email = documentSnapshot.getString("email");
                about.setText("Welcome to your profile, "+userName+"! You are a key player in the Jibi project ecosystem. Here, you can track your tasks, set your goals, and monitor your progress. The more you use Jibi, the better you can manage your projects and team collaborations.");
                username.setText(userName);
                emailT.setText(email);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserdataViewModel.class);
        // TODO: Use the ViewModel
    }

}