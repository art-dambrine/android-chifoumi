package com.example.shifumi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GameMenu extends AppCompatActivity {

    private TextView bienvenueAvecNomJoueur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        // Declare UI components
        bienvenueAvecNomJoueur = findViewById(R.id.bienvenue_joueur);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String email = user.getEmail();
            String name = email.substring(0,email.indexOf("@"));

            bienvenueAvecNomJoueur.setText("Bienvenue " + name);

            // The user's ID, unique to the Firebase project.
            String uid = user.getUid();
        }

        // Button ouverture des regles
        Button button_menu_regles = findViewById(R.id.button_menu_regles);
        button_menu_regles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OpenRegles = new Intent(GameMenu.this, Regles.class);
                startActivity(OpenRegles);
            }
        });


        // Button choix difficulté
        Button button_difficulty = findViewById(R.id.button_menu_difficulté);
        button_difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OpenDifficulty = new Intent(GameMenu.this, Difficulty.class);
                startActivity(OpenDifficulty);
            }
        });

        // Button ouverture du classement
        Button button_classement = findViewById(R.id.button_menu_classement);
        button_classement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OpenClassement = new Intent(GameMenu.this, Classement.class);
                startActivity(OpenClassement);
            }
        });

        // Button lancement du jeu
        Button button_lancement_jeu = findViewById(R.id.button_menu_jouer);
        button_lancement_jeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OpenGame = new Intent(GameMenu.this, Game.class);
                startActivity(OpenGame);
            }
        });

        // Button deconnexion
        Button button_deconnexion = findViewById(R.id.button_menu_deconnexion);
        button_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
