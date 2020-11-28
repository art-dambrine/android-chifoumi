package com.example.shifumi;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Classement extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;

    private String userEmail;
    private String userUid;

    private Button classement_button_retour;
    private RadioButton classement_radio_difficulte_1;
    private RadioButton classement_radio_difficulte_2;
    private RadioButton classement_radio_difficulte_3;

    private ArrayList<ItemClassement> listItemClassement1;
    private ArrayList<ItemClassement> listItemClassement2;
    private ArrayList<ItemClassement> listItemClassement3;

    private ListView listView;

    private int choix_difficulty_classement = 1;


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_classement);

        db = FirebaseFirestore.getInstance();

        // Infos utilisateur
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            userUid = user.getUid();
        }

        // Recuperation des elements d'UI
        classement_button_retour = findViewById(R.id.classement_button_retour);
        classement_radio_difficulte_1 = findViewById(R.id.classement_radio_difficulte_1);
        classement_radio_difficulte_2 = findViewById(R.id.classement_radio_difficulte_2);
        classement_radio_difficulte_3 = findViewById(R.id.classement_radio_difficulte_3);

        // Set defaults :
        setDifficultyOnUI();

        // Set data :
        // Liste difficulte 1, 2, 3 :
        listItemClassement1 = new ArrayList<>();
        listItemClassement2 = new ArrayList<>();
        listItemClassement3 = new ArrayList<>();

        // Get data from firebase :

        db.collection("scores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int index = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FIREBASEDEBUG", document.getId() + " => " + document.getData());
                                try {
                                    JSONObject jsonData = new JSONObject(document.getData().toString());
                                    listItemClassement1.add(new ItemClassement(index, jsonData.getString("email"), jsonData.getString("name"), jsonData.getInt("score_1")));
                                    listItemClassement2.add(new ItemClassement(index, jsonData.getString("email"), jsonData.getString("name"), jsonData.getInt("score_2")));
                                    listItemClassement3.add(new ItemClassement(index, jsonData.getString("email"), jsonData.getString("name"), jsonData.getInt("score_3")));
                                    index++;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Collections.sort(listItemClassement1, new ComparateurItemClassement());
                                Collections.sort(listItemClassement2, new ComparateurItemClassement());
                                Collections.sort(listItemClassement3, new ComparateurItemClassement());

                                updateListViewUIbyDifficulty();
                            }
                        } else {
                            Log.w("FIREBASEDEBUG", "Error getting documents.", task.getException());
                        }
                    }
                });


        classement_radio_difficulte_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choix_difficulty_classement = 1;
                updateListViewUIbyDifficulty();
            }
        });
        classement_radio_difficulte_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choix_difficulty_classement = 2;
                updateListViewUIbyDifficulty();
            }
        });
        classement_radio_difficulte_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choix_difficulty_classement = 3;
                updateListViewUIbyDifficulty();
            }
        });

        classement_button_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void updateListViewUIbyDifficulty() {
        switch (choix_difficulty_classement) {
            case 1:
                setListView(listItemClassement1);
                break;
            case 2:
                setListView(listItemClassement2);
                break;
            case 3:
                setListView(listItemClassement3);
                break;
        }
    }

    private void setListView(ArrayList<ItemClassement> listItemClassement) {
        String[] positionArray = new String[listItemClassement.size()];
        String[] nameArray = new String[listItemClassement.size()];
        String[] infoArray = new String[listItemClassement.size()];

        int previousScore = 0;
        int position = 1;

        for (int i = 0; i < listItemClassement.size(); i++) {


            nameArray[i] = listItemClassement.get(i).getNom();
            infoArray[i] = "" + listItemClassement.get(i).getScore();

            if (i == 0)
                previousScore = listItemClassement.get(i).getScore();

            if (i != 0)
                previousScore = listItemClassement.get(i - 1).getScore();

            if (previousScore != listItemClassement.get(i).getScore())
                position++;

            positionArray[i] = "Pos. " + position;


        }

        CustomListAdapter whatever = new CustomListAdapter(this, positionArray, nameArray, infoArray);
        listView = (ListView) findViewById(R.id.classement_list);
        listView.setAdapter(whatever);
    }


    public void setDifficultyOnUI() {
        switch (choix_difficulty_classement) {
            case 1:
                classement_radio_difficulte_1.setChecked(true);
                break;
            case 2:
                classement_radio_difficulte_2.setChecked(true);
                break;
            case 3:
                classement_radio_difficulte_3.setChecked(true);
                break;
        }
    }


    private static class ItemClassement {

        // IN MAIN : Collections.sort(listItemClassement, new ComparateurItemClassement());

        private int id;
        private String email;
        private String nom;
        private int score;

        public ItemClassement(int id, String email, String nom, int score) {
            this.id = id;
            this.email = email;
            this.nom = nom;
            this.score = score;
        }

        @Override
        public String toString() {
            return "ItemClassement{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", nom='" + nom + '\'' +
                    ", score=" + score +
                    '}';
        }

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getNom() {
            return nom;
        }

        public int getScore() {
            return score;
        }
    }

    public static class ComparateurItemClassement implements Comparator<ItemClassement> {

        @Override
        public int compare(ItemClassement itemClassement, ItemClassement t1) {
            return Integer.compare(t1.getScore(), itemClassement.getScore());
        }
    }

}


