package com.example.shifumi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Authentification extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.inscription_mail);
        passwordEditText = findViewById(R.id.connexion_password);
        passwordConfirmEditText = findViewById(R.id.password_confirm);

        // bouton create user
        Button button_inscription = findViewById(R.id.button_inscription);
        button_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordConfirm = passwordConfirmEditText.getText().toString();

                if (password.equals(passwordConfirm) && !password.equals("")) {
                    createUser(email, password);
                } else {
                    Toast.makeText(Authentification.this, "Saisissez le même mot de passe deux fois !", Toast.LENGTH_LONG).show();
                }

            }
        });


        // bouton activity_connexion
        Button button_go_connexion = findViewById(R.id.connect_redirection);
        button_go_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Revient à l'activité précédente (connexion)
                finish();
            }
        });

    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Authentification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASEDEBUG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // L'Intent data permet de renvoyer des informations à l'activité mère
                            final Intent data = new Intent();

                            data.putExtra("email", emailEditText.getText().toString());
                            setResult(RESULT_OK, data);

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FIREBASEDEBUG", "createUserWithEmail:failure", task.getException());

                            // updateUI(null);
                            Toast.makeText(Authentification.this, "Creation échouée : " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

}
