package VehiculeProject;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Choix du véhicule
        System.out.println("Bienvenue dans le système de transport! Veuillez choisir votre véhicule: ");
        System.out.println("1. Voiture\n2. Moto\n3. TriCycle");
        int choixVehicule = 0;
        boolean choixValide = false;
        
        while (!choixValide) {
            if (scanner.hasNextInt()) {
                choixVehicule = scanner.nextInt();
                if (choixVehicule >= 1 && choixVehicule <= 3) {
                    choixValide = true;
                } else {
                    System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 3.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entre 1 et 3.");
                scanner.next(); 
            }
        }
        scanner.nextLine();  

        
        // Personnalisation de notre  véhicule
        System.out.println("Veuillez personnaliser votre véhicule:");
        System.out.print("Entrez la marque: ");
        String marque = scanner.nextLine();
        System.out.print("Entrez le modèle: ");
        String modele = scanner.nextLine();
        System.out.print("Entrez le type de moteur (1-Essence/2-Diesel/3-Électrique): ");
        String typeMoteur = "";
        while (true) {
            typeMoteur = scanner.nextLine();  
            if (typeMoteur.equalsIgnoreCase("1") || typeMoteur.equalsIgnoreCase("2") || typeMoteur.equalsIgnoreCase("3")) {
                break; 
            } else {
                System.out.println("Erreur: Veuillez entrer ' 1 pour Essence', ' 2 pour Diesel' ou ' 3 pour Électrique'.");
            }
        }


      int carburant = 10; 
        System.out.println("Le carburant actuel dans la voiture est : " + carburant + " litres.\n");
        
        
        // Création de notre véhicule 
        Vehicule vehicule;
        switch (choixVehicule) {
            case 1:
                vehicule = new Voiture(marque, modele, typeMoteur, carburant);
                break;
            case 2:
                vehicule = new Moto(marque, modele, typeMoteur, carburant);
                break;
            case 3:
                vehicule = new TriCycle(marque, modele, typeMoteur, carburant);
                break;
            default:
                System.out.println("Choix invalide. Par défaut, une Voiture sera sélectionnée.");
                vehicule = new Voiture(marque, modele, typeMoteur, carburant);
        }
        System.out.println("Attributs du véhicule: \n" + vehicule.getDetails()+"\n");
    
        
        
        // Entrée des paramètres de trajet
        System.out.println("Veuillez entrer la distance à parcourir (en km):");
        int distance = 0;
        while (distance <= 0) {
            if (scanner.hasNextDouble()) {
                distance = (int) scanner.nextDouble();
                if (distance <= 0) {
                    System.out.println("La distance doit être un nombre positif.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre pour la distance.");
                scanner.next(); 
            }
        }
        int vitesse = 0;
       while (vitesse <= 0 || vitesse > 120) {
            System.out.print("Veuillez entrer la vitesse désirée (en km/h, max 120): ");
            if (scanner.hasNextInt()) {
                vitesse = scanner.nextInt();
                if (vitesse <= 0 || vitesse > 120) {
                    System.out.println("Vitesse invalide. Veuillez entrer une vitesse entre 1 et 120 km/h.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next();
            }
        }
        
        
     // Simulation de stations-service proches et sélection du ravitaillement
        System.out.println("Souhaitez-vous ravitailler ? (oui/non)");
        String ravitaillement = scanner.nextLine();
        
        while (!ravitaillement.equalsIgnoreCase("oui") && !ravitaillement.equalsIgnoreCase("non")) {
            System.out.println("Réponse invalide. Veuillez entrer 'oui' ou 'non'.");
            ravitaillement = scanner.nextLine();
        }
        if (ravitaillement.equalsIgnoreCase("oui")) {
        	
        	 vehicule.ravitaillerEtPayer();
        }else {
        	System.out.println("Vous avez choisi de ne pas ravitailler .\n");
        }
        
        // Conseils de conduite de l'IA
        System.out.println("Conseils de l'IA pour économiser du carburant :");
        System.out.println("1. Évitez les accélérations soudaines pour économiser du carburant.");
        System.out.println("2. Freinez doucement avant les feux pour réduire la consommation.\n");
        
      
        vehicule.demarrer();
        LocalTime heureDepart = LocalTime.now();
        LocalTime heureArrivee = heureDepart.plusMinutes((long) (vehicule.calculerHeureDarrivee(distance, vitesse) * 60));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println("Heure de départ: " + heureDepart.format(formatter));
        System.out.println("Heure approximative d'arrivée: " + heureArrivee.format(formatter) +"\n");
        
        
        // pour le confort de notre client 
        scanner.nextLine();  
        System.out.println("Souhaitez-vous écouter de la musique pendant le trajet? (oui/non)");
        String musique = scanner.nextLine();
        while (!musique.equalsIgnoreCase("oui") && !musique.equalsIgnoreCase("non")) {
            System.out.println("Réponse invalide. Veuillez entrer 'oui' ou 'non'.");
            musique = scanner.nextLine();
        }
        if (musique.equalsIgnoreCase("oui")) {
            System.out.println("Lecture de votre playlist préférée...");
        }
        
        System.out.println("Souhaitez-vous ajuster la température? (oui/non)");
        String temperature = scanner.nextLine();
        while (!temperature.equalsIgnoreCase("oui") && !temperature.equalsIgnoreCase("non")) {
            System.out.println("Réponse invalide. Veuillez entrer 'oui' ou 'non'.");
            temperature = scanner.nextLine();
        }
      
        if (temperature.equalsIgnoreCase("oui")) {
            int temp = 0;
            boolean tempValide = false;
            while (!tempValide) {
                System.out.print("Veuillez entrer la température souhaitée (en °C, max 100): ");
                if (scanner.hasNextInt()) {
                    temp = scanner.nextInt();
                    if (temp <= 100 && temp >= 0) {
                        tempValide = true;
                        System.out.println("Température réglée à " + temp + "°C.\n");
                    } else {
                        System.out.println("La température doit être comprise entre 0 et 100 °C.");
                    }
                } else {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                    scanner.next();
                }
            }
        }
        
        int vitesseActuelle = 0;
        boolean enCours = true;
        while (enCours) {
            vitesseActuelle += 5;
            if (vitesseActuelle > vitesse) {
                vitesseActuelle = (int) vitesse;
            }
            System.out.println("Vitesse actuelle: " + vitesseActuelle + " km/h");                       
            System.out.println("Souhaitez-vous ajuster votre vitesse? (1-accélérer/2-ralentir/3-maintenir)");
            String reponse = scanner.next();
            
            while (!reponse.equalsIgnoreCase("1") && !reponse.equalsIgnoreCase("2") && !reponse.equalsIgnoreCase("3")) {
                System.out.println("Réponse invalide. Veuillez entrer '1 pour accélérer', '2 pour ralentir',  ou ' 3 pour maintenir'.");
                reponse = scanner.next();
            }
            if (reponse.equalsIgnoreCase("1")) {
                vitesseActuelle += 5;
                if (vitesseActuelle > vitesse) vitesseActuelle = (int) vitesse;
                System.out.println("Vitesse accélérée à " + vitesseActuelle + " km/h.");
            } else if (reponse.equalsIgnoreCase("2")) {
                vitesseActuelle -= 5;
                if (vitesseActuelle < 0) vitesseActuelle = 0;
                System.out.println("Vitesse réduite à " + vitesseActuelle + " km/h.");
            } 
            else if (reponse.equalsIgnoreCase("3")) {
                System.out.println("Maintien de la vitesse actuelle à " + vitesseActuelle + " km/h.");
            }

            // Vérifier si l'arrivée est atteinte
            if (vitesseActuelle == vitesse) {
                enCours = false;  // Fin du trajet
            }
        }
       
        while (vitesseActuelle > 0) {
            vitesseActuelle -= 5;
            if (vitesseActuelle < 0) vitesseActuelle = 0;
            System.out.println("Vitesse actuelle: " + vitesseActuelle + " km/h");
        }
        vehicule.arriverAdestination();
        System.out.println("La course est terminée. Merci d'avoir voyagé avec nous!\n");


       
     // Paiement
        System.out.println("Veuillez choisir un mode de paiement :");
        System.out.println("1. Liquide\n2. Wave\n3. Orange Money");

        int modePaiement = 0;
        while (modePaiement < 1 || modePaiement > 3) {
            if (scanner.hasNextInt()) {
                modePaiement = scanner.nextInt();
                if (modePaiement < 1 || modePaiement > 3) {
                    System.out.println("Choix invalide. Veuillez choisir entre 1, 2 ou 3.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entre 1 et 3.");
                scanner.next(); 
            }
        }

        String moyenPaiement = switch (modePaiement) {
            case 1 -> "Liquide";
            case 2 -> "Wave";
            case 3 -> "Orange Money";
            default -> "";
        };

        System.out.println("Vous avez choisi le mode de paiement : " + moyenPaiement);
        System.out.println("Confirmez-vous ce choix ? (oui/non)");
        scanner.nextLine();
        String confirmation = scanner.nextLine();

        while (!confirmation.equalsIgnoreCase("oui") && !confirmation.equalsIgnoreCase("non")) {
            System.out.println("Réponse invalide. Veuillez entrer 'oui' ou 'non'.");
            confirmation = scanner.nextLine();
        }

        while (!confirmation.equals("oui")) {
            System.out.println("Votre paiement est annulé. Veuillez confirmer votre choix en entrant 'oui'."); 
            confirmation = scanner.nextLine();
        }

       
        // Calcul du montant en fonction de la distance parcourue
        int tarifParKm = 500; 
        int montantTotal = distance * tarifParKm;
        System.out.println("Le montant à payer pour " + distance + " km est de : " + montantTotal + " FCFA.");

        if (modePaiement == 1) { 
        	
            // Paiement en liquide
            int paiement;
            do {
                System.out.print("Veuillez donner le montant en liquide (au moins " + montantTotal + " FCFA) : ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrée invalide. Veuillez entrer un montant valide.");
                    scanner.next();
                }
                paiement = scanner.nextInt();
                if (paiement < montantTotal) {
                    System.out.println("Montant insuffisant. Veuillez entrer un montant égal ou supérieur à " + montantTotal + " FCFA.");
                }
            } while (paiement < montantTotal);
              int monnaie = paiement - montantTotal;
            System.out.println(monnaie > 0 ? "Paiement reçu. Votre monnaie est de : " + monnaie + " FCFA." : "Paiement exact reçu. Merci!");
        } else { // Paiement électronique
            System.out.print("Veuillez confirmer le paiement de " + montantTotal + " FCFA via " + moyenPaiement + ". (oui/non) : ");
            
            while (!scanner.nextLine().equalsIgnoreCase("oui")) {
                System.out.println("Paiement non confirmé. Veuillez entrer 'oui' pour confirmer et envoyer le paiement.");
            }
            
            System.out.println("Veuillez entrer le montant que vous souhaitez payer via " + moyenPaiement + " : ");
            int montantPaye = 0;
            while (montantPaye != montantTotal) { 
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrée invalide. Veuillez entrer un montant valide.");
                    scanner.next(); 
                }
                montantPaye = scanner.nextInt();
                scanner.nextLine(); 
                if (montantPaye < montantTotal) {
                    System.out.println("Le montant est insuffisant. Veuillez entrer " + montantTotal + " FCFA exactement.");
                } else if (montantPaye > montantTotal) {
                    System.out.println("Vous avez payé trop. Veuillez entrer le montant exact de " + montantTotal + " FCFA.");
                }
            }            
            System.out.println("Paiement de " + montantTotal + " FCFA reçu via " + moyenPaiement + ". Merci!\n ");
        }
  

        
     // Évaluations
        System.out.println("Veuillez évaluer l'état du véhicule (1 à 5) :");
        int evaluationVehicule = 0;
        while (evaluationVehicule < 1 || evaluationVehicule > 5) {
            if (scanner.hasNextInt()) {
                evaluationVehicule = scanner.nextInt();
                if (evaluationVehicule < 1 || evaluationVehicule > 5) {
                    System.out.println("L'évaluation doit être entre 1 et 5.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entre 1 et 5.");
                scanner.next();
            }
        }
        System.out.println("Merci pour votre évaluation de l'état du véhicule : " + evaluationVehicule + "/5.");

        System.out.println("Veuillez évaluer le trajet (1 à 5) :");
        int evaluationTrajet = 0;
        while (evaluationTrajet < 1 || evaluationTrajet > 5) {
            if (scanner.hasNextInt()) {
                evaluationTrajet = scanner.nextInt();
                if (evaluationTrajet < 1 || evaluationTrajet > 5) {
                    System.out.println("L'évaluation doit être entre 1 et 5.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entre 1 et 5.");
                scanner.next();
            }
        }
        System.out.println("Merci pour votre évaluation du trajet : " + evaluationTrajet + "/5.\n" );

        // Administration
        System.out.println("---Bienvenue dans le systéme d'administration---\n");
        System.out.println(" Veillez entre votre mot de passe pour accéder aux données (5 tentatives) :");
        final String motDepasse = "sabakey3";
        boolean accesAutorise = false;
        for (int i = 0; i < 5; i++) {
            System.out.println("Tentative " + (i + 1) + " :");
            String motDePasse = scanner.next();
            if (motDePasse.equals(motDepasse)) {
                accesAutorise = true;
                break;
            }
        }
        if (accesAutorise) {
            System.out.println("Accès autorisé. Voici les données administratives :\n");
            System.out.println("Évaluation de l'état du véhicule : " + evaluationVehicule + "/5");
            System.out.println("Évaluation du trajet : " + evaluationTrajet + "/5");
            System.out.println("Détails du véhicule :");
            System.out.println(vehicule.getDetails() +"\n");
        } else {
            System.out.println("Accès refusé. Mot de passe incorrect.");            
        }
       
        vehicule.entretien("  Vérification des freins effectuée le 31/12/2024.");
        vehicule.entretien("  Changement d'huile effectué le 6/01/2024.");
        vehicule.afficherEntretien();
        scanner.close();
    }
    }