package bj.ifri.medipass.dao.jdbc;

import bj.ifri.medipass.dao.ConsultationDao;
import bj.ifri.medipass.dao.DaoFactory;
import bj.ifri.medipass.dao.MedecinDao;
import bj.ifri.medipass.dao.PatientDao;

public class JdbcDaoFactory extends DaoFactory {
    @Override public PatientDao getPatientDao() { return new JdbcPatientDao(); }
    @Override public MedecinDao getMedecinDao() { return new JdbcMedecinDao(); }
    @Override public ConsultationDao getConsultationDao() { return new JdbcConsultationDao(); }
}
