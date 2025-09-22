package com.farmmonitor.agriai;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in, go to HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // User is not signed in, go to StartingActivity
            startActivity(new Intent(this, com.farmmonitor.agriai.StartingActivity.class));
        }

        // Finish this activity so user can’t come back here
        finish();
    }
}
