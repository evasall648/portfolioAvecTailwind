import pyodbc
from faker import Faker
from datetime import datetime
import csv

# Fonction de connexion à la base de données avec gestion des erreurs
def connect_db():
    try:
        connection = pyodbc.connect(
            'DRIVER={MySQL ODBC 8.0 Unicode Driver};'
            'SERVER=localhost;'
            'DATABASE=Recensement;'
            'UID=root;'
            'PWD=ton_mot_de_passe;'
            'CHARSET=utf8mb4;'
        )
        return connection
    except pyodbc.Error as e:
        print(f"Erreur lors de la connexion à la base de données : {e}")
        return None

# Fonction pour calculer l'âge à partir de la date de naissance
def calculer_age(date_naissance):
    today = datetime.today()
    birth_date = datetime.strptime(date_naissance, "%Y-%m-%d")
    return today.year - birth_date.year - ((today.month, today.day) < (birth_date.month, birth_date.day))

# Fonction pour ajouter une région avec gestion des erreurs
def ajouter_region(cursor, nom_region):
    try:
        query = "INSERT INTO Regions (nom_region) VALUES (?)"
        cursor.execute(query, (nom_region,))
    except pyodbc.Error as e:
        print(f"Erreur lors de l'ajout de la région '{nom_region}' : {e}")

# Fonction pour ajouter un département avec gestion des erreurs
def ajouter_departement(cursor, nom_departement, id_region):
    try:
        query = "INSERT INTO Departements (nom_departement, id_region) VALUES (?, ?)"
        cursor.execute(query, (nom_departement, id_region))
    except pyodbc.Error as e:
        print(f"Erreur lors de l'ajout du département '{nom_departement}' : {e}")

# Fonction pour ajouter un individu avec gestion des erreurs
def ajouter_individu(cursor, nom, prenom, date_naissance, id_departement):
    try:
        query = "INSERT INTO Individus (nom, prenom, date_naissance, id_departement) VALUES (?, ?, ?, ?)"
        cursor.execute(query, (nom, prenom, date_naissance, id_departement))
    except pyodbc.Error as e:
        print(f"Erreur lors de l'ajout de l'individu '{nom} {prenom}' : {e}")

# Fonction pour lister les individus par région avec gestion des erreurs
def lister_individus_par_region(cursor, id_region):
    try:
        query = """
        SELECT i.nom, i.prenom, i.date_naissance
        FROM Individus i
        JOIN Departements d ON i.id_departement = d.id_departement
        WHERE d.id_region = ?
        """
        cursor.execute(query, (id_region,))
        return cursor.fetchall()
    except pyodbc.Error as e:
        print(f"Erreur lors de la récupération des individus pour la région ID {id_region} : {e}")
        return []

# Fonction pour lister le nombre d'individus par région avec gestion des erreurs
def lister_nombre_individus_par_region(cursor):
    try:
        query = """
        SELECT r.nom_region, COUNT(i.id_individu) AS nombre_individus
        FROM Regions r
        LEFT JOIN Departements d ON r.id_region = d.id_region
        LEFT JOIN Individus i ON d.id_departement = i.id_departement
        GROUP BY r.id_region
        """
        cursor.execute(query)
        return cursor.fetchall()
    except pyodbc.Error as e:
        print(f"Erreur lors de la récupération du nombre d'individus par région : {e}")
        return []

# Fonction pour calculer des statistiques sur la population
def statistiques_population(cursor):
    try:
        query = "SELECT date_naissance FROM Individus"
        cursor.execute(query)
        individus = cursor.fetchall()
        
        ages = [calculer_age(individu[0]) for individu in individus]
        moyenne_age = sum(ages) / len(ages) if ages else 0

        tranches_age = {
            "18-30": 0,
            "31-40": 0,
            "41-50": 0,
            "51+": 0
        }

        for age in ages:
            if 18 <= age <= 30:
                tranches_age["18-30"] += 1
            elif 31 <= age <= 40:
                tranches_age["31-40"] += 1
            elif 41 <= age <= 50:
                tranches_age["41-50"] += 1
            elif age >= 51:
                tranches_age["51+"] += 1

        print(f"Moyenne d'âge : {moyenne_age:.2f} ans")
        print("Tranches d'âge :")
        for tranche, count in tranches_age.items():
            print(f"{tranche}: {count} individus")
    except pyodbc.Error as e:
        print(f"Erreur lors du calcul des statistiques de la population : {e}")

# Fonction de recherche avancée d'individus par critère avec gestion des erreurs
def recherche_avancee(cursor, critere, valeur):
    try:
        query = f"""
        SELECT nom, prenom, date_naissance
        FROM Individus
        WHERE {critere} LIKE ?
        """
        cursor.execute(query, (f"%{valeur}%",))
        return cursor.fetchall()
    except pyodbc.Error as e:
        print(f"Erreur lors de la recherche avancée avec le critère '{critere}' et la valeur '{valeur}' : {e}")
        return []

# Fonction pour importer des données depuis un fichier CSV avec gestion des erreurs
def importer_donnees(cursor, fichier_csv):
    try:
        with open(fichier_csv, mode='r', encoding='utf-8') as file:
            reader = csv.reader(file)
            next(reader)  # Ignorer l'en-tête
            for row in reader:
                ajouter_individu(cursor, row[0], row[1], row[2], int(row[3]))
    except (FileNotFoundError, IOError) as e:
        print(f"Erreur lors de l'importation des données depuis '{fichier_csv}' : {e}")

# Fonction pour exporter des données vers un fichier CSV avec gestion des erreurs
def exporter_donnees(cursor, fichier_csv):
    try:
        query = "SELECT nom, prenom, date_naissance, id_departement FROM Individus"
        cursor.execute(query)
        individus = cursor.fetchall()
        with open(fichier_csv, mode='w', encoding='utf-8', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(["Nom", "Prénom", "Date de naissance", "ID Département"])
            writer.writerows(individus)
    except (IOError, pyodbc.Error) as e:
        print(f"Erreur lors de l'exportation des données vers '{fichier_csv}' : {e}")

# Fonction pour rappeler les anniversaires des individus avec gestion des erreurs
def rappels_anniversaires(cursor):
    try:
        today = datetime.today()
        query = "SELECT nom, prenom, date_naissance FROM Individus"
        cursor.execute(query)
        individus = cursor.fetchall()
        for individu in individus:
            date_naissance = datetime.strptime(individu[2], "%Y-%m-%d")
            if date_naissance.day == today.day and date_naissance.month == today.month:
                print(f"Aujourd'hui, c'est l'anniversaire de {individu[0]} {individu[1]} !")
    except pyodbc.Error as e:
        print(f"Erreur lors de la vérification des anniversaires : {e}")

# Fonction pour générer des données fictives avec gestion des erreurs
def generate_fake_data(cursor):
    try:
        fake = Faker("fr_FR")  # Générer des données en français

        # Ajouter des régions fictives
        regions = ['Dakar', 'Saint-Louis', 'Thiès']
        for region in regions:
            ajouter_region(cursor, region)

        # Ajouter des départements fictifs pour chaque région
        departements = {
            'Dakar': ['Dakar', 'Pikine', 'Guédiawaye'],
            'Saint-Louis': ['Saint-Louis', 'Rufisque'],
            'Thiès': ['Thiès', 'Mbour', 'Tivaouane']
        }
        for region, deps in departements.items():
            cursor.execute("SELECT id_region FROM Regions WHERE nom_region = ?", (region,))
            id_region = cursor.fetchone()[0]
            for dep in deps:
                ajouter_departement(cursor, dep, id_region)

        # Ajouter 20 individus fictifs
        for _ in range(20):
            nom = fake.last_name()
            prenom = fake.first_name()
            date_naissance = fake.date_of_birth(minimum_age=18, maximum_age=80).strftime("%Y-%m-%d")
            cursor.execute("SELECT id_departement FROM Departements ORDER BY RAND() LIMIT 1")
            id_departement = cursor.fetchone()[0]
            ajouter_individu(cursor, nom, prenom, date_naissance, id_departement)
    except pyodbc.Error as e:
        print(f"Erreur lors de la génération de données fictives : {e}")

# Fonction pour afficher le menu et valider le choix de l'utilisateur
def menu():
    options = {
        "1": "Ajouter un individu",
        "2": "Lister les individus d'une région",
        "3": "Lister le nombre d'individus par région",
        "4": "Statistiques sur la population (Moyenne d'âge, Tranches d'âge)",
        "5": "Recherche avancée d'individus",
        "6": "Importer des données depuis un fichier CSV",
        "7": "Exporter des données vers un fichier CSV",
        "8": "Rappels des anniversaires",
        "9": "Quitter"
    }
    print("\nMENU:")
    for key, value in options.items():
        print(f"{key}. {value}")
    choix = input("Entrez votre choix : ").strip()
    if choix in options:
        return choix
    else:
        print("Choix invalide. Veuillez entrer un numéro entre 1 et 9.")
        return None

# Fonction principale avec gestion des erreurs et validation des entrées utilisateur
def main():
    connection = connect_db()
    if connection is None:
        return
    cursor = connection.cursor()

    # Générer des données fictives
    generate_fake_data(cursor)
    connection.commit()

    while True:
        choix = menu()
        if choix is None:
            continue

        if choix == "1":
            # Ajouter un individu
            nom = input("Nom de l'individu : ").strip()
            prenom = input("Prénom de l'individu : ").strip()
            date_naissance = input("Date de naissance (YYYY-MM-DD) : ").strip()
            try:
                datetime.strptime(date_naissance, "%Y-%m-%d")
            except ValueError:
                print("Format de date invalide. Veuillez entrer une date au format YYYY-MM-DD.")
                continue
            try:
                id_departement = int(input("ID du département : ").strip())
            except ValueError:
                print("ID du département invalide. Veuillez entrer un nombre entier.")
                continue
            ajouter_individu(cursor, nom, prenom, date_naissance, id_departement)
            connection.commit()
            print("Individu ajouté avec succès.")

        elif choix == "2":
            # Lister les individus d'une région
            try:
                id_region = int(input("ID de la région : ").strip())
            except ValueError:
                print("ID de la région invalide. Veuillez entrer un nombre entier.")
                continue
            individus = lister_individus_par_region(cursor, id_region)
            if individus:
                print("Liste des individus dans la région :")
                for individu in individus:
                    print(f"{individu[0]} {individu[1]}, né le {individu[2]}")
            else:
                print("Aucun individu trouvé pour cette région.")

        elif choix == "3":
            # Lister le nombre d'individus par région
            regions = lister_nombre_individus_par_region(cursor)
            if regions:
                print("Nombre d'individus par région :")
                for region in regions:
                    print(f"{region[0]} : {region[1]} individus")
            else:
                print("Aucune donnée disponible.")

        elif choix == "4":
            # Statistiques sur la population
            statistiques_population(cursor)
        
        elif choix == "5":
            # Recherche avancée d'individus
            critere = input("Critère de recherche (nom, prenom, date_naissance) : ")
            valeur = input(f"Valeur à rechercher pour {critere} : ")
            resultats = recherche_avancee(cursor, critere, valeur)
            print("Résultats de la recherche :")
            for resultat in resultats:
                print(f"{resultat[0]} {resultat[1]}, né le {resultat[2]}")
        
        elif choix == "6":
            # Importer des données depuis un fichier CSV
            fichier_csv = input("Chemin du fichier CSV à importer : ")
            try:
                importer_donnees(cursor, fichier_csv)
                connection.commit()
                print("Données importées avec succès.")
            except Exception as e:
                print(f"Erreur lors de l'importation des données : {e}")
        
        elif choix == "7":
            # Exporter des données vers un fichier CSV
            fichier_csv = input("Chemin du fichier CSV pour l'exportation : ")
            try:
                exporter_donnees(cursor, fichier_csv)
                print("Données exportées avec succès.")
            except Exception as e:
                print(f"Erreur lors de l'exportation des données : {e}")
        
        elif choix == "8":
            # Rappels des anniversaires
            rappels_anniversaires(cursor)
        
        elif choix == "9":
            # Quitter
            print("Fermeture de l'application.")
            break
        
        else:
            print("Choix invalide, veuillez réessayer.")
    
    # Fermeture de la connexion à la base de données
    cursor.close()
    connection.close()

if __name__ == "__main__":
    main()