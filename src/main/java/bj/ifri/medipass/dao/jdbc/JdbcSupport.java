package bj.ifri.medipass.dao.jdbc;

import bj.ifri.medipass.dao.DaoException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Small template shared by the JDBC DAOs (prepared statements only: no SQL injection). */
abstract class JdbcSupport<T> {

    @FunctionalInterface
    interface RowMapper<T> { T map(ResultSet rs) throws SQLException; }

    protected abstract RowMapper<T> mapper();

    protected List<T> query(String sql, Object... params) {
        try (Connection c = ConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> list = new ArrayList<>();
                while (rs.next()) list.add(mapper().map(rs));
                return list;
            }
        } catch (SQLException e) {
            throw new DaoException("Query failed: " + sql, e);
        }
    }

    protected Optional<T> queryOne(String sql, Object... params) {
        return query(sql, params).stream().findFirst();
    }

    protected int update(String sql, Object... params) {
        try (Connection c = ConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Update failed: " + sql, e);
        }
    }

    protected long insert(String sql, Object... params) {
        try (Connection c = ConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, params);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new DaoException("No generated key returned", null);
            }
        } catch (SQLException e) {
            throw new DaoException("Insert failed: " + sql, e);
        }
    }

    private static void bind(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p instanceof LocalDate d) ps.setDate(i + 1, Date.valueOf(d));
            else if (p instanceof Character ch) ps.setString(i + 1, String.valueOf(ch));
            else ps.setObject(i + 1, p);
        }
    }

    protected static LocalDate date(ResultSet rs, String col) throws SQLException {
        Date d = rs.getDate(col);
        return d == null ? null : d.toLocalDate();
    }
}
