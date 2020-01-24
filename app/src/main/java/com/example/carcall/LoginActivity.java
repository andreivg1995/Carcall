package com.example.carcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText email, pass;
    Button login, registro;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        registro = findViewById(R.id.register_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //LOGIN
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //REGISTRO
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(pass.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(email.getText().toString(), pass.getText().toString());
                }
            }
        });
    }

    private void createAccount(String email, String password){
        Log.i("@@@@@@@@@@@@@@@@@@@@@", email);
        Log.i("@@@@@@@@@@@@@@@@@@@@@", password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("usuarios").child(user.getUid());

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", user.getUid());
                            hashMap.put("email", user.getEmail());

                            databaseReference.setValue(hashMap);

                            /*
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //Intent intent = new Intent
                                    }
                                }
                            });
                             */
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
