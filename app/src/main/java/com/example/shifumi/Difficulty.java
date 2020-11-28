package com.example.shifumi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Difficulty extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;

    RadioButton radio_button_difficulty_1;
    RadioButton radio_button_difficulty_2;
    RadioButton radio_button_difficulty_3;
    Button button_validation_difficulty;

    private int choix_difficulty;

    private String userEmail;
    private String userUid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        // Declare UI components
        radio_button_difficulty_1 = findViewById(R.id.radio_button_difficulty_1);
        radio_button_difficulty_2 = findViewById(R.id.radio_button_difficulty_2);
        radio_button_difficulty_3 = findViewById(R.id.radio_button_difficulty_3);
        button_validation_difficulty = findViewById(R.id.button_validation_difficulty);

        // GET db access
        db = FirebaseFirestore.getInstance();

        // GET current uid
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // email address
            userEmail = user.getEmail();
            // The user's ID, unique to the Firebase project.
            userUid = user.getUid();
        }

        // TODO : get difficulty from database
        importDifficultyFromDatabase();

        // Mettre le bon radiobutton préséléctionné
        updateUIRadiobuttons(choix_difficulty);


        radio_button_difficulty_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choix_difficulty = 1;
                updateUIRadiobuttons(choix_difficulty);
            }
        });
        radio_button_difficulty_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choix_difficulty = 2;
                updateUIRadiobuttons(choix_difficulty);
            }
        });
        radio_button_difficulty_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choix_difficulty = 3;
                updateUIRadiobuttons(choix_difficulty);
            }
        });


        button_validation_difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDifficultyInDatabase();
            }
        });


    }

    private void setDifficultyInDatabase() {

        Map<String, Object> difficulty = new HashMap<>();
        difficulty.put("email", userEmail);
        difficulty.put("name", userEmail.substring(0, userEmail.indexOf("@")));
        difficulty.put("difficulty", choix_difficulty);

        db.collection("difficulties").document(userUid)
                .set(difficulty)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASEDEBUG", "Difficulté enregistrée !");

                        // L'Intent data permet de renvoyer des informations à l'activité mère
                        final Intent data = new Intent();

                        data.putExtra("choix_difficulty", choix_difficulty);
                        setResult(RESULT_OK, data);

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASEDEBUG", "Error writing document", e);
                        Toast.makeText(Difficulty.this, "Error writing document", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateUIRadiobuttons(int choix_difficulty) {
        switch (choix_difficulty) {
            case 1:
                radio_button_difficulty_1.setChecked(true);
                radio_button_difficulty_2.setChecked(false);
                radio_button_difficulty_3.setChecked(false);
                break;
            case 2:
                radio_button_difficulty_1.setChecked(false);
                radio_button_difficulty_2.setChecked(true);
                radio_button_difficulty_3.setChecked(false);
                break;
            case 3:
                radio_button_difficulty_1.setChecked(false);
                radio_button_difficulty_2.setChecked(false);
                radio_button_difficulty_3.setChecked(true);
                break;
        }
    }

    private void importDifficultyFromDatabase() {

        DocumentReference docRef = db.collection("difficulties").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("FIREBASEDEBUG", "DocumentSnapshot data: " + document.getData());
                        try {
                            JSONObject jsonData = new JSONObject(document.getData().toString());
                            choix_difficulty = jsonData.getInt("difficulty");
                            Log.d("JSONDATA", "onComplete: " + choix_difficulty);
                            updateUIRadiobuttons(choix_difficulty);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("FIREBASEDEBUG", "No such document");
                        choix_difficulty = 1;
                        updateUIRadiobuttons(choix_difficulty);
                    }
                } else {
                    Log.d("FIREBASEDEBUG", "get failed with ", task.getException());
                    choix_difficulty = 1;
                    updateUIRadiobuttons(choix_difficulty);
                }
            }
        });
    }
}
