package bj.ifri.medipass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import bj.ifri.medipass.dao.DaoFactory;
import bj.ifri.medipass.dao.PatientDao;
import bj.ifri.medipass.model.Patient;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class MemoryDaoTest {

    private PatientDao dao;

    @Before public void setUp() {
        dao = DaoFactory.of(DaoFactory.Type.MEMORY).getPatientDao();
    }

    private Patient patient(String nom, String prenom) {
        return new Patient(nom, prenom, LocalDate.of(2000, 1, 1), 'F', "", "O+", "");
    }

    @Test public void createAssignsIncrementalIds() {
        assertEquals(Long.valueOf(1), dao.create(patient("Zinsou", "Ella")).getId());
        assertEquals(Long.valueOf(2), dao.create(patient("Akpo", "Jean")).getId());
    }

    @Test public void findAllIsSortedByName() {
        dao.create(patient("Zinsou", "Ella"));
        dao.create(patient("akpo", "Jean"));
        assertEquals("akpo", dao.findAll().get(0).getNom());
    }

    @Test public void searchIsCaseInsensitiveOnBothNames() {
        dao.create(patient("Zinsou", "Ella"));
        dao.create(patient("Akpo", "Jean"));
        assertEquals(1, dao.searchByName("ELL").size());
        assertEquals(1, dao.searchByName("akp").size());
        assertEquals(0, dao.searchByName("xyz").size());
    }

    @Test public void updateAndDelete() {
        Patient p = dao.create(patient("Zinsou", "Ella"));
        p.setTelephone("+229 01 99 99 99");
        assertTrue(dao.update(p));
        assertEquals("+229 01 99 99 99", dao.findById(p.getId()).orElseThrow().getTelephone());
        assertTrue(dao.delete(p.getId()));
        assertFalse(dao.findById(p.getId()).isPresent());
        assertFalse(dao.delete(p.getId()));
    }

    @Test public void updateUnknownEntityReturnsFalse() {
        Patient ghost = patient("X", "Y");
        ghost.setId(42L);
        assertFalse(dao.update(ghost));
    }
}
