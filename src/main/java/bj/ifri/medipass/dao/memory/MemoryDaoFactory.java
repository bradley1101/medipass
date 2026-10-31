package bj.ifri.medipass.dao.memory;

import bj.ifri.medipass.dao.ConsultationDao;
import bj.ifri.medipass.dao.DaoFactory;
import bj.ifri.medipass.dao.MedecinDao;
import bj.ifri.medipass.dao.PatientDao;
import bj.ifri.medipass.model.Consultation;
import bj.ifri.medipass.model.Medecin;
import bj.ifri.medipass.model.Patient;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** DAOs backed by memory: used for the demo mode and the unit tests (no MySQL needed). */
public class MemoryDaoFactory extends DaoFactory {

    private final PatientDao patients = new MemoryPatientDao();
    private final MedecinDao medecins = new MemoryMedecinDao();
    private final ConsultationDao consultations = new MemoryConsultationDao();

    @Override public PatientDao getPatientDao() { return patients; }
    @Override public MedecinDao getMedecinDao() { return medecins; }
    @Override public ConsultationDao getConsultationDao() { return consultations; }

    // ---------------------------------------------------------------- patients
    static class MemoryPatientDao implements PatientDao {
        private final MemoryStore<Patient> store = new MemoryStore<>(Patient::getId, Patient::setId);
        private static final Comparator<Patient> BY_NAME =
                Comparator.comparing(Patient::getNom, String.CASE_INSENSITIVE_ORDER)
                          .thenComparing(Patient::getPrenom, String.CASE_INSENSITIVE_ORDER);

        @Override public Patient create(Patient p) { return store.create(p); }
        @Override public Optional<Patient> findById(long id) { return store.find(id); }
        @Override public List<Patient> findAll() { return store.all().stream().sorted(BY_NAME).toList(); }
        @Override public boolean update(Patient p) { return store.update(p); }
        @Override public boolean delete(long id) { return store.delete(id); }

        @Override public List<Patient> searchByName(String text) {
            String t = text.toLowerCase();
            return findAll().stream()
                    .filter(p -> p.getNom().toLowerCase().contains(t) || p.getPrenom().toLowerCase().contains(t))
                    .toList();
        }
    }

    // ---------------------------------------------------------------- doctors
    static class MemoryMedecinDao implements MedecinDao {
        private final MemoryStore<Medecin> store = new MemoryStore<>(Medecin::getId, Medecin::setId);

        @Override public Medecin create(Medecin m) { return store.create(m); }
        @Override public Optional<Medecin> findById(long id) { return store.find(id); }
        @Override public List<Medecin> findAll() {
            return store.all().stream()
                    .sorted(Comparator.comparing(Medecin::getNom, String.CASE_INSENSITIVE_ORDER)).toList();
        }
        @Override public boolean update(Medecin m) { return store.update(m); }
        @Override public boolean delete(long id) { return store.delete(id); }

        @Override public List<Medecin> findBySpecialite(String specialite) {
            return findAll().stream().filter(m -> m.getSpecialite().equalsIgnoreCase(specialite)).toList();
        }
    }

    // ---------------------------------------------------------- consultations
    static class MemoryConsultationDao implements ConsultationDao {
        private final MemoryStore<Consultation> store =
                new MemoryStore<>(Consultation::getId, Consultation::setId);
        private static final Comparator<Consultation> RECENT_FIRST =
                Comparator.comparing(Consultation::getDate).thenComparing(Consultation::getId).reversed();

        @Override public Consultation create(Consultation c) { return store.create(c); }
        @Override public Optional<Consultation> findById(long id) { return store.find(id); }
        @Override public List<Consultation> findAll() { return store.all().stream().sorted(RECENT_FIRST).toList(); }
        @Override public boolean update(Consultation c) { return store.update(c); }
        @Override public boolean delete(long id) { return store.delete(id); }

        @Override public List<Consultation> findByPatient(long patientId) {
            return findAll().stream().filter(c -> c.getPatientId() == patientId).toList();
        }

        @Override public List<Consultation> findByMedecin(long medecinId) {
            return findAll().stream().filter(c -> c.getMedecinId() == medecinId).toList();
        }
    }
}
