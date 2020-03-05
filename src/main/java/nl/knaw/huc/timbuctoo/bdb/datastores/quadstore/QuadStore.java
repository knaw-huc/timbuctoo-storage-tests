package nl.knaw.huc.timbuctoo.bdb.datastores.quadstore;


import nl.knaw.huc.timbuctoo.bdb.datastores.quadstore.dto.CursorQuad;
import nl.knaw.huc.timbuctoo.util.Direction;

import java.util.stream.Stream;

public interface QuadStore {
  Stream<CursorQuad> getQuads(String subject, String predicate, Direction direction, String cursor);

  Stream<CursorQuad> getQuads(String subject);

  Stream<CursorQuad> getAllQuads();

  void close();

  void commit();

  boolean isClean();
}
