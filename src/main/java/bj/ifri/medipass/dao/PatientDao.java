package bj.ifri.medipass.dao;

import bj.ifri.medipass.model.Patient;
import java.util.List;

public interface PatientDao extends Dao<Patient> {
    /** Case-insensitive search on last name or first name. */
    List<Patient> searchByName(String text);
}
