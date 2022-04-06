package nl.knaw.huc.timbuctoo.rocksdb;

import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;

public class DataSource {
    private final RocksDB db;

    public final QuadStore quadStore;
    public final UpdatedPerPatchStore updatedPerPatchStore;
    public final TruePatchStore truePatchStore;

    public DataSource(String path) throws Exception {
        RocksDB.loadLibrary();
        db = RocksDB.open(new Options().setCreateIfMissing(true).setCreateMissingColumnFamilies(true), path);

        ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeUniversalStyleCompaction();
        quadStore = new QuadStore(db, db.createColumnFamily(new ColumnFamilyDescriptor("quads".getBytes(), cfOpts)));
        updatedPerPatchStore = new UpdatedPerPatchStore(db, db.createColumnFamily(new ColumnFamilyDescriptor("updatedPerPatch".getBytes(), cfOpts)));
        truePatchStore = new TruePatchStore(db, updatedPerPatchStore,
                version -> db.createColumnFamily(new ColumnFamilyDescriptor(("truePatch" + version).getBytes(), cfOpts)));
    }

    public void close() {
        quadStore.close();
        updatedPerPatchStore.close();
        truePatchStore.close();
        db.close();
    }
}
