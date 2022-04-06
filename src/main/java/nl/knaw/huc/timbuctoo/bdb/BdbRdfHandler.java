package nl.knaw.huc.timbuctoo.bdb;

import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbEnvironmentCreator;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbPersistentEnvironmentCreator;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.exceptions.DatabaseWriteException;
import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.RDFHandler;

public class BdbRdfHandler extends RDFHandler {
    public final BdbDataSource bdbDataSource;
    private final int version;

    public BdbRdfHandler(String userId, String dataSetId, String baseUri, String defaultGraph, String fileName,
                         String databaseLocation, int version) {
        super(baseUri, defaultGraph, fileName);
        this.version = version;

        BdbEnvironmentCreator bdbEnvironmentCreator = new BdbPersistentEnvironmentCreator(databaseLocation);
        bdbEnvironmentCreator.start();

        bdbDataSource = new BdbDataSource(bdbEnvironmentCreator, userId, dataSetId);

        bdbDataSource.quadStore.isClean();
        bdbDataSource.truePatchStore.isClean();
        bdbDataSource.quadStore.isClean();
    }

    public void commit() {
        bdbDataSource.commit();
    }

    protected void putQuad(String subject, String predicate, Direction direction, String object, String valueType,
                           String language, String graph) throws Exception {
        try {
            final boolean wasChanged = bdbDataSource.quadStore.putQuad(subject, predicate, direction, object, valueType, language, graph);
            if (wasChanged) {
                bdbDataSource.truePatchStore.put(subject, version, predicate, direction, true, object, valueType, language, graph);
                bdbDataSource.updatedPerPatchStore.put(version, subject);
            }
        } catch (DatabaseWriteException e) {
            throw new Exception(e);
        }
    }

    protected void deleteQuad(String subject, String predicate, Direction direction, String object, String valueType,
                              String language, String graph) throws Exception {
        try {
            final boolean wasChanged = bdbDataSource.quadStore.deleteQuad(subject, predicate, direction, object, valueType, language, graph);
            if (wasChanged) {
                bdbDataSource.truePatchStore.put(subject, version, predicate, direction, false, object, valueType, language, graph);
                bdbDataSource.updatedPerPatchStore.put(version, subject);
            }
        } catch (DatabaseWriteException e) {
            throw new Exception(e);
        }
    }
}
