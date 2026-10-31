package bj.ifri.medipass.app;

import bj.ifri.medipass.model.Consultation;
import bj.ifri.medipass.model.Medecin;
import bj.ifri.medipass.model.Patient;
import bj.ifri.medipass.service.MedipassService;
import java.time.LocalDate;

/** Fictitious sample data for the in-memory demo mode. */
final class DemoData {
    private DemoData() { }

    static void load(MedipassService s) {
        Medecin generaliste = s.enregistrerMedecin(new Medecin("Houngbo", "Aline", "Médecine générale", "+229 01 00 00 01"));
        Medecin pediatre = s.enregistrerMedecin(new Medecin("Dossou", "Marc", "Pédiatrie", "+229 01 00 00 02"));
        s.enregistrerMedecin(new Medecin("Agossou", "Rita", "Cardiologie", "+229 01 00 00 03"));

        Patient koffi = s.enregistrerPatient(new Patient("Adjovi", "Koffi", LocalDate.of(1990, 4, 12), 'M',
                "+229 01 11 11 11", "O+", "Pénicilline"));
        Patient awa = s.enregistrerPatient(new Patient("Sossa", "Awa", LocalDate.of(2018, 9, 3), 'F',
                "+229 01 22 22 22", "A+", ""));

        s.ajouterConsultation(new Consultation(koffi.getId(), generaliste.getId(), LocalDate.now().minusMonths(3),
                "Fièvre et courbatures", "Paludisme simple", "Artéméther-luméfantrine 3 jours"));
        s.ajouterConsultation(new Consultation(koffi.getId(), generaliste.getId(), LocalDate.now().minusDays(10),
                "Contrôle", "Guéri", "—"));
        s.ajouterConsultation(new Consultation(awa.getId(), pediatre.getId(), LocalDate.now().minusDays(20),
                "Vaccination", "Rappel effectué", "Paracétamol si fièvre"));
    }
}
