package bj.ifri.medipass.dao.jdbc;

import bj.ifri.medipass.dao.ConsultationDao;
import bj.ifri.medipass.model.Consultation;
import java.util.List;
import java.util.Optional;

public class JdbcConsultationDao extends JdbcSupport<Consultation> implements ConsultationDao {

    private static final String COLS =
            "id, patient_id, medecin_id, date_consultation, motif, diagnostic, prescription";

    @Override protected RowMapper<Consultation> mapper() {
        return rs -> {
            Consultation c = new Consultation(rs.getLong("patient_id"), rs.getLong("medecin_id"),
                    date(rs, "date_consultation"), rs.getString("motif"),
                    rs.getString("diagnostic"), rs.getString("prescription"));
            c.setId(rs.getLong("id"));
            return c;
        };
    }

    @Override public Consultation create(Consultation c) {
        c.setId(insert("INSERT INTO consultation (patient_id, medecin_id, date_consultation, motif, diagnostic, "
                        + "prescription) VALUES (?, ?, ?, ?, ?, ?)",
                c.getPatientId(), c.getMedecinId(), c.getDate(), c.getMotif(), c.getDiagnostic(),
                c.getPrescription()));
        return c;
    }

    @Override public Optional<Consultation> findById(long id) {
        return queryOne("SELECT " + COLS + " FROM consultation WHERE id = ?", id);
    }

    @Override public List<Consultation> findAll() {
        return query("SELECT " + COLS + " FROM consultation ORDER BY date_consultation DESC, id DESC");
    }

    @Override public List<Consultation> findByPatient(long patientId) {
        return query("SELECT " + COLS + " FROM consultation WHERE patient_id = ? "
                + "ORDER BY date_consultation DESC, id DESC", patientId);
    }

    @Override public List<Consultation> findByMedecin(long medecinId) {
        return query("SELECT " + COLS + " FROM consultation WHERE medecin_id = ? "
                + "ORDER BY date_consultation DESC, id DESC", medecinId);
    }

    @Override public boolean update(Consultation c) {
        return update("UPDATE consultation SET patient_id = ?, medecin_id = ?, date_consultation = ?, motif = ?, "
                        + "diagnostic = ?, prescription = ? WHERE id = ?",
                c.getPatientId(), c.getMedecinId(), c.getDate(), c.getMotif(), c.getDiagnostic(),
                c.getPrescription(), c.getId()) == 1;
    }

    @Override public boolean delete(long id) {
        return update("DELETE FROM consultation WHERE id = ?", id) == 1;
    }
}
