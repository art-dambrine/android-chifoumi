package com.example.shifumi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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


public class Game extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;

    private String userEmail;
    private String userUid;

    public static int spock = 991;
    public static int ciseaux = 992;
    public static int lezard = 993;
    public static int papier = 994;
    public static int pierre = 995;

    private Boolean roundFinished = false;

    private int score = 0;
    private int meilleur_score;
    private int difficulty = 0;

    private int best_score_1;
    private int best_score_2;
    private int best_score_3;


    private int choix_player = 0;
    private int choix_adversaire = 0;

    private Button button_play;
    private Button button_quit;

    private TextView display_score;
    private TextView display_difficulty;
    private TextView display_meilleur_score;

    private ImageView img_button_spock;
    private ImageView img_button_ciseaux;
    private ImageView img_button_lezard;
    private ImageView img_button_papier;
    private ImageView img_button_pierre;

    private ImageView img_player;
    private ImageView img_adversaire;
    private ImageView img_choix_player;
    private ImageView img_choix_adversaire;

    public static final int VALIDATION_PREMIER_CHOIX_DIFFICULTE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        // Infos utilisateur
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            userUid = user.getUid();
        }

        // Recuperation des elements d'UI
        display_score = findViewById(R.id.game_display_score);
        display_difficulty = findViewById(R.id.game_difficulte_niveau);
        display_meilleur_score = findViewById(R.id.game_display_best_score);

        button_play = findViewById(R.id.button_game_valider);
        button_quit = findViewById(R.id.button_stop);

        img_button_spock = findViewById(R.id.game_spock);
        img_button_ciseaux = findViewById(R.id.game_cisor);
        img_button_lezard = findViewById(R.id.game_lizzard);
        img_button_papier = findViewById(R.id.game_paper);
        img_button_pierre = findViewById(R.id.game_rock);

        img_player = findViewById(R.id.game_img_player);
        img_adversaire = findViewById(R.id.game_img_adversaire);
        img_choix_player = findViewById(R.id.game_img_choix_player);
        img_choix_adversaire = findViewById(R.id.game_img_choix_adversaire);

        // Mise en place des valeurs par defaut
        setDefaultUIstate();
        importDifficultyFromDatabase();

        img_button_spock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!roundFinished) {
                    setDefaultButtonsState();
                    img_button_spock.setAlpha((float) 1);
                    img_choix_player.setImageResource(R.drawable.button_spock);
                    choix_player = spock;
                }
            }
        });

        img_button_ciseaux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!roundFinished) {
                    setDefaultButtonsState();
                    img_button_ciseaux.setAlpha((float) 1);
                    img_choix_player.setImageResource(R.drawable.button_cisor);
                    choix_player = ciseaux;
                }
            }
        });

        img_button_lezard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!roundFinished) {
                    setDefaultButtonsState();
                    img_button_lezard.setAlpha((float) 1);
                    img_choix_player.setImageResource(R.drawable.button_lizzard);
                    choix_player = lezard;
                }
            }
        });

        img_button_papier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!roundFinished) {
                    setDefaultButtonsState();
                    img_button_papier.setAlpha((float) 1);
                    img_choix_player.setImageResource(R.drawable.button_paper);
                    choix_player = papier;
                }
            }
        });

        img_button_pierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!roundFinished) {
                    setDefaultButtonsState();
                    img_button_pierre.setAlpha((float) 1);
                    img_choix_player.setImageResource(R.drawable.button_rock);
                    choix_player = pierre;
                }
            }
        });


        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!roundFinished) {
                    // Debut du round
                    int resultat_du_combat;

                    resultat_du_combat = deroulementCombat(difficulty, choix_player);

                    if (resultat_du_combat == 1) {

                        // Victoire du joueur affichage du choix_joueur et du choix_adversaire
                        setEndOfRoundUIstate(resultat_du_combat, choix_player, choix_adversaire);
                        // On incrémente le score du joueur
                        score++;

                        Log.d("logcombat", "Choix adversaire : " + Utilitaires.choixToString(choix_adversaire) + " , Issue du combat : victoire player");
                    }

                    if (resultat_du_combat == 0) {

                        // Defaite du joueur affichage du choix_joueur et du choix_adversaire
                        setEndOfRoundUIstate(resultat_du_combat, choix_player, choix_adversaire);

                        // Remise à zero du score joueur
                        score = 0;

                        Log.d("logcombat", "Choix adversaire : " + Utilitaires.choixToString(choix_adversaire) + " , Issue du combat : victoire adversaire");
                    }

                    if (resultat_du_combat == 2) {

                        // Affichage de l'égalité et recommencer la selection par le joueur
                        setEndOfRoundUIstate(resultat_du_combat, choix_player, choix_adversaire);

                        Log.d("logcombat", "Choix adversaire : " + Utilitaires.choixToString(choix_adversaire) + "Issue du combat : égalité");
                    }

                    if (resultat_du_combat == -1) {
                        Log.d("logcombat", "onClick de play : erreur resultat combat");
                    }


                    // TODO : FIN du round - enregistrer score en base etc... (Mise à jour du meilleur score du joueur)
                    display_score.setText("" + score + "");
                    updateMeilleurScore();
                    button_play.setText("Rejouer");
                    roundFinished = true;

                } else {

                    // Remise en place des defaults
                    setDefaultUIstate();
                    choix_player = 0;
                    choix_adversaire = 0;
                    button_play.setText("Jouer");
                    roundFinished = false;
                }
            }

        });


        button_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void updateMeilleurScore() {
        if (score > meilleur_score) {
            meilleur_score = score;
            display_meilleur_score.setText("" + meilleur_score + "");
            // TODO UPDATE MEILLEUR SCORE EN BASE
            updateMeilleurScoreDatabase();
        }
    }

    private void importScoresFromDatabase() {

        DocumentReference docRef = db.collection("scores").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("FIREBASEDEBUG", "DocumentSnapshot data: " + document.getData());
                        try {
                            JSONObject jsonData = new JSONObject(document.getData().toString());
                            try {
                                best_score_1 = jsonData.getInt("score_1");
                            } catch (Exception e) {
                                best_score_1 = 0;
                            }

                            try {
                                best_score_2 = jsonData.getInt("score_2");
                            } catch (Exception e) {
                                best_score_2 = 0;
                            }

                            try {
                                best_score_3 = jsonData.getInt("score_3");
                            } catch (Exception e) {
                                best_score_3 = 0;
                            }

                            Log.d("FIREBASEDEBUG", "onComplete: " + jsonData.toString());
                            switch (difficulty) {
                                case 1:
                                    meilleur_score = best_score_1;
                                    display_meilleur_score.setText("" + meilleur_score + "");
                                    break;
                                case 2:
                                    meilleur_score = best_score_2;
                                    display_meilleur_score.setText("" + meilleur_score + "");
                                    break;
                                case 3:
                                    meilleur_score = best_score_3;
                                    display_meilleur_score.setText("" + meilleur_score + "");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("FIREBASEDEBUG", "No such document");
                        /*choix_difficulty = 1;
                        updateUIRadiobuttons(choix_difficulty);*/
                    }
                } else {
                    Log.d("FIREBASEDEBUG", "get failed with ", task.getException());
                    /*choix_difficulty = 1;
                    updateUIRadiobuttons(choix_difficulty);*/
                }
            }
        });
    }

    private void updateMeilleurScoreDatabase() {
        Map<String, Object> bestScore = new HashMap<>();
        bestScore.put("email", userEmail);
        bestScore.put("name", userEmail.substring(0, userEmail.indexOf("@")));

        switch (difficulty) {
            case 1:
                bestScore.put("score_1", meilleur_score);
                bestScore.put("score_2", best_score_2);
                bestScore.put("score_3", best_score_3);
                break;
            case 2:
                bestScore.put("score_1", best_score_1);
                bestScore.put("score_2", meilleur_score);
                bestScore.put("score_3", best_score_3);
                break;
            case 3:
                bestScore.put("score_1", best_score_1);
                bestScore.put("score_2", best_score_2);
                bestScore.put("score_3", meilleur_score);
                break;
        }

        db.collection("scores").document(userUid)
                .set(bestScore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASEDEBUG", "Score enregistré !");
                        Toast.makeText(Game.this, "Nouveau meilleur score enregistré !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASEDEBUG", "Error writing document", e);
                        Toast.makeText(Game.this, "Error writing document", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private int deroulementCombat(int difficulty, int choix_joueur) {
        /* Pour le jeu 3 modes de difficultés sont possibles :
         * - niveau 1 - tirage aléatoire total random
         * - niveau 2 - tirage aléatoire avec 30% de victoire IA et 10% egalite
         * - niveau 3 - tirage aléatoire avec 60% de victoire IA et 5% egalite
         *
         * */

        // Déroulement du combat :
        int choix_adversaire = 0;
        int issue_predefinie;
        int resultat_du_combat = -1;

        // Pour le mode de difficulté 1
        if (difficulty == 1) {
            // On détermine en avance l'issue du combat :
            choix_adversaire = Utilitaires.choixDeMainAleatoire();

            // On détermine le gagnant
            resultat_du_combat = Utilitaires.verifReglesSiChoixJoueurGagne(choix_joueur, choix_adversaire);
        }

        // Pour les modes de difficultés 2 et 3
        if (difficulty == 2 || difficulty == 3) {
            // On détermine en avance l'issue du combat :
            issue_predefinie = Utilitaires.tirageIA(difficulty);

            while (true) {
                // On fait ensuite un tirage aléatoire :
                choix_adversaire = Utilitaires.choixDeMainAleatoire();

                // On vérifie si l'issue du combat est en adéquation avec le tirage
                resultat_du_combat = Utilitaires.verifReglesSiChoixJoueurGagne(choix_joueur, choix_adversaire);

                if (issue_predefinie == resultat_du_combat)
                    break;

            }
        }

        Game.this.choix_adversaire = choix_adversaire;

        return resultat_du_combat;

    }

    private void setDefaultButtonsState() {
        img_button_spock.setAlpha((float) 0.5);
        img_button_ciseaux.setAlpha((float) 0.5);
        img_button_lezard.setAlpha((float) 0.5);
        img_button_papier.setAlpha((float) 0.5);
        img_button_pierre.setAlpha((float) 0.5);
    }

    private void setDefaultUIstate() {
        // Mise en place des defaults
        setDefaultButtonsState();
        img_player.setImageResource(R.drawable.player_win);
        img_adversaire.setImageResource(R.drawable.adversaire_win);
        img_choix_player.setImageResource(R.drawable.question);
        img_choix_adversaire.setImageResource(R.drawable.question);
        img_choix_player.setForeground(null);
        img_choix_adversaire.setForeground(null);
    }

    private void setEndOfRoundUIstate(int resultat_du_combat, int choix_player, int choix_adversaire) {

        // Affichage du choix du joueur
        img_choix_player.setAlpha((float) 1);

        if (choix_player == spock) {
            img_choix_player.setImageResource(R.drawable.button_spock);
        }
        if (choix_player == ciseaux) {
            img_choix_player.setImageResource(R.drawable.button_cisor);
        }
        if (choix_player == lezard) {
            img_choix_player.setImageResource(R.drawable.button_lizzard);
        }
        if (choix_player == papier) {
            img_choix_player.setImageResource(R.drawable.button_paper);
        }
        if (choix_player == pierre) {
            img_choix_player.setImageResource(R.drawable.button_rock);
        }


        // Affichage du choix de l'adversaire
        if (choix_adversaire == spock) {
            img_choix_adversaire.setImageResource(R.drawable.button_spock);
        }
        if (choix_adversaire == ciseaux) {
            img_choix_adversaire.setImageResource(R.drawable.button_cisor);
        }
        if (choix_adversaire == lezard) {
            img_choix_adversaire.setImageResource(R.drawable.button_lizzard);
        }
        if (choix_adversaire == papier) {
            img_choix_adversaire.setImageResource(R.drawable.button_paper);
        }
        if (choix_adversaire == pierre) {
            img_choix_adversaire.setImageResource(R.drawable.button_rock);
        }

        // Victoire
        if (resultat_du_combat == 1) {
            img_player.setImageResource(R.drawable.player_win);
            img_adversaire.setImageResource(R.drawable.adversaire_lose);
            img_choix_player.setForeground(getDrawable(R.drawable.overlay_win));
            img_choix_adversaire.setForeground(getDrawable(R.drawable.overlay_lose));
        }

        // Defaite
        if (resultat_du_combat == 0) {
            img_player.setImageResource(R.drawable.player_lose);
            img_adversaire.setImageResource(R.drawable.adversaire_win);
            img_choix_player.setForeground(getDrawable(R.drawable.overlay_lose));
            img_choix_adversaire.setForeground(getDrawable(R.drawable.overlay_win));
        }

        // Egalite
        if (resultat_du_combat == 2) {
            img_player.setImageResource(R.drawable.player_lose);
            img_adversaire.setImageResource(R.drawable.adversaire_lose);
            img_choix_player.setForeground(getDrawable(R.drawable.overlay_equality));
            img_choix_adversaire.setForeground(getDrawable(R.drawable.overlay_equality));
        }
    }

    private void importDifficultyFromDatabase() {


        // Si la difficulté n'est pas encore selectionnée
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
                            difficulty = jsonData.getInt("difficulty");
                            updateDifficultyOnUI(difficulty);

                            // Si on a bien récupéré la difficulté on recupère le meilleur score de la base
                            importScoresFromDatabase();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("FIREBASEDEBUG", "No such document");
                        difficulty = 1;
                        updateDifficultyOnUI(difficulty);
                        // Si on a pas récupéré la difficulté on demande de la choisir
                        Intent OpenDifficulty = new Intent(Game.this, Difficulty.class);
                        startActivityForResult(OpenDifficulty, VALIDATION_PREMIER_CHOIX_DIFFICULTE);

                    }
                } else {
                    Log.d("FIREBASEDEBUG", "get failed with ", task.getException());
                    difficulty = 1;
                    updateDifficultyOnUI(difficulty);
                    finish();
                }
            }
        });

    }

    private void updateDifficultyOnUI(int difficulty) {
        display_difficulty.setText("" + difficulty + "");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VALIDATION_PREMIER_CHOIX_DIFFICULTE) {
            if (data.hasExtra("choix_difficulty")) {

                difficulty = data.getExtras().getInt("choix_difficulty");
                display_difficulty.setText("" + difficulty + "");

                meilleur_score = 0;
                display_meilleur_score.setText("" + meilleur_score + "");

                setDefaultUIstate();
                importScoresFromDatabase();
            }
        }
    }
}
