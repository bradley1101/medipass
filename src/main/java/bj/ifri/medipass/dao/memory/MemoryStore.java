package bj.ifri.medipass.dao.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;

/** Generic in-memory table with auto-increment ids. */
class MemoryStore<T> {
    private final Map<Long, T> rows = new TreeMap<>();
    private final AtomicLong seq = new AtomicLong();
    private final Function<T, Long> getId;
    private final BiConsumer<T, Long> setId;

    MemoryStore(Function<T, Long> getId, BiConsumer<T, Long> setId) {
        this.getId = getId;
        this.setId = setId;
    }

    synchronized T create(T e) {
        setId.accept(e, seq.incrementAndGet());
        rows.put(getId.apply(e), e);
        return e;
    }

    synchronized Optional<T> find(long id) { return Optional.ofNullable(rows.get(id)); }

    synchronized List<T> all() { return new ArrayList<>(rows.values()); }

    synchronized boolean update(T e) {
        Long id = getId.apply(e);
        if (id == null || !rows.containsKey(id)) return false;
        rows.put(id, e);
        return true;
    }

    synchronized boolean delete(long id) { return rows.remove(id) != null; }
}
