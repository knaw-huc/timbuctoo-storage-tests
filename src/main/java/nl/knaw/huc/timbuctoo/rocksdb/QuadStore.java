package nl.knaw.huc.timbuctoo.rocksdb;

import nl.knaw.huc.timbuctoo.bdb.berkeleydb.DatabaseGetter;
import nl.knaw.huc.timbuctoo.util.CursorQuad;
import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.Graph;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Stream;

import static nl.knaw.huc.timbuctoo.bdb.berkeleydb.DatabaseGetter.Iterate.BACKWARDS;
import static nl.knaw.huc.timbuctoo.bdb.berkeleydb.DatabaseGetter.Iterate.FORWARDS;
import static nl.knaw.huc.timbuctoo.util.StreamIterator.stream;

public class QuadStore {
    private final RocksDB db;
    private final ColumnFamilyHandle cf;

    public QuadStore(RocksDB db, ColumnFamilyHandle cf) {
        this.db = db;
        this.cf = cf;
    }

    public boolean hasQuad(String subject, String predicate, Direction direction, String object, String dataType,
                           String language, String graph) throws RocksDBException {
        return db.get(cf, formatQuad(subject, predicate, direction, object, dataType, language, graph)) != null;
    }

    public void putQuad(String subject, String predicate, Direction direction, String object, String dataType,
                        String language, String graph) throws RocksDBException {
        db.put(cf, formatQuad(subject, predicate, direction, object, dataType, language, graph), new byte[]{});
    }

    public void deleteQuad(String subject, String predicate, Direction direction, String object, String dataType,
                           String language, String graph) throws RocksDBException {
        db.delete(cf, formatQuad(subject, predicate, direction, object, dataType, language, graph));
    }

    public Stream<CursorQuad> getAllQuads() {
        return stream(new RocksDBIterator<>(db.newIterator(cf), (key, value) -> formatResult(key)));
    }

    public Stream<CursorQuad> getQuads(String subject) {
        return stream(new RocksDBIterator<>(db.newIterator(cf), (key, value) -> formatResult(key),
                (subject + "\n").getBytes(StandardCharsets.UTF_8)));
    }

    public Stream<CursorQuad> getQuads(String subject, String predicate, Direction direction) {
        return stream(new RocksDBIterator<>(db.newIterator(cf), (key, value) -> formatResult(key),
                (subject + "\n" + predicate + "\n" + (direction == null ? "" : direction.name()) + "\n")
                        .getBytes(StandardCharsets.UTF_8)));
    }

    public void close() {
        cf.close();
    }

    private static byte[] formatQuad(String subject, String predicate, Direction direction, String object, String dataType, String language, String graph) {
        String serialized = subject + "\n" + predicate + "\n" + (direction == null ? "" : direction.name()) + "\n" +
                (dataType == null ? "" : dataType) + "\n" + (language == null ? "" : language) + "\n" +
                (graph == null ? "" : graph) + "\n" + object;
        return serialized.getBytes(StandardCharsets.UTF_8);
    }

    private CursorQuad formatResult(byte[] resultBytes) {
        String result = new String(resultBytes, StandardCharsets.UTF_8);
        String[] results = result.split("\n", 7);
        return CursorQuad.create(
                results[0],
                results[1],
                Direction.valueOf(results[2]),
                results[6],
                results[3].isEmpty() ? null : results[3],
                results[4].isEmpty() ? null : results[4],
                results[5].isEmpty() ? null : results[5],
                result
        );
    }
}
