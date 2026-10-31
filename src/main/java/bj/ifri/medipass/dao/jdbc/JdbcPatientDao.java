package bj.ifri.medipass.dao.jdbc;

import bj.ifri.medipass.dao.PatientDao;
import bj.ifri.medipass.model.Patient;
import java.util.List;
import java.util.Optional;

public class JdbcPatientDao extends JdbcSupport<Patient> implements PatientDao {

    private static final String COLS =
            "id, nom, prenom, date_naissance, sexe, telephone, groupe_sanguin, allergies";

    @Override protected RowMapper<Patient> mapper() {
        return rs -> {
            Patient p = new Patient(rs.getString("nom"), rs.getString("prenom"),
                    date(rs, "date_naissance"), rs.getString("sexe").charAt(0),
                    rs.getString("telephone"), rs.getString("groupe_sanguin"), rs.getString("allergies"));
            p.setId(rs.getLong("id"));
            return p;
        };
    }

    @Override public Patient create(Patient p) {
        long id = insert("INSERT INTO patient (nom, prenom, date_naissance, sexe, telephone, groupe_sanguin, allergies) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                p.getNom(), p.getPrenom(), p.getDateNaissance(), p.getSexe(),
                p.getTelephone(), p.getGroupeSanguin(), p.getAllergies());
        p.setId(id);
        return p;
    }

    @Override public Optional<Patient> findById(long id) {
        return queryOne("SELECT " + COLS + " FROM patient WHERE id = ?", id);
    }

    @Override public List<Patient> findAll() {
        return query("SELECT " + COLS + " FROM patient ORDER BY nom, prenom");
    }

    @Override public List<Patient> searchByName(String text) {
        String like = "%" + text.toLowerCase() + "%";
        return query("SELECT " + COLS + " FROM patient WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? "
                + "ORDER BY nom, prenom", like, like);
    }

    @Override public boolean update(Patient p) {
        return update("UPDATE patient SET nom = ?, prenom = ?, date_naissance = ?, sexe = ?, telephone = ?, "
                        + "groupe_sanguin = ?, allergies = ? WHERE id = ?",
                p.getNom(), p.getPrenom(), p.getDateNaissance(), p.getSexe(), p.getTelephone(),
                p.getGroupeSanguin(), p.getAllergies(), p.getId()) == 1;
    }

    @Override public boolean delete(long id) {
        return update("DELETE FROM patient WHERE id = ?", id) == 1;
    }
}
