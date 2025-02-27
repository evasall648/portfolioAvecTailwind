-- Création de la base de données
CREATE DATABASE IF NOT EXISTS Recensement_Senegal;
USE Recensement_Senegal;

-- Création de la table Regions
CREATE TABLE IF NOT EXISTS Regions (
    id_region INT AUTO_INCREMENT PRIMARY KEY,
    nom_region VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Departements
CREATE TABLE IF NOT EXISTS Departements (
    id_departement INT AUTO_INCREMENT PRIMARY KEY,
    nom_departement VARCHAR(100) NOT NULL UNIQUE,
    id_region INT NOT NULL,
    FOREIGN KEY (id_region) REFERENCES Regions(id_region)
);

-- Création de la table Individus
CREATE TABLE IF NOT EXISTS Individus (
    id_individu INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    sexe ENUM('Masculin', 'Féminin') NOT NULL,
    id_departement INT NOT NULL,
    FOREIGN KEY (id_departement) REFERENCES Departements(id_departement)
);