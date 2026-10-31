package bj.ifri.medipass.service;

import bj.ifri.medipass.model.Consultation;
import bj.ifri.medipass.model.Medecin;
import bj.ifri.medipass.model.Patient;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** The medical pass of a patient: identity + history of consultations (most recent first). */
public record DossierMedical(Patient patient, List<Entree> historique) {

    public record Entree(Consultation consultation, Medecin medecin) { }

    private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================== MEDIPASS ====================\n");
        sb.append(String.format("Patient   : %s %s (n° %d)%n", patient.getNom().toUpperCase(),
                patient.getPrenom(), patient.getId()));
        sb.append(String.format("Né(e) le  : %s  —  %d ans  —  sexe %c%n",
                patient.getDateNaissance().format(FR), patient.getAge(), patient.getSexe()));
        sb.append(String.format("Groupe    : %s%n", patient.getGroupeSanguin()));
        String allergies = patient.getAllergies();
        sb.append(String.format("Allergies : %s%n", allergies == null || allergies.isBlank() ? "aucune connue" : allergies));
        sb.append("--------------------------------------------------\n");
        if (historique.isEmpty()) sb.append("Aucune consultation enregistrée.\n");
        for (Entree e : historique) {
            Consultation c = e.consultation();
            Medecin m = e.medecin();
            sb.append(String.format("%s  Dr %s (%s)%n", c.getDate().format(FR),
                    m == null ? "?" : m.getNom().toUpperCase(), m == null ? "?" : m.getSpecialite()));
            sb.append("   Motif        : ").append(c.getMotif()).append('\n');
            sb.append("   Diagnostic   : ").append(nz(c.getDiagnostic())).append('\n');
            sb.append("   Prescription : ").append(nz(c.getPrescription())).append('\n');
        }
        sb.append("==================================================\n");
        return sb.toString();
    }

    private static String nz(String s) { return s == null || s.isBlank() ? "—" : s; }
}
