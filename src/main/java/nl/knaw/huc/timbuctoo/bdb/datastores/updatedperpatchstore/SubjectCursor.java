package nl.knaw.huc.timbuctoo.bdb.datastores.updatedperpatchstore;

import nl.knaw.huc.timbuctoo.bdb.datastores.CursorValue;

import java.util.Set;

public interface SubjectCursor extends CursorValue {
  String getSubject();

  Set<Integer> getVersions();

  static SubjectCursor create(String subject, Set<Integer> versions) {
    return ImmutableSubjectCursor.builder()
                                 .subject(subject)
                                 .versions(versions)
                                 .cursor(subject)
                                 .build();
  }

  static SubjectCursor create(String subject, Set<Integer> versions, String cursor) {
    return ImmutableSubjectCursor.builder()
                                 .subject(subject)
                                 .versions(versions)
                                 .cursor(cursor)
                                 .build();
  }
}
