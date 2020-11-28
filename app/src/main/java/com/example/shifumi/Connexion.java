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

public class Connexion extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;

    public static final int VALIDATION_INSCRIPTION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Declare UI components
        emailEditText = findViewById(R.id.connexion_login);
        passwordEditText = findViewById(R.id.connexion_password);

        Button button_connect = findViewById(R.id.button_inscription);
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Log.d("FIREBASEDEBUG", "onClick: input : " + email + " " + password);

                // On verifie si le champs n'est pas vide
                if(!email.equals("") && !password.equals(""))
                    connectUser(email, password);
                else Toast.makeText(Connexion.this, "Veuillez remplir les deux champs.", Toast.LENGTH_SHORT).show();
            }
        });

        Button button_pas_inscrit = findViewById(R.id.connexion_pas_inscrit_button);
        button_pas_inscrit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OpenInscription = new Intent(Connexion.this, Authentification.class);
                startActivityForResult(OpenInscription, VALIDATION_INSCRIPTION);
            }
        });

    }

    private void connectUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Connexion.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASEDEBUG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent OpenMenuGame = new Intent(Connexion.this, GameMenu.class);
                            startActivity(OpenMenuGame);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FIREBASEDEBUG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Connexion.this, "Authentication failed : " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            // updateUI(null);
                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // updateUI(currentUser);
        try {
            Log.d("FIREBASEDEBUG", "onStart: " + currentUser.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VALIDATION_INSCRIPTION) {
            if (data.hasExtra("email")) {
                if (!data.getExtras().getString("email").isEmpty()) {
                    Toast.makeText(this, "Inscription validée pour : " + data.getExtras().getString("email"), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
