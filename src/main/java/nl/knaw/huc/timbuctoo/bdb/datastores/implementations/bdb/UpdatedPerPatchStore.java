package nl.knaw.huc.timbuctoo.bdb.datastores.implementations.bdb;

import nl.knaw.huc.timbuctoo.util.SubjectCursor;
import nl.knaw.huc.timbuctoo.util.DataStoreCreationException;
import nl.knaw.huc.timbuctoo.util.Streams;
import nl.knaw.huc.timbuctoo.util.Tuple;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbWrapper;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.exceptions.DatabaseWriteException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UpdatedPerPatchStore {
  private final BdbWrapper<String, Integer> bdbWrapper;

  public UpdatedPerPatchStore(BdbWrapper<String, Integer> bdbWrapper) throws DataStoreCreationException {
    this.bdbWrapper = bdbWrapper;
  }

  public void put(int version, String subject) throws DatabaseWriteException {
    bdbWrapper.put(subject, version);
  }

  public Stream<String> ofVersion(int version) {
    return bdbWrapper.databaseGetter().getAll()
                     .getKeysAndValues(bdbWrapper.keyValueConverter(Tuple::new))
                     .filter(tuple -> tuple.getRight() == version)
                     .map(Tuple::getLeft);
  }

  public Stream<SubjectCursor> fromVersion(int version, String cursor) {
    Stream<Tuple<String, Integer>> stream;
    String startSubject = !cursor.isEmpty() ? cursor.substring(2) : null;
    if (startSubject != null) {
      stream = bdbWrapper.databaseGetter()
                         .skipToKey(startSubject)
                         .skipToEnd()
                         .skipOne()
                         .forwards()
                         .getKeysAndValues(bdbWrapper.keyValueConverter(Tuple::new));
    } else {
      stream = bdbWrapper.databaseGetter().getAll()
                         .getKeysAndValues(bdbWrapper.keyValueConverter(Tuple::new));
    }

    return Streams.combine(stream, (scA, scB) -> scA.getLeft().equals(scB.getLeft()))
                  .map(UpdatedPerPatchStore::makeSubjectCursor)
                  .filter(subjectCursor -> subjectCursor.getVersions().stream().anyMatch(v -> v >= version));
  }

  public Stream<Integer> getVersions() {
    return bdbWrapper.databaseGetter().getAll()
                     .getKeysAndValues(bdbWrapper.keyValueConverter(Tuple::new))
                     .map(Tuple::getRight).distinct();
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

  private static SubjectCursor makeSubjectCursor(Set<Tuple<String, Integer>> subjectVersions) {
    Optional<String> subject = subjectVersions.stream().findAny().map(Tuple::getLeft);
    Set<Integer> versions = subjectVersions.stream().map(Tuple::getRight).collect(Collectors.toSet());
    return subject.map(s -> SubjectCursor.create(s, versions)).orElse(null);
  }
}
