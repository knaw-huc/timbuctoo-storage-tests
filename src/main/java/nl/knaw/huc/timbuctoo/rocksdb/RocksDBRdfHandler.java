package nl.knaw.huc.timbuctoo.rocksdb;

import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.RDFHandler;

public class RocksDBRdfHandler extends RDFHandler {
    public final DataSource dataSource;
    private final int version;

    public RocksDBRdfHandler(String baseUri, String fileName, String databaseLocation, int version) throws Exception {
        super(baseUri, null, fileName);
        this.version = version;
        dataSource = new DataSource(databaseLocation);
    }

    public void commit() {
    }

    protected void putQuad(String subject, String predicate, Direction direction, String object, String valueType,
                           String language, String graph) throws Exception {
        if (!dataSource.quadStore.hasQuad(subject, predicate, direction, object, valueType, language, graph)) {
            dataSource.quadStore.putQuad(subject, predicate, direction, object, valueType, language, graph);
            dataSource.truePatchStore.put(subject, version, predicate, direction, true, object, valueType, language, graph);
            dataSource.updatedPerPatchStore.put(version, subject);
        }
    }

    protected void deleteQuad(String subject, String predicate, Direction direction, String object, String valueType,
                              String language, String graph) throws Exception {
        if (dataSource.quadStore.hasQuad(subject, predicate, direction, object, valueType, language, graph)) {
            dataSource.quadStore.deleteQuad(subject, predicate, direction, object, valueType, language, graph);
            dataSource.truePatchStore.put(subject, version, predicate, direction, false, object, valueType, language, graph);
            dataSource.updatedPerPatchStore.put(version, subject);
        }
    }
}
