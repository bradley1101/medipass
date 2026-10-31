package bj.ifri.medipass.dao.jdbc;

import bj.ifri.medipass.dao.MedecinDao;
import bj.ifri.medipass.model.Medecin;
import java.util.List;
import java.util.Optional;

public class JdbcMedecinDao extends JdbcSupport<Medecin> implements MedecinDao {

    private static final String COLS = "id, nom, prenom, specialite, telephone";

    @Override protected RowMapper<Medecin> mapper() {
        return rs -> {
            Medecin m = new Medecin(rs.getString("nom"), rs.getString("prenom"),
                    rs.getString("specialite"), rs.getString("telephone"));
            m.setId(rs.getLong("id"));
            return m;
        };
    }

    @Override public Medecin create(Medecin m) {
        m.setId(insert("INSERT INTO medecin (nom, prenom, specialite, telephone) VALUES (?, ?, ?, ?)",
                m.getNom(), m.getPrenom(), m.getSpecialite(), m.getTelephone()));
        return m;
    }

    @Override public Optional<Medecin> findById(long id) {
        return queryOne("SELECT " + COLS + " FROM medecin WHERE id = ?", id);
    }

    @Override public List<Medecin> findAll() {
        return query("SELECT " + COLS + " FROM medecin ORDER BY nom, prenom");
    }

    @Override public List<Medecin> findBySpecialite(String specialite) {
        return query("SELECT " + COLS + " FROM medecin WHERE LOWER(specialite) = LOWER(?) ORDER BY nom",
                specialite);
    }

    @Override public boolean update(Medecin m) {
        return update("UPDATE medecin SET nom = ?, prenom = ?, specialite = ?, telephone = ? WHERE id = ?",
                m.getNom(), m.getPrenom(), m.getSpecialite(), m.getTelephone(), m.getId()) == 1;
    }

    @Override public boolean delete(long id) {
        return update("DELETE FROM medecin WHERE id = ?", id) == 1;
    }
}
