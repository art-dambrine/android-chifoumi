package com.example.shifumi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Utilitaires {

    public static int spock = 991;
    public static int ciseaux = 992;
    public static int lezard = 993;
    public static int papier = 994;
    public static int pierre = 995;

    // Lancer avec "Code Coverage" pour tester :
    public static void main(String[] args) {

        // Si on run cette classe en code coverage on a la vérification de config du coeur de notre jeu :

        String choix;
        do {

            System.out.println("1.spock\n" +
                    "2.ciseaux\n" +
                    "3.lezard\n" +
                    "4.papier\n" +
                    "5.pierre\n" +
                    "0.quitter\n");

            choix = readStringFromUser();

            if ((Integer.parseInt(choix) != 0) && Integer.parseInt(choix) <= 5 && Integer.parseInt(choix) > 0) {
                int choix_joueur = -1;
                // Printing the read line
                if (Integer.parseInt(choix) == 1) choix_joueur = spock;
                if (Integer.parseInt(choix) == 2) choix_joueur = ciseaux;
                if (Integer.parseInt(choix) == 3) choix_joueur = lezard;
                if (Integer.parseInt(choix) == 4) choix_joueur = papier;
                if (Integer.parseInt(choix) == 5) choix_joueur = pierre;
                if (choix_joueur == -1) break;

                System.out.println("Votre choix :" + choix);

                // Paramètre d'entrée d'un combat :
                int difficulty = 3;


                System.out.println("Test de difficulté niveau " + difficulty);
                for (int i = 0; i < 10; i++) {
                    System.out.println("N " + i);
                    deroulementCombatConsole(difficulty, choix_joueur);
                    System.out.println("===");
                }

                Utilitaires.testDeTirageAleatoire();
                Utilitaires.testDifficulteNiveau2();
                Utilitaires.testDifficulteNiveau3();
            }


        } while (!choix.equals("0"));


    }

    private static void deroulementCombatConsole(int difficulty, int choix_joueur) {
        // Déroulement du combat :
        int choix_adversaire = 0;
        int issue_predefinie = 0;
        int resultat_du_combat = 0;

        // Pour les modes de difficultés 1 et 2
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


        System.out.println("Choix joueur : " + Utilitaires.choixToString(choix_joueur));
        System.out.println("Choix adversaire : " + Utilitaires.choixToString(choix_adversaire));

        if (issue_predefinie == 1) {
            // TODO : Victoire du joueur affichage du choix_joueur et du choix_adversaire
            // TODO : incrémentation du score joueur
            // TODO : Mise à jour du meilleur score du joueur
            System.out.println("Issue predefinie (victoire joueur)");
        }

        if (issue_predefinie == 0) {
            // TODO : victoire de l'IA joueur affichage du choix_joueur et du choix_adversaire
            // TODO : remise à 0 du score joueur
            System.out.println("Issue predefinie (victoire IA)");
        }

        if (issue_predefinie == 2) {
            // TODO : Afficher égalité et recommencer la selection par le joueur
            System.out.println("Egalité");
        }

    }

    public static String choixToString(int choix) {
        if (choix == 991) return "spock";
        if (choix == 992) return "ciseaux";
        if (choix == 993) return "lezard";
        if (choix == 994) return "papier";
        if (choix == 995) return "pierre";
        return "erreur choixToString";
    }

    public static int verifReglesSiChoixJoueurGagne(int choix_joueur, int choix_adversaire) {

        /*
         *   verifReglesSiChoixJoueurGagne retourne :
         *       - '1' si le choix_joueur gagne
         *       - '0' si le choix_joueur perd
         *       - '2' si égalité
         *       et  '-1' si il y une erreur
         * */

        if (choix_joueur == spock) {

            if (choix_adversaire == ciseaux)
                return 1; // choix_joueur gagne

            if (choix_adversaire == pierre)
                return 1; // choix_joueur gagne

            if (choix_adversaire == lezard)
                return 0; // choix_joueur perd

            if (choix_adversaire == papier)
                return 0; // choix_joueur perd

            if (choix_adversaire == spock)
                return 2; // egalité

        }

        if (choix_joueur == ciseaux) {
            if (choix_adversaire == papier)
                return 1; // choix_joueur gagne

            if (choix_adversaire == lezard)
                return 1; // choix_joueur gagne

            if (choix_adversaire == pierre)
                return 0; // choix_joueur perd

            if (choix_adversaire == spock)
                return 0; // choix_joueur perd

            if (choix_adversaire == ciseaux)
                return 2; // egalité
        }

        if (choix_joueur == lezard) {
            if (choix_adversaire == spock)
                return 1; // choix_joueur gagne

            if (choix_adversaire == papier)
                return 1; // choix_joueur gagne

            if (choix_adversaire == pierre)
                return 0; // choix_joueur perd

            if (choix_adversaire == ciseaux)
                return 0; // choix_joueur perd

            if (choix_adversaire == lezard)
                return 2; // egalité
        }

        if (choix_joueur == papier) {
            if (choix_adversaire == pierre)
                return 1; // choix_joueur gagne

            if (choix_adversaire == spock)
                return 1; // choix_joueur gagne

            if (choix_adversaire == ciseaux)
                return 0; // choix_joueur perd

            if (choix_adversaire == lezard)
                return 0; // choix_joueur perd

            if (choix_adversaire == papier)
                return 2; // egalité
        }

        if (choix_joueur == pierre) {
            if (choix_adversaire == ciseaux)
                return 1; // choix_joueur gagne

            if (choix_adversaire == lezard)
                return 1; // choix_joueur gagne

            if (choix_adversaire == papier)
                return 0; // choix_joueur perd

            if (choix_adversaire == spock)
                return 0; // choix_joueur perd

            if (choix_adversaire == pierre)
                return 2; // egalité
        }

        return -1; // erreur
    }

    public static int choixDeMainAleatoire() {
        // un nombre aléatoire entre spock et pierre
        return getRandomNumberInRange(991, 995);
    }

    public static int tirageIA(int difficulty) {

        /*
         *  retourne le choix de l'IA
         *
         *  3 niveaux de difficulté :
         *
         *      niveau 1 - tirage aléatoire total random
         *      niveau 2 - tirage aléatoire avec 30% de victoire IA et 10% egalite
         *      niveau 3 - tirage aléatoire avec 60% de victoire IA et 5% egalite
         * */

        if (difficulty == 2) {
            // return qui gagne, 1 joueur, 0 IA, 2 egalite:
            //    niveau 2 - tirage aléatoire avec 55% de victoire joueur et 15% d'égalité

            int chance_victoire_joueur = 55; // %
            int chance_egalite = 15; // %

            // Premier tirage pour l'egalité chance_egalite %
            int result = getRandomNumberInRange(0, 100);
            if (result > (100 - chance_egalite))
                return 2; // egalite

            // deuxième tirage pour la victoire joueur chance_victoire_joueur %
            if (result < chance_victoire_joueur)
                return 1; // joueur gagne


            // Si le joueur ne gagne pas ou si il n'y a pas d'égalité
            return 0; // IA gagne
        }

        if (difficulty == 3) {
            // return qui gagne, 1 joueur, 0 IA, 2 egalite:
            //    niveau 3 - tirage aléatoire avec 40% de victoire joueur

            int chance_victoire_joueur = 40; // %
            int chance_egalite = 10; // %

            // Premier tirage pour l'egalité chance_egalite %
            int result = getRandomNumberInRange(0, 100);
            if (result > (100 - chance_egalite))
                return 2; // egalite

            // deuxième tirage pour la victoire joueur chance_victoire_joueur %
            if (result < chance_victoire_joueur)
                return 1; // joueur gagne


            // Si le joueur ne gagne pas ou si il n'y a pas d'égalité
            return 0; // IA gagne
        }

        return -1; // error
    }

    private static String readStringFromUser() {
        //Enter data using BufferReader
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String name = null;
        try {
            name = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void testDeTirageAleatoire() {
        int count_spock = 0;
        int count_ciseaux = 0;
        int count_lezard = 0;
        int count_papier = 0;
        int count_pierre = 0;

        int nbTirage = 1000000;

        for (int i = 0; i < nbTirage; i++) {
            int resultatTirage = choixDeMainAleatoire();
            if (resultatTirage == spock) {
                count_spock++;
            }
            if (resultatTirage == ciseaux) {
                count_ciseaux++;
            }
            if (resultatTirage == lezard) {
                count_lezard++;
            }
            if (resultatTirage == papier) {
                count_papier++;
            }
            if (resultatTirage == pierre) {
                count_pierre++;
            }
        }

        System.out.println("Pour " + nbTirage + " tirages :");

        System.out.println("count_spock : " + count_spock);
        System.out.println("count_ciseaux : " + count_ciseaux);
        System.out.println("count_lezard : " + count_lezard);
        System.out.println("count_papier : " + count_papier);
        System.out.println("count_pierre : " + count_pierre);

        System.out.println("Resultat count_spock (%) :  " + ((float) count_spock / (float) nbTirage) * 100);
        System.out.println("Resultat count_ciseaux (%) :  " + ((float) count_ciseaux / (float) nbTirage) * 100);
        System.out.println("Resultat count_lezard (%) :  " + ((float) count_lezard / (float) nbTirage) * 100);
        System.out.println("Resultat count_papier (%) :  " + ((float) count_papier / (float) nbTirage) * 100);
        System.out.println("Resultat count_pierre (%) :  " + ((float) count_pierre / (float) nbTirage) * 100);
    }

    public static void testDifficulteNiveau2() {
        int count_victoire_joueur = 0;
        int count_defaite = 0;
        int count_egalite = 0;
        int nombreTirage = 100000;

        for (int i = 0; i < nombreTirage; i++) {
            int resultatTirage = tirageIA(2);
            if (resultatTirage == 1) count_victoire_joueur++;
            if (resultatTirage == 0) count_defaite++;
            if (resultatTirage == 2) count_egalite++;
        }

        System.out.println("On a fait " + nombreTirage + " en difficulté 2 (niveau 2 - tirage aléatoire avec 55% de victoire joueur et 15% d'égalité)");
        System.out.println("Victoire joueur = " + ((float) count_victoire_joueur / (float) nombreTirage) * 100 + " %");
        System.out.println("Defaite = " + ((float) count_defaite / (float) nombreTirage) * 100 + " %");
        System.out.println("Egalite = " + ((float) count_egalite / (float) nombreTirage) * 100 + " %");
    }

    public static void testDifficulteNiveau3() {
        int count_victoire_joueur = 0;
        int count_defaite = 0;
        int count_egalite = 0;
        int nombreTirage = 100000;

        for (int i = 0; i < nombreTirage; i++) {
            int resultatTirage = tirageIA(3);
            if (resultatTirage == 1) count_victoire_joueur++;
            if (resultatTirage == 0) count_defaite++;
            if (resultatTirage == 2) count_egalite++;
        }

        System.out.println("On a fait " + nombreTirage + " en difficulté 3 (niveau 3 - tirage aléatoire avec 40% de victoire joueur 10% d'égalité)");
        System.out.println("Victoire joueur = " + ((float) count_victoire_joueur / (float) nombreTirage) * 100 + " %");
        System.out.println("Defaite = " + ((float) count_defaite / (float) nombreTirage) * 100 + " %");
        System.out.println("Egalite = " + ((float) count_egalite / (float) nombreTirage) * 100 + " %");
    }

}
