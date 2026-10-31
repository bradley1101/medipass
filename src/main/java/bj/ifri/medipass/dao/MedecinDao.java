package bj.ifri.medipass.dao;

import bj.ifri.medipass.model.Medecin;
import java.util.List;

public interface MedecinDao extends Dao<Medecin> {
    List<Medecin> findBySpecialite(String specialite);
}
