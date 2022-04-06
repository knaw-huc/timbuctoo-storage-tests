package nl.knaw.huc.timbuctoo.rocksdb;

import nl.knaw.huc.timbuctoo.util.Tuple;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static nl.knaw.huc.timbuctoo.util.StreamIterator.stream;

public class UpdatedPerPatchStore {
    private final RocksDB db;
    private final ColumnFamilyHandle cf;

    public UpdatedPerPatchStore(RocksDB db, ColumnFamilyHandle cf) {
        this.db = db;
        this.cf = cf;
    }

    public void close() {
        cf.close();
    }

    public void put(int version, String subject) throws RocksDBException {
        db.put(cf, subject.getBytes(StandardCharsets.UTF_8), BigInteger.valueOf(version).toByteArray());
    }

    public Stream<String> ofVersion(int version) {
        return stream(new RocksDBIterator<>(db.newIterator(cf), this::formatResult))
                .filter(tuple -> tuple.getRight() == version)
                .map(Tuple::getLeft);
    }

    public Stream<Integer> getVersions() {
        return stream(new RocksDBIterator<>(db.newIterator(cf), this::formatResult))
                .map(Tuple::getRight).distinct();
    }

    private Tuple<String, Integer> formatResult(byte[] subjectBytes, byte[] versionBytes) {
        String subject = new String(subjectBytes, StandardCharsets.UTF_8);
        Integer version = new BigInteger(versionBytes).intValue();
        return new Tuple<>(subject, version);
    }
}
