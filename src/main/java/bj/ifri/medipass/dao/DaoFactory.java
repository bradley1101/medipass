package bj.ifri.medipass.dao;

import bj.ifri.medipass.dao.jdbc.JdbcDaoFactory;
import bj.ifri.medipass.dao.memory.MemoryDaoFactory;

/**
 * Abstract Factory: gives the DAOs of one storage technology.
 * Switching from MySQL to memory is a one-line change in medipass.properties.
 */
public abstract class DaoFactory {

    public enum Type { JDBC, MEMORY }

    public abstract PatientDao getPatientDao();
    public abstract MedecinDao getMedecinDao();
    public abstract ConsultationDao getConsultationDao();

    public static DaoFactory of(Type type) {
        return switch (type) {
            case JDBC -> new JdbcDaoFactory();
            case MEMORY -> new MemoryDaoFactory();
        };
    }
}
