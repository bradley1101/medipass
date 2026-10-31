package bj.ifri.medipass.dao;

/** Unchecked wrapper so that SQLException does not leak out of the DAO layer. */
public class DaoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
