package com.example.jibi.ui.userdata;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jibi.NavigationdrawerActivity;
import com.example.jibi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserdataFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private String currentUserId;
    private TextView about;
    private TextView username;
    private TextView emailT;
    private ImageView profile_image;
    private Button btn_edit_profile;
    private ActivityResultLauncher<String> mTakePhoto;

    // SharedPreferences key for storing the image URI
    private static final String PREFS_NAME = "user_profile_prefs";
    private static final String KEY_PROFILE_IMAGE_URI = "profile_image_uri";

    public static UserdataFragment newInstance() {
        return new UserdataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userdata, container, false);

        // Initialize views
        about = view.findViewById(R.id.about_text);
        username = view.findViewById(R.id.user_name);
        emailT = view.findViewById(R.id.user_email);
        profile_image = view.findViewById(R.id.profile_image);
        btn_edit_profile = view.findViewById(R.id.btn_edit_profile);

        // Initialize Firebase and Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        currentUserId = currentUser.getUid();

        // Set up ActivityResultLauncher to pick an image
        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // Use Glide to set the selected image to the ImageView
                Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(profile_image);

                // Save the selected image URI in SharedPreferences
                SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, requireContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_PROFILE_IMAGE_URI, uri.toString());
                editor.apply();
                Intent intent = new Intent(getContext(), NavigationdrawerActivity.class);
                intent.putExtra("profile_image_uri", uri.toString());
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "No image selected!", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle button click to open the photo picker
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTakePhoto.launch("image/*");

            }
        });

        // Fetch user data from Firestore
        DocumentReference userDocRef = db.collection("Users").document(currentUserId);
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = documentSnapshot.getString("userName");
                String email = documentSnapshot.getString("email");
                about.setText("Welcome to your profile, " + userName + "! You are a key player in the Jibi project ecosystem. Here, you can track your tasks, set your goals, and monitor your progress. The more you use Jibi, the better you can manage your projects and team collaborations.");
                username.setText(userName);
                emailT.setText(email);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure (optional)
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the profile image URI from SharedPreferences when the fragment is recreated
//        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
//        String savedImageUriString = prefs.getString(KEY_PROFILE_IMAGE_URI, "");
//        if (!savedImageUriString.isEmpty()) {
//            Uri savedImageUri = Uri.parse(savedImageUriString);
//            profile_image.setImageURI(savedImageUri);
//        }
        // Retrieve the saved URI string from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, requireContext().MODE_PRIVATE);
        String savedImageUriString = prefs.getString(KEY_PROFILE_IMAGE_URI, "");

        if (!savedImageUriString.isEmpty()) {
            try {
                // Parse the URI and set it to the ImageView
                Uri savedImageUri = Uri.parse(savedImageUriString);
                Glide.with(this) // Replace with setImageURI for basic use
                        .load(savedImageUri)
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(profile_image);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error loading profile image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // If you want to use ViewModel, you can initialize it here
        // mViewModel = new ViewModelProvider(this).get(UserdataViewModel.class);
    }
}
