CREATE DATABASE GestionUniversitaire;
GO

USE GestionUniversitaire;
GO

CREATE TABLE Etudiant (
	id_etudiant INT PRIMARY KEY IDENTITY (1,1),
	nom VARCHAR (50) NOT NULL,
	prenom VARCHAR (50) NOT NULL,
	date_naissance DATE NOT NULL,
	email VARCHAR (100) UNIQUE NOT NULL
	);

CREATE TABLE Enseignant (
	id_enseignant INT PRIMARY KEY IDENTITY (1,1),
	nom VARCHAR (50) NOT NULL,
	specialite VARCHAR (100) NOT NULL
	);

CREATE TABLE Module (
	id_module INT PRIMARY KEY IDENTITY (1,1),
	nom VARCHAR (100) NOT NULL,
	coefficient DECIMAL(4,2) NOT NULL CHECK (coefficient > 0),
	volume_horaire INT NOT NULL CHECK (volume_horaire > 0),
	id_enseignant INT NOT NULL,
	FOREIGN KEY (id_enseignant) REFERENCES Enseignant(id_enseignant)
	);

CREATE TABLE Inscription (
	id_etudiant INT NOT NULL,
	id_module INT NOT NULL,
	date_inscription DATE NOT NULL DEFAULT GETDATE(),
	PRIMARY KEY (id_etudiant, id_module),
    FOREIGN KEY (id_etudiant) REFERENCES Etudiant(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_module) REFERENCES Module(id_module) ON DELETE CASCADE
	);

CREATE TABLE Note (
	id_note INT PRIMARY KEY IDENTITY (1,1),
	id_etudiant INT NOT NULL,
	id_module INT NOT NULL,
	valeur DECIMAL(5,2) NOT NULL CHECK (valeur BETWEEN 0 AND 20),
	type VARCHAR (20) NOT NULL CHECK(type IN ('CC','Examen','Rattrapage')),
	session VARCHAR (20) NOT NULL CHECK(session IN('Normale','Rattrapage')),
	FOREIGN KEY (id_etudiant, id_module) REFERENCES Inscription(id_etudiant, id_module),
    CONSTRAINT unique_note_per_session UNIQUE (id_etudiant, id_module, type, session)
	);
	GO

CREATE INDEX idx_etudiant_nom ON Etudiant(nom);
CREATE INDEX idx_module_nom ON Module(nom);
CREATE INDEX idx_note_etudiant_module ON Note(id_etudiant, id_module);
GO

CREATE VIEW v_NotesAvantRattrapage AS
SELECT 
    e.id_etudiant,
    e.nom,
    e.prenom,
    m.id_module,
    m.nom AS module_nom,
    m.coefficient,
    MAX(CASE WHEN n.type = 'CC' THEN n.valeur END) AS note_cc,
    MAX(CASE WHEN n.type = 'Examen' THEN n.valeur END) AS note_examen,
    (MAX(CASE WHEN n.type = 'CC' THEN n.valeur END) * 0.4 + 
     MAX(CASE WHEN n.type = 'Examen' THEN n.valeur END) * 0.6) AS moyenne_module
FROM Etudiant e
JOIN Inscription i ON e.id_etudiant = i.id_etudiant
JOIN Module m ON i.id_module = m.id_module
LEFT JOIN Note n ON n.id_etudiant = e.id_etudiant AND n.id_module = m.id_module AND n.session = 'Normale'
GROUP BY e.id_etudiant, e.nom, e.prenom, m.id_module, m.nom, m.coefficient;
GO

CREATE VIEW v_EtudiantsRattrapage AS
SELECT DISTINCT 
    e.id_etudiant,
    e.nom,
    e.prenom,
    e.email
FROM v_NotesAvantRattrapage v
JOIN Etudiant e ON v.id_etudiant = e.id_etudiant
WHERE v.moyenne_module < 10;
GO

CREATE VIEW v_NotesApresRattrapage AS
SELECT 
    e.id_etudiant,
    e.nom,
    e.prenom,
    m.id_module,
    m.nom AS module_nom,
    m.coefficient,
    MAX(CASE WHEN n.type = 'CC' THEN n.valeur END) AS note_cc,
    MAX(CASE WHEN n.type = 'Examen' AND n.session = 'Normale' THEN n.valeur ELSE 0 END) AS examen_normal,
    MAX(CASE WHEN n.type = 'Examen' AND n.session = 'Rattrapage' THEN n.valeur ELSE 0 END) AS examen_rattrapage,
    MAX(CASE WHEN n.type = 'CC' THEN n.valeur END) * 0.4 + 
    CASE 
        WHEN MAX(CASE WHEN n.type = 'Examen' AND n.session = 'Normale' THEN n.valeur ELSE 0 END) >= 
             MAX(CASE WHEN n.type = 'Examen' AND n.session = 'Rattrapage' THEN n.valeur ELSE 0 END)
        THEN MAX(CASE WHEN n.type = 'Examen' AND n.session = 'Normale' THEN n.valeur ELSE 0 END)
        ELSE MAX(CASE WHEN n.type = 'Examen' AND n.session = 'Rattrapage' THEN n.valeur ELSE 0 END)
    END * 0.6 AS moyenne_module_finale
FROM Etudiant e
JOIN Inscription i ON e.id_etudiant = i.id_etudiant
JOIN Module m ON i.id_module = m.id_module
LEFT JOIN Note n ON n.id_etudiant = e.id_etudiant AND n.id_module = m.id_module
GROUP BY e.id_etudiant, e.nom, e.prenom, m.id_module, m.nom, m.coefficient;
GO

CREATE VIEW v_Diplomes AS
SELECT 
    e.id_etudiant,
    e.nom,
    e.prenom,
    AVG(v.moyenne_module_finale) AS moyenne_generale
FROM Etudiant e
JOIN v_NotesApresRattrapage v ON e.id_etudiant = v.id_etudiant
GROUP BY e.id_etudiant, e.nom, e.prenom
HAVING MIN(v.moyenne_module_finale) >= 10;
GO

CREATE PROCEDURE sp_MoyenneGeneraleAvantRattrapage
    @id_etudiant INT
AS
BEGIN
    SELECT 
        e.id_etudiant,
        e.nom,
        e.prenom,
        SUM(v.moyenne_module * m.coefficient) / SUM(m.coefficient) AS moyenne_generale
    FROM v_NotesAvantRattrapage v
    JOIN Module m ON v.id_module = m.id_module
    JOIN Etudiant e ON v.id_etudiant = e.id_etudiant
    WHERE v.id_etudiant = @id_etudiant
    GROUP BY e.id_etudiant, e.nom, e.prenom;
END;
GO

CREATE PROCEDURE sp_MoyenneGeneraleApresRattrapage
    @id_etudiant INT
AS
BEGIN
    SELECT 
        e.id_etudiant,
        e.nom,
        e.prenom,
        SUM(v.moyenne_module_finale * m.coefficient) / SUM(m.coefficient) AS moyenne_generale
    FROM v_NotesApresRattrapage v
    JOIN Module m ON v.id_module = m.id_module
    JOIN Etudiant e ON v.id_etudiant = e.id_etudiant
    WHERE v.id_etudiant = @id_etudiant
    GROUP BY e.id_etudiant, e.nom, e.prenom;
END;
GO