package VehiculeProject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Vehicule {
    private String marque;
    private String modele;
    private String typeMoteur;
    private double carburant;
    private List<String> historiqueEntretien; 

    public Vehicule(String marque, String modele, String typeMoteur, double carburant) {
        this.marque = marque;
        this.modele = modele;
        this.typeMoteur = typeMoteur;
        this.carburant = carburant;
        this.historiqueEntretien = new ArrayList<>();
    }

    
    public void afficherEntretien() {
        System.out.println("Historique des entretiens:");
        if (historiqueEntretien.isEmpty()) {
            System.out.println("Aucun entretien enregistré.");
        } else {
            for (String tache : historiqueEntretien) {
                System.out.println("- " + tache);
            }
        }
    }

    public void demarrer() {
        System.out.println("Le véhicule démarre 0 km.");
    }

    public void accelerer() {
        System.out.println("Le véhicule accélère.");
    }

    public void ralentir() {
        System.out.println("Le véhicule ralentit.");
    }

    public void freiner() {
        System.out.println("Le véhicule freine.");
    }

    public void afficherTypeDeVehicule() {
        System.out.println("Type de véhicule: " + this.getClass().getSimpleName());
    }

    public String getDetails() {
        return "Marque: " + marque + ", Modèle: " + modele + ", Type de moteur: " + typeMoteur  ;
    }

    public double calculerHeureDarrivee(double distance, double vitesse) {
        return distance / vitesse;
    }

    public void arriverAdestination() {
        System.out.println("Le véhicule est arrivé à destination.");
    }
 
    // Ajout d'un entretien avec description
    public void entretien(String description) {
        // Ajoute une tâche d'entretien à l'historique
        historiqueEntretien.add(description);
    }
    // Nouvelle méthode pour le ravitaillement et le paiement
    public void ravitaillerEtPayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Stations-service disponibles à proximité : ");
        System.out.println("Station 1 : 750 FCFA/L");
        System.out.println("Station 2 : 800 FCFA/L");
        System.out.println("Station 3 : 700 FCFA/L");

        int stationChoisie = 0;
        int prix = 0;

        while (stationChoisie < 1 || stationChoisie > 3) {
            System.out.println("Choisissez la station de votre choix (1/2/3) : ");
            if (scanner.hasNextInt()) {
                stationChoisie = scanner.nextInt();
                switch (stationChoisie) {
                    case 1: prix = 750; break;
                    case 2: prix = 800; break;
                    case 3: prix = 700; break;
                    default:
                        System.out.println("Station invalide. Choisissez entre 1, 2 ou 3.");
                        stationChoisie = 0;
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entre 1 et 3.");
                scanner.next();
            }
        }

        System.out.println("Vous avez choisi la station avec le tarif : " + prix + " FCFA/L");
        System.out.println("Combien de litres souhaitez-vous ravitailler ?");
        
        while (!scanner.hasNextInt()) {
            System.out.println("Erreur : Vous devez entrer un nombre entier.");
            scanner.next(); 
            System.out.print("Combien de litres souhaitez-vous ravitailler ? ");
        }
        int litres = scanner.nextInt();
        int montant = prix * litres;
        System.out.println("Vous devez payer : " + montant + " FCFA.");

        System.out.print("Veuillez entrer le montant payé : ");
        int montantPaye = scanner.nextInt();

        while (montantPaye < montant) {
            System.out.println("Le paiement est insuffisant. Vous devez encore payer : " + (montant - montantPaye) + " FCFA.");
            System.out.print("Veuillez entrer un montant supplémentaire : ");
            int montantSupplementaire = scanner.nextInt();
            montantPaye += montantSupplementaire;
        }

        if (montantPaye == montant) {
            System.out.println("Le paiement a été effectué avec succès.");
        } else if (montantPaye > montant) {
            System.out.println("Paiement effectué avec succès. Le montant excédentaire de " + (montantPaye - montant) + " FCFA vous sera remboursé.");
        }

        this.carburant += litres;
        System.out.println("Le réservoir a été ravitaillé avec " + litres + " litres. Nouveau niveau de carburant : " + this.carburant + " litres.ravitailler\n");
    }
}