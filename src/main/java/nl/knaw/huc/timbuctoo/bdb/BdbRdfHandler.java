package nl.knaw.huc.timbuctoo.bdb;

import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbEnvironmentCreator;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbPersistentEnvironmentCreator;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.exceptions.DatabaseWriteException;
import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.RDFHandler;

public class BdbRdfHandler extends RDFHandler {
    public final BdbDataSource bdbDataSource;
    private final int currentversion;

    public BdbRdfHandler(String userId, String dataSetId, String defaultGraph, String databaseLocation,
                         int currentversion) {
        super(defaultGraph);
        this.currentversion = currentversion;

        BdbEnvironmentCreator bdbEnvironmentCreator = new BdbPersistentEnvironmentCreator(databaseLocation);
        bdbEnvironmentCreator.start();

        bdbDataSource = new BdbDataSource(bdbEnvironmentCreator, userId, dataSetId);

        bdbDataSource.tripleStore.isClean();
        bdbDataSource.truePatchStore.isClean();
        bdbDataSource.tripleStore.isClean();
    }

    protected void putQuad(String subject, String predicate, Direction direction, String object, String valueType,
                           String language) throws Exception {
        try {
            final boolean wasChanged = bdbDataSource.tripleStore.putQuad(subject, predicate, direction, object, valueType, language);
            if (wasChanged) {
                bdbDataSource.truePatchStore.put(subject, currentversion, predicate, direction, true, object, valueType, language);
                bdbDataSource.updatedPerPatchStore.put(currentversion, subject);
            }
        } catch (DatabaseWriteException e) {
            throw new Exception(e);
        }
    }

    protected void deleteQuad(String subject, String predicate, Direction direction, String object, String valueType,
                              String language) throws Exception {
        try {
            final boolean wasChanged = bdbDataSource.tripleStore.deleteQuad(subject, predicate, direction, object, valueType, language);
            if (wasChanged) {
                bdbDataSource.truePatchStore.put(subject, currentversion, predicate, direction, false, object, valueType, language);
                bdbDataSource.updatedPerPatchStore.put(currentversion, subject);
            }
        } catch (DatabaseWriteException e) {
            throw new Exception(e);
        }
    }
}
