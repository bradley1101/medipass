package bj.ifri.medipass.dao;

import bj.ifri.medipass.model.Consultation;
import java.util.List;

public interface ConsultationDao extends Dao<Consultation> {
    /** All consultations of a patient, most recent first. */
    List<Consultation> findByPatient(long patientId);

    List<Consultation> findByMedecin(long medecinId);
}
