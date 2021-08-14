package com.videocall.datingapp.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.videocall.datingapp.Accounts.AccountsActivity;
import com.videocall.datingapp.Main.Application;
import com.videocall.datingapp.R;
import com.videocall.datingapp.Settings.SettingsActivity;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String currentUser;

    CircleImageView imageViewProfileTabUsernameImage;
    CircleImageView imageViewProfileTabSettingsImage;
    CircleImageView imageViewProfileTabAccountImage;
    CircleImageView imageViewProfileTabPrivacyImage;

    TextView textViewProfileTabUsernameText;
    TextView textViewProfileTabLocationText;
    TextView textViewProfileTabSettingsText;
    TextView textViewProfileTabAccountText;
    TextView textViewProfileTabPrivacyText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        imageViewProfileTabUsernameImage = view.findViewById(R.id.imageViewProfileTabUsernameImage);
        imageViewProfileTabSettingsImage = view.findViewById(R.id.imageViewProfileTabSettingsImage);
        imageViewProfileTabAccountImage = view.findViewById(R.id.imageViewProfileTabAccountImage);
        imageViewProfileTabPrivacyImage = view.findViewById(R.id.imageViewProfileTabPrivacyImage);

        textViewProfileTabUsernameText = view.findViewById(R.id.textViewProfileTabUsernameText);
        textViewProfileTabLocationText = view.findViewById(R.id.textViewProfileTabLocationText);
        textViewProfileTabSettingsText = view.findViewById(R.id.textViewProfileTabSettingsText);
        textViewProfileTabAccountText = view.findViewById(R.id.textViewProfileTabAccountText);
        textViewProfileTabPrivacyText = view.findViewById(R.id.textViewProfileTabPrivacyText);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            currentUser = firebaseUser.getUid();

            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot != null && documentSnapshot.exists()) {

                                String userImage = documentSnapshot.getString("user_image");

                                String user_name = documentSnapshot.getString("user_name");
                                String[] splitUserName = user_name.split(" ");
                                String user_birthage = documentSnapshot.getString("user_birthage");

                                textViewProfileTabUsernameText.setText(splitUserName[0] + ", " + user_birthage);

                                if (userImage.equals("image")) {
                                    imageViewProfileTabUsernameImage.setImageResource(R.drawable.profile_image);
                                } else {
                                    Picasso.get().load(userImage).into(imageViewProfileTabUsernameImage);
                                }

                                String user_city = documentSnapshot.getString("user_city");
                                String user_state = documentSnapshot.getString("user_state");
                                String user_country = documentSnapshot.getString("user_country");

                                if (documentSnapshot.getString("premium_user") != null) {
                                    String premium_user = documentSnapshot.getString("premium_user");
                                    saveSubscriptionDetails(premium_user);

                                    Log.e(">>TAG", "onEvent: 1"+ premium_user);
                                    Log.e(">>TAG", "onEvent: 2"+ Application.getSubscriptionDetails(getContext()));
                                }

//                                PrivacyProfile("30");


                                textViewProfileTabLocationText.setText(user_city + ", " + user_state + ", " + user_country);

                            }
                        }
                    });
        }



        imageViewProfileTabUsernameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user_uid", currentUser);
                startActivity(intent);
            }
        });


        imageViewProfileTabSettingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        imageViewProfileTabAccountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AccountsActivity.class);
                startActivity(intent);
            }
        });

        imageViewProfileTabPrivacyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });



        return view;


    }


    public void saveSubscriptionDetails(String premium_user) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putString("premium_user", premium_user);
        editor.apply();
    }

    private void setSubscriptionDetails(String premium_user_time) {


            Map<String, Object> mapUserProfile = new HashMap<>();
            mapUserProfile.put("premium_user", premium_user_time);
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .update(mapUserProfile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),
                                        "premium user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



    }

}
