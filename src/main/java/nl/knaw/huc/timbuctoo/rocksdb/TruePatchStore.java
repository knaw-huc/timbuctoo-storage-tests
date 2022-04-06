package nl.knaw.huc.timbuctoo.rocksdb;

import nl.knaw.huc.timbuctoo.util.ChangeType;
import nl.knaw.huc.timbuctoo.util.CursorQuad;
import nl.knaw.huc.timbuctoo.util.Direction;
import org.rocksdb.AbstractImmutableNativeReference;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.knaw.huc.timbuctoo.util.Direction.IN;
import static nl.knaw.huc.timbuctoo.util.Direction.OUT;
import static nl.knaw.huc.timbuctoo.util.StreamIterator.stream;

public class TruePatchStore {
    private final RocksDB db;
    private final DatabaseCreator creator;
    private final HashMap<Integer, ColumnFamilyHandle> columnFamilies;

    public TruePatchStore(RocksDB db, UpdatedPerPatchStore updatedPerPatchStore, DatabaseCreator creator) throws RocksDBException {
        this.db = db;
        this.creator = creator;
        this.columnFamilies = new HashMap<>();

        try (final Stream<Integer> versions = updatedPerPatchStore.getVersions()) {
            for (Integer version : versions.collect(Collectors.toList())) {
                columnFamilies.put(version, this.creator.createDatabase(String.valueOf(version)));
            }
        }
    }

    public void put(String subject, int version, String predicate, Direction direction, boolean isAssertion,
                    String object, String valueType, String language, String graph) throws RocksDBException {
        final String start = subject + "\n" + version + "\n";
        final String end = "\n" + predicate + "\n" +
                (direction == OUT ? "1" : "0") + "\n" +
                (valueType == null ? "" : valueType) + "\n" +
                (language == null ? "" : language) + "\n" +
                (graph == null ? "" : graph) + "\n" +
                object;

        ColumnFamilyHandle cf = getOrCreateColumnFamily(version);
        db.delete(cf, (start + (!isAssertion ? 1 : 0) + end).getBytes(StandardCharsets.UTF_8));
        db.put(cf, (start + (isAssertion ? 1 : 0) + end).getBytes(StandardCharsets.UTF_8), new byte[]{});
    }

    public Stream<CursorQuad> getChangesOfVersion(int version, boolean assertions) {
        if (columnFamilies.containsKey(version)) {
            final ColumnFamilyHandle cf = columnFamilies.get(version);
            return stream(new RocksDBIterator<>(db.newIterator(cf), (key, value) -> makeCursorQuad(key)))
                    .filter(quad -> assertions
                            ? quad.getChangeType() == ChangeType.ASSERTED
                            : quad.getChangeType() == ChangeType.RETRACTED);
        }

        return Stream.empty();
    }

    public void close() {
        columnFamilies.values().forEach(AbstractImmutableNativeReference::close);
    }

    public interface DatabaseCreator {
        ColumnFamilyHandle createDatabase(String version) throws RocksDBException;
    }

    private ColumnFamilyHandle getOrCreateColumnFamily(int version) throws RocksDBException {
        if (columnFamilies.containsKey(version)) {
            return columnFamilies.get(version);
        }

        ColumnFamilyHandle cf = creator.createDatabase(String.valueOf(version));
        columnFamilies.put(version, cf);

        return cf;
    }

    private CursorQuad makeCursorQuad(byte[] resultBytes) {
        String result = new String(resultBytes, StandardCharsets.UTF_8);
        String[] parts = result.split("\n", 9);
        Direction direction = parts[4].charAt(0) == '1' ? OUT : IN;
        ChangeType changeType = parts[2].charAt(0) == '1' ? ChangeType.ASSERTED : ChangeType.RETRACTED;
        return CursorQuad.create(
                parts[0],
                parts[3],
                direction,
                changeType,
                parts[8],
                parts[5].isEmpty() ? null : parts[5],
                parts[6].isEmpty() ? null : parts[6],
                parts[7].isEmpty() ? null : parts[7],
                ""
        );
    }
}
