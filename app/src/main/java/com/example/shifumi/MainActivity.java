package com.example.shifumi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = findViewById(R.id.main_connexion);
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OpenConnexion=new Intent(MainActivity.this, Connexion.class);
                startActivity(OpenConnexion);
            }
        });

    }


    // Bouton pour ouvrir les règles
    public void openRegles(View view) {
        Intent OpenRegles=new Intent(MainActivity.this, Regles.class);
        startActivity(OpenRegles);
    }
}