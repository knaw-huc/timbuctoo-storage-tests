package nl.knaw.huc.timbuctoo.bdb.datastores.quadstore.dto;

//import org.immutables.value.Value;

import nl.knaw.huc.timbuctoo.bdb.datastores.CursorValue;
import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.Graph;

import java.util.Optional;

//@Value.Immutable
public interface CursorQuad extends CursorValue {
  String getSubject();

  String getPredicate();

  String getObject();

  Optional<String> getValuetype();

  Optional<String> getLanguage();

  Optional<String> getGraph();

  Direction getDirection();

  //@Value.Auxiliary
  ChangeType getChangeType();

  //@Value.Auxiliary
  String getCursor();

  static CursorQuad create(String subject, String predicate, Direction direction, String object, String valueType,
                           String language, String graph, String cursor) {
    return create(subject, predicate, direction, ChangeType.UNCHANGED, object, valueType, language, graph, cursor);
  }

  static CursorQuad create(String subject, String predicate, Direction direction, ChangeType changeType, String object,
                           String valueType, String language, String graph, String cursor) {
    return ImmutableCursorQuad.builder()
      .subject(subject)
      .predicate(predicate)
      .object(object)
      .valuetype(Optional.ofNullable(valueType))
      .language(Optional.ofNullable(language))
      .graph(Optional.ofNullable(graph))
      .cursor(cursor)
      .direction(direction)
      .changeType(changeType)
      .build();
  }

  default boolean inGraph(Optional<Graph> graph) {
    if (graph.isEmpty()) {
      return true;
    }

    Graph filterGraph = graph.get();
    return (getGraph().isEmpty() && filterGraph.isDefaultGraph()) ||
        (getGraph().isPresent() && getGraph().get().equals(filterGraph.getUri()));
  }
}
