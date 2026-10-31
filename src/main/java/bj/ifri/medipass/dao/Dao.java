package bj.ifri.medipass.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic Data Access Object: the rest of the application only talks to this
 * interface and never knows whether data comes from MySQL or from memory.
 *
 * @param <T> entity type
 */
public interface Dao<T> {
    /** Inserts the entity, sets its generated id and returns it. */
    T create(T entity);

    Optional<T> findById(long id);

    List<T> findAll();

    /** @return true if a row was updated */
    boolean update(T entity);

    /** @return true if a row was deleted */
    boolean delete(long id);
}
