-- Medipass — MySQL schema
-- mysql -u root -p < sql/schema.sql
CREATE DATABASE IF NOT EXISTS medipass CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medipass;

CREATE TABLE IF NOT EXISTS patient (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom             VARCHAR(80)  NOT NULL,
    prenom          VARCHAR(80)  NOT NULL,
    date_naissance  DATE         NOT NULL,
    sexe            CHAR(1)      NOT NULL CHECK (sexe IN ('M', 'F')),
    telephone       VARCHAR(30),
    groupe_sanguin  VARCHAR(3),
    allergies       VARCHAR(255),
    INDEX idx_patient_nom (nom, prenom)
);

CREATE TABLE IF NOT EXISTS medecin (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(80)  NOT NULL,
    prenom      VARCHAR(80),
    specialite  VARCHAR(80)  NOT NULL,
    telephone   VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS consultation (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id         BIGINT NOT NULL,
    medecin_id         BIGINT NOT NULL,
    date_consultation  DATE   NOT NULL,
    motif              VARCHAR(255) NOT NULL,
    diagnostic         VARCHAR(255),
    prescription       TEXT,
    CONSTRAINT fk_consult_patient FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    CONSTRAINT fk_consult_medecin FOREIGN KEY (medecin_id) REFERENCES medecin(id),
    INDEX idx_consult_patient (patient_id, date_consultation)
);
