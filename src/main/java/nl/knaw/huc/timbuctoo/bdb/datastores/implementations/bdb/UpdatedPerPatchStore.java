package nl.knaw.huc.timbuctoo.bdb.datastores.implementations.bdb;

import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbWrapper;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.exceptions.DatabaseWriteException;
import nl.knaw.huc.timbuctoo.util.DataStoreCreationException;

import java.util.stream.Stream;

public class UpdatedPerPatchStore {
  private final BdbWrapper<Integer, String> bdbWrapper;

  public UpdatedPerPatchStore(BdbWrapper<Integer, String> bdbWrapper)
    throws DataStoreCreationException {

    this.bdbWrapper = bdbWrapper;
  }

  public void put(int currentversion, String subject) throws DatabaseWriteException {
    bdbWrapper.put(currentversion, subject);
  }

  public Stream<String> ofVersion(int version) {
    return bdbWrapper.databaseGetter().key(version).dontSkip().forwards().getValues(bdbWrapper.valueRetriever());
  }

  public Stream<String> subjectsOfVersion(int version) {
    bdbWrapper.databaseGetter().key(version).dontSkip().forwards().getValues(bdbWrapper.valueRetriever()).forEach(
      value -> {

      }
    );
    return bdbWrapper.databaseGetter().key(version).dontSkip().forwards().getValues(bdbWrapper.valueRetriever());
  }

  public Stream<Integer> getVersions() {
    return bdbWrapper.databaseGetter().getAll().getKeys(bdbWrapper.keyRetriever()).distinct();
  }

  public void close() {
    try {
      bdbWrapper.close();
    } catch (Exception e) {
      //LOG.error("Exception closing UpdatedPerPatchStore", e);
    }
  }

  public void commit() {
    bdbWrapper.commit();
  }

  public void start() {
    bdbWrapper.beginTransaction();
  }

  public boolean isClean() {
    return bdbWrapper.isClean();
  }

  public void empty() {
    bdbWrapper.empty();
  }
}
