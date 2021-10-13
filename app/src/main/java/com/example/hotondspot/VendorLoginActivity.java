package com.example.hotondspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class VendorLoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);


        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(VendorLoginActivity.this, MapActivity.class);
                startActivity(intent);
                finish();

            }


        };


        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);


        mLogin = findViewById(R.id.Login);
        mRegistration = findViewById(R.id.Registration);

        mRegistration.setOnClickListener(v -> {

            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(VendorLoginActivity.this, task -> {
                if (!task.isSuccessful()) {


                    Toast.makeText(VendorLoginActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                } else {
                    String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Vendors").child(user_id);

                    current_user_db.setValue(true);

                }

            });

        });


        mLogin.setOnClickListener(v -> {
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(VendorLoginActivity.this, task -> {
                if (!task.isSuccessful()) {


                    Toast.makeText(VendorLoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();

                }
            });
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
}
