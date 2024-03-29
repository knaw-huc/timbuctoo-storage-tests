package nl.knaw.huc.timbuctoo.bdb.datastores.implementations.bdb;

import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbWrapper;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.DatabaseGetter;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.exceptions.DatabaseWriteException;
import nl.knaw.huc.timbuctoo.util.DataStoreCreationException;
import nl.knaw.huc.timbuctoo.util.CursorQuad;
import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.Graph;

import java.util.Optional;
import java.util.stream.Stream;

import static nl.knaw.huc.timbuctoo.bdb.berkeleydb.DatabaseGetter.Iterate.BACKWARDS;
import static nl.knaw.huc.timbuctoo.bdb.berkeleydb.DatabaseGetter.Iterate.FORWARDS;

/**
 * This datastore determines the current state of the DataSet.
 */
public class BdbQuadStore {
  private final BdbWrapper<String, String> bdbWrapper;

  public BdbQuadStore(BdbWrapper<String, String> rdfData)
    throws DataStoreCreationException {
    this.bdbWrapper = rdfData;
  }

  public Stream<CursorQuad> getQuads(String subject) {
    return getQuadsInGraph(subject, Optional.empty());
  }

  public Stream<CursorQuad> getQuads(String subject, String predicate, Direction direction, String cursor) {
    return getQuadsInGraph(subject, predicate, direction, cursor, Optional.empty());
  }

  public Stream<CursorQuad> getQuadsInGraph(String subject, Optional<Graph> graph) {
    return bdbWrapper.databaseGetter()
                     .partialKey(subject + "\n", (prefix, key) -> key.startsWith(prefix))
                     .dontSkip()
                     .forwards()
                     .getKeysAndValues(bdbWrapper.keyValueConverter(this::formatResult))
                     .filter(quad -> quad.inGraph(graph));
  }

  public Stream<CursorQuad> getQuadsInGraph(String subject, String predicate,
                                            Direction direction, String cursor, Optional<Graph> graph) {
    final DatabaseGetter<String, String> getter;
    if (cursor.isEmpty()) {
      getter = bdbWrapper.databaseGetter()
                         .key((formatKey(subject, predicate, direction)))
                         .dontSkip()
                         .forwards();
    } else {
      if (cursor.equals("LAST")) {
        getter = bdbWrapper.databaseGetter()
                           .key((formatKey(subject, predicate, direction)))
                           .skipToEnd()
                           .backwards();
      } else {
        String[] fields = cursor.substring(2).split("\n\n", 2);
        getter = bdbWrapper.databaseGetter()
                           .key(fields[0])
                           .skipToValue(fields[1])
                           .skipOne() //we start after the cursor
                           .direction(cursor.startsWith("A\n") ? FORWARDS : BACKWARDS);
      }
    }

    return getter.getKeysAndValues(bdbWrapper.keyValueConverter(this::formatResult))
                 .filter(quad -> quad.inGraph(graph));
  }

  public Stream<CursorQuad> getAllQuads() {
    return getAllQuadsInGraph(Optional.empty());
  }

  public Stream<CursorQuad> getAllQuadsInGraph(Optional<Graph> graph) {
    return bdbWrapper.databaseGetter()
                     .getAll()
                     .getKeysAndValues(bdbWrapper.keyValueConverter(this::formatResult))
                     .filter(quad -> quad.inGraph(graph));
  }

  public void close() {
    try {
      bdbWrapper.close();
    } catch (Exception e) {
      //LOG.error("Exception closing BdbQuadStore", e);
    }
  }

  private CursorQuad formatResult(String key, String value) {
    String cursor = key + "\n\n" + value;
    String[] keyFields = key.split("\n", 3);
    String[] valueFields = value.split("\n", 4);
    return CursorQuad.create(
      keyFields[0],
      keyFields[1],
      Direction.valueOf(keyFields[2]),
      valueFields[3],
      valueFields[0].isEmpty() ? null : valueFields[0],
      valueFields[1].isEmpty() ? null : valueFields[1],
      valueFields[2].isEmpty() ? null : valueFields[2],
      cursor
    );
  }

  public boolean putQuad(String subject, String predicate, Direction direction, String object, String dataType,
                         String language, String graph) throws DatabaseWriteException {
    String value = formatValue(object, dataType, language, graph);
    return bdbWrapper.put(formatKey(subject, predicate, direction), value);
  }

  public boolean deleteQuad(String subject, String predicate, Direction direction, String object, String dataType,
                         String language, String graph) throws DatabaseWriteException {
    String value = formatValue(object, dataType, language, graph);
    return bdbWrapper.delete(formatKey(subject, predicate, direction), value);
  }

  public String formatKey(String subject, String predicate, Direction direction) {
    return subject + "\n" + predicate + "\n" + (direction == null ? "" : direction.name());
  }

  public String formatValue(String object, String dataType, String language, String graph) {
    return (dataType == null ? "" : dataType) + "\n" + (language == null ? "" : language) + "\n" +
        (graph == null ? "" : graph) + "\n" + object;
  }

  public int compare(CursorQuad leftQ, CursorQuad rightQ) {
    final String leftStr = formatKey(leftQ.getSubject(), leftQ.getPredicate(), leftQ.getDirection()) + "\n" +
      formatValue(leftQ.getObject(), leftQ.getValuetype().orElse(null),
          leftQ.getLanguage().orElse(null), leftQ.getGraph().orElse(null));
    final String rightStr = formatKey(rightQ.getSubject(), rightQ.getPredicate(), rightQ.getDirection()) + "\n" +
      formatValue(rightQ.getObject(), rightQ.getValuetype().orElse(null),
          rightQ.getLanguage().orElse(null), rightQ.getGraph().orElse(null));
    return leftStr.compareTo(rightStr);
  }

  public void commit() {
    bdbWrapper.commit();
  }

  public boolean isClean() {
    return bdbWrapper.isClean();
  }

  public void start() {
    bdbWrapper.beginTransaction();
  }

  public void empty() {
    bdbWrapper.empty();
  }
}
