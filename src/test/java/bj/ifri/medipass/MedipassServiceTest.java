package bj.ifri.medipass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import bj.ifri.medipass.dao.DaoFactory;
import bj.ifri.medipass.model.Consultation;
import bj.ifri.medipass.model.Medecin;
import bj.ifri.medipass.model.Patient;
import bj.ifri.medipass.service.DossierMedical;
import bj.ifri.medipass.service.MedipassService;
import bj.ifri.medipass.service.ValidationException;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class MedipassServiceTest {

    private MedipassService service;
    private Patient koffi;
    private Medecin docteur;

    @Before public void setUp() {
        service = new MedipassService(DaoFactory.of(DaoFactory.Type.MEMORY));
        docteur = service.enregistrerMedecin(new Medecin("Houngbo", "Aline", "Médecine générale", ""));
        koffi = service.enregistrerPatient(new Patient("Adjovi", "Koffi", LocalDate.of(1990, 4, 12), 'M',
                "", "O+", "Pénicilline"));
    }

    private Consultation consultation(LocalDate date, String motif) {
        return new Consultation(koffi.getId(), docteur.getId(), date, motif, "diag", "presc");
    }

    @Test public void dossierListsConsultationsMostRecentFirst() {
        service.ajouterConsultation(consultation(LocalDate.now().minusDays(30), "ancienne"));
        service.ajouterConsultation(consultation(LocalDate.now().minusDays(1), "récente"));
        DossierMedical d = service.dossier(koffi.getId());
        assertEquals(2, d.historique().size());
        assertEquals("récente", d.historique().get(0).consultation().getMotif());
        assertEquals(docteur, d.historique().get(0).medecin());
        assertTrue(d.format().contains("Pénicilline"));
    }

    @Test(expected = ValidationException.class)
    public void rejectsBirthDateInTheFuture() {
        service.enregistrerPatient(new Patient("A", "B", LocalDate.now().plusDays(1), 'M', "", "O+", ""));
    }

    @Test(expected = ValidationException.class)
    public void rejectsUnknownBloodGroup() {
        service.enregistrerPatient(new Patient("A", "B", LocalDate.of(2000, 1, 1), 'M', "", "Z+", ""));
    }

    @Test(expected = ValidationException.class)
    public void rejectsBlankName() {
        service.enregistrerPatient(new Patient(" ", "B", LocalDate.of(2000, 1, 1), 'M', "", "O+", ""));
    }

    @Test(expected = ValidationException.class)
    public void rejectsConsultationInTheFuture() {
        service.ajouterConsultation(consultation(LocalDate.now().plusDays(2), "futur"));
    }

    @Test(expected = ValidationException.class)
    public void rejectsConsultationForUnknownDoctor() {
        service.ajouterConsultation(new Consultation(koffi.getId(), 999L, LocalDate.now(), "x", "", ""));
    }

    @Test(expected = ValidationException.class)
    public void rejectsConsultationWithoutMotif() {
        service.ajouterConsultation(consultation(LocalDate.now(), ""));
    }

    @Test public void deletingPatientAlsoDeletesHistory() {
        service.ajouterConsultation(consultation(LocalDate.now(), "contrôle"));
        service.supprimerPatient(koffi.getId());
        assertTrue(service.listerPatients().isEmpty());
        try {
            service.dossier(koffi.getId());
        } catch (ValidationException expected) {
            return;
        }
        throw new AssertionError("dossier of a deleted patient should not exist");
    }

    @Test public void searchFindsPatient() {
        assertEquals(1, service.rechercherPatients("koff").size());
    }
}
