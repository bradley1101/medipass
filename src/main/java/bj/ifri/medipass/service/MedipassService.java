package bj.ifri.medipass.service;

import bj.ifri.medipass.dao.ConsultationDao;
import bj.ifri.medipass.dao.DaoFactory;
import bj.ifri.medipass.dao.MedecinDao;
import bj.ifri.medipass.dao.PatientDao;
import bj.ifri.medipass.model.Consultation;
import bj.ifri.medipass.model.Medecin;
import bj.ifri.medipass.model.Patient;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/** Business rules. Depends only on the DAO interfaces, never on JDBC. */
public class MedipassService {

    private static final Set<String> GROUPES = Set.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

    private final PatientDao patients;
    private final MedecinDao medecins;
    private final ConsultationDao consultations;

    public MedipassService(DaoFactory factory) {
        this.patients = factory.getPatientDao();
        this.medecins = factory.getMedecinDao();
        this.consultations = factory.getConsultationDao();
    }

    // ------------------------------------------------------------ patients
    public Patient enregistrerPatient(Patient p) {
        require(notBlank(p.getNom()) && notBlank(p.getPrenom()), "Le nom et le prénom sont obligatoires");
        require(p.getDateNaissance() != null && !p.getDateNaissance().isAfter(LocalDate.now()),
                "Date de naissance invalide");
        require(p.getSexe() == 'M' || p.getSexe() == 'F', "Sexe : M ou F");
        require(p.getGroupeSanguin() == null || GROUPES.contains(p.getGroupeSanguin()),
                "Groupe sanguin inconnu : " + p.getGroupeSanguin());
        return patients.create(p);
    }

    public List<Patient> listerPatients() { return patients.findAll(); }

    public List<Patient> rechercherPatients(String texte) { return patients.searchByName(texte); }

    public Patient patient(long id) {
        return patients.findById(id).orElseThrow(() -> new ValidationException("Patient introuvable : " + id));
    }

    /** Deletes the patient and his whole history. */
    public void supprimerPatient(long id) {
        patient(id);
        consultations.findByPatient(id).forEach(c -> consultations.delete(c.getId()));
        patients.delete(id);
    }

    // ------------------------------------------------------------- doctors
    public Medecin enregistrerMedecin(Medecin m) {
        require(notBlank(m.getNom()) && notBlank(m.getSpecialite()), "Nom et spécialité obligatoires");
        return medecins.create(m);
    }

    public List<Medecin> listerMedecins() { return medecins.findAll(); }

    // ------------------------------------------------------- consultations
    public Consultation ajouterConsultation(Consultation c) {
        patient(c.getPatientId());
        require(medecins.findById(c.getMedecinId()).isPresent(), "Médecin introuvable : " + c.getMedecinId());
        require(c.getDate() != null && !c.getDate().isAfter(LocalDate.now()),
                "La date de consultation ne peut pas être dans le futur");
        require(notBlank(c.getMotif()), "Le motif est obligatoire");
        return consultations.create(c);
    }

    public DossierMedical dossier(long patientId) {
        Patient p = patient(patientId);
        List<DossierMedical.Entree> entrees = consultations.findByPatient(patientId).stream()
                .map(c -> new DossierMedical.Entree(c, medecins.findById(c.getMedecinId()).orElse(null)))
                .toList();
        return new DossierMedical(p, entrees);
    }

    // ------------------------------------------------------------- helpers
    private static boolean notBlank(String s) { return s != null && !s.isBlank(); }

    private static void require(boolean condition, String message) {
        if (!condition) throw new ValidationException(message);
    }
}
