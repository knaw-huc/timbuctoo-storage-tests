package nl.knaw.huc.timbuctoo.bdb.datastores.quadstore.dto;

import com.google.common.base.MoreObjects;
//import com.google.errorprone.annotations.CanIgnoreReturnValue;
//import com.google.errorprone.annotations.Var;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
//import javax.annotation.CheckReturnValue;
//import javax.annotation.Nullable;
//import javax.annotation.ParametersAreNonnullByDefault;
//import javax.annotation.concurrent.Immutable;
//import javax.annotation.concurrent.NotThreadSafe;
import nl.knaw.huc.timbuctoo.bdb.datastores.CursorValue;
import nl.knaw.huc.timbuctoo.util.Direction;
//import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link CursorQuad}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableCursorQuad.builder()}.
 */
//@Generated(from = "CursorQuad", generator = "Immutables")
@SuppressWarnings({"all"})
//@ParametersAreNonnullByDefault
//@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
//@Immutable
//@CheckReturnValue
public final class ImmutableCursorQuad
        implements CursorQuad {
  private final String cursor;
  private final String subject;
  private final String predicate;
  private final String object;
  private final String valuetype;
  private final String language;
  private final String graph;
  private final Direction direction;
  private final ChangeType changeType;

  private ImmutableCursorQuad(
          String cursor,
          String subject,
          String predicate,
          String object,
          String valuetype,
          String language,
          String graph,
          Direction direction,
          ChangeType changeType) {
    this.cursor = cursor;
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
    this.valuetype = valuetype;
    this.language = language;
    this.graph = graph;
    this.direction = direction;
    this.changeType = changeType;
  }

  /**
   * @return The value of the {@code cursor} attribute
   */
  @Override
  public String getCursor() {
    return cursor;
  }

  /**
   * @return The value of the {@code subject} attribute
   */
  @Override
  public String getSubject() {
    return subject;
  }

  /**
   * @return The value of the {@code predicate} attribute
   */
  @Override
  public String getPredicate() {
    return predicate;
  }

  /**
   * @return The value of the {@code object} attribute
   */
  @Override
  public String getObject() {
    return object;
  }

  /**
   * @return The value of the {@code valuetype} attribute
   */
  @Override
  public Optional<String> getValuetype() {
    return Optional.ofNullable(valuetype);
  }

  /**
   * @return The value of the {@code language} attribute
   */
  @Override
  public Optional<String> getLanguage() {
    return Optional.ofNullable(language);
  }

  /**
   * @return The value of the {@code graph} attribute
   */
  @Override
  public Optional<String> getGraph() {
    return Optional.ofNullable(graph);
  }

  /**
   * @return The value of the {@code direction} attribute
   */
  @Override
  public Direction getDirection() {
    return direction;
  }

  /**
   * @return The value of the {@code changeType} attribute
   */
  @Override
  public ChangeType getChangeType() {
    return changeType;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link CursorQuad#getCursor() cursor} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for cursor
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableCursorQuad withCursor(String value) {
    String newValue = Objects.requireNonNull(value, "cursor");
    if (this.cursor.equals(newValue)) return this;
    return new ImmutableCursorQuad(
            newValue,
            this.subject,
            this.predicate,
            this.object,
            this.valuetype,
            this.language,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link CursorQuad#getSubject() subject} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for subject
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableCursorQuad withSubject(String value) {
    String newValue = Objects.requireNonNull(value, "subject");
    if (this.subject.equals(newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            newValue,
            this.predicate,
            this.object,
            this.valuetype,
            this.language,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link CursorQuad#getPredicate() predicate} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for predicate
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableCursorQuad withPredicate(String value) {
    String newValue = Objects.requireNonNull(value, "predicate");
    if (this.predicate.equals(newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            newValue,
            this.object,
            this.valuetype,
            this.language,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link CursorQuad#getObject() object} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for object
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableCursorQuad withObject(String value) {
    String newValue = Objects.requireNonNull(value, "object");
    if (this.object.equals(newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            newValue,
            this.valuetype,
            this.language,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link CursorQuad#getValuetype() valuetype} attribute.
   * @param value The value for valuetype
   * @return A modified copy of {@code this} object
   */
  public final ImmutableCursorQuad withValuetype(String value) {
    String newValue = Objects.requireNonNull(value, "valuetype");
    if (Objects.equals(this.valuetype, newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            newValue,
            this.language,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link CursorQuad#getValuetype() valuetype} attribute.
   * An equality check is used on inner nullable value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for valuetype
   * @return A modified copy of {@code this} object
   */
  public final ImmutableCursorQuad withValuetype(Optional<String> optional) {
    String value = optional.orElse(null);
    if (Objects.equals(this.valuetype, value)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            value,
            this.language,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link CursorQuad#getLanguage() language} attribute.
   * @param value The value for language
   * @return A modified copy of {@code this} object
   */
  public final ImmutableCursorQuad withLanguage(String value) {
    String newValue = Objects.requireNonNull(value, "language");
    if (Objects.equals(this.language, newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            this.valuetype,
            newValue,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link CursorQuad#getLanguage() language} attribute.
   * An equality check is used on inner nullable value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for language
   * @return A modified copy of {@code this} object
   */
  public final ImmutableCursorQuad withLanguage(Optional<String> optional) {
    String value = optional.orElse(null);
    if (Objects.equals(this.language, value)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            this.valuetype,
            value,
            this.graph,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link CursorQuad#getGraph() graph} attribute.
   * @param value The value for graph
   * @return A modified copy of {@code this} object
   */
  public final ImmutableCursorQuad withGraph(String value) {
    String newValue = Objects.requireNonNull(value, "graph");
    if (Objects.equals(this.graph, newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            this.valuetype,
            this.language,
            newValue,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link CursorQuad#getGraph() graph} attribute.
   * An equality check is used on inner nullable value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for graph
   * @return A modified copy of {@code this} object
   */
  public final ImmutableCursorQuad withGraph(Optional<String> optional) {
    String value = optional.orElse(null);
    if (Objects.equals(this.graph, value)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            this.valuetype,
            this.language,
            value,
            this.direction,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link CursorQuad#getDirection() direction} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for direction
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableCursorQuad withDirection(Direction value) {
    if (this.direction == value) return this;
    Direction newValue = Objects.requireNonNull(value, "direction");
    if (this.direction.equals(newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            this.valuetype,
            this.language,
            this.graph,
            newValue,
            this.changeType);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link CursorQuad#getChangeType() changeType} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for changeType
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableCursorQuad withChangeType(ChangeType value) {
    if (this.changeType == value) return this;
    ChangeType newValue = Objects.requireNonNull(value, "changeType");
    if (this.changeType.equals(newValue)) return this;
    return new ImmutableCursorQuad(
            this.cursor,
            this.subject,
            this.predicate,
            this.object,
            this.valuetype,
            this.language,
            this.graph,
            this.direction,
            newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableCursorQuad} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableCursorQuad
            && equalTo((ImmutableCursorQuad) another);
  }

  private boolean equalTo(ImmutableCursorQuad another) {
    return subject.equals(another.subject)
            && predicate.equals(another.predicate)
            && object.equals(another.object)
            && Objects.equals(valuetype, another.valuetype)
            && Objects.equals(language, another.language)
            && Objects.equals(graph, another.graph)
            && direction.equals(another.direction);
  }

  /**
   * Computes a hash code from attributes: {@code subject}, {@code predicate}, {@code object}, {@code valuetype}, {@code language}, {@code graph}, {@code direction}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + subject.hashCode();
    h += (h << 5) + predicate.hashCode();
    h += (h << 5) + object.hashCode();
    h += (h << 5) + Objects.hashCode(valuetype);
    h += (h << 5) + Objects.hashCode(language);
    h += (h << 5) + Objects.hashCode(graph);
    h += (h << 5) + direction.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code CursorQuad} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("CursorQuad")
            .omitNullValues()
            .add("subject", subject)
            .add("predicate", predicate)
            .add("object", object)
            .add("valuetype", valuetype)
            .add("language", language)
            .add("graph", graph)
            .add("direction", direction)
            .toString();
  }

  /**
   * Creates an immutable copy of a {@link CursorQuad} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable CursorQuad instance
   */
  public static ImmutableCursorQuad copyOf(CursorQuad instance) {
    if (instance instanceof ImmutableCursorQuad) {
      return (ImmutableCursorQuad) instance;
    }
    return ImmutableCursorQuad.builder()
            .from(instance)
            .build();
  }

  /**
   * Creates a builder for {@link ImmutableCursorQuad ImmutableCursorQuad}.
   * <pre>
   * ImmutableCursorQuad.builder()
   *    .cursor(String) // required {@link CursorQuad#getCursor() cursor}
   *    .subject(String) // required {@link CursorQuad#getSubject() subject}
   *    .predicate(String) // required {@link CursorQuad#getPredicate() predicate}
   *    .object(String) // required {@link CursorQuad#getObject() object}
   *    .valuetype(String) // optional {@link CursorQuad#getValuetype() valuetype}
   *    .language(String) // optional {@link CursorQuad#getLanguage() language}
   *    .graph(String) // optional {@link CursorQuad#getGraph() graph}
   *    .direction(nl.knaw.huygens.timbuctoo.v5.datastores.quadstore.dto.Direction) // required {@link CursorQuad#getDirection() direction}
   *    .changeType(nl.knaw.huygens.timbuctoo.v5.datastores.quadstore.dto.ChangeType) // required {@link CursorQuad#getChangeType() changeType}
   *    .build();
   * </pre>
   * @return A new ImmutableCursorQuad builder
   */
  public static ImmutableCursorQuad.Builder builder() {
    return new ImmutableCursorQuad.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableCursorQuad ImmutableCursorQuad}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  //@Generated(from = "CursorQuad", generator = "Immutables")
  //@NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_CURSOR = 0x1L;
    private static final long INIT_BIT_SUBJECT = 0x2L;
    private static final long INIT_BIT_PREDICATE = 0x4L;
    private static final long INIT_BIT_OBJECT = 0x8L;
    private static final long INIT_BIT_DIRECTION = 0x10L;
    private static final long INIT_BIT_CHANGE_TYPE = 0x20L;
    private long initBits = 0x3fL;

    private String cursor;
    private String subject;
    private String predicate;
    private String object;
    private String valuetype;
    private String language;
    private String graph;
    private Direction direction;
    private ChangeType changeType;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code nl.knaw.huygens.timbuctoo.v5.datastores.quadstore.dto.CursorQuad} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder from(CursorQuad instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    /**
     * Fill a builder with attribute values from the provided {@code nl.knaw.huygens.timbuctoo.v5.datastores.CursorValue} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder from(CursorValue instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    private void from(Object object) {
      if (object instanceof CursorQuad) {
        CursorQuad instance = (CursorQuad) object;
        Optional<String> valuetypeOptional = instance.getValuetype();
        if (valuetypeOptional.isPresent()) {
          valuetype(valuetypeOptional);
        }
        predicate(instance.getPredicate());
        subject(instance.getSubject());
        changeType(instance.getChangeType());
        Optional<String> languageOptional = instance.getLanguage();
        if (languageOptional.isPresent()) {
          language(languageOptional);
        }
        Optional<String> graphOptional = instance.getGraph();
        if (graphOptional.isPresent()) {
          graph(graphOptional);
        }
        object(instance.getObject());
        direction(instance.getDirection());
      }
      if (object instanceof CursorValue) {
        CursorValue instance = (CursorValue) object;
        cursor(instance.getCursor());
      }
    }

    /**
     * Initializes the value for the {@link CursorQuad#getCursor() cursor} attribute.
     * @param cursor The value for cursor
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder cursor(String cursor) {
      this.cursor = Objects.requireNonNull(cursor, "cursor");
      initBits &= ~INIT_BIT_CURSOR;
      return this;
    }

    /**
     * Initializes the value for the {@link CursorQuad#getSubject() subject} attribute.
     * @param subject The value for subject
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder subject(String subject) {
      this.subject = Objects.requireNonNull(subject, "subject");
      initBits &= ~INIT_BIT_SUBJECT;
      return this;
    }

    /**
     * Initializes the value for the {@link CursorQuad#getPredicate() predicate} attribute.
     * @param predicate The value for predicate
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder predicate(String predicate) {
      this.predicate = Objects.requireNonNull(predicate, "predicate");
      initBits &= ~INIT_BIT_PREDICATE;
      return this;
    }

    /**
     * Initializes the value for the {@link CursorQuad#getObject() object} attribute.
     * @param object The value for object
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder object(String object) {
      this.object = Objects.requireNonNull(object, "object");
      initBits &= ~INIT_BIT_OBJECT;
      return this;
    }

    /**
     * Initializes the optional value {@link CursorQuad#getValuetype() valuetype} to valuetype.
     * @param valuetype The value for valuetype
     * @return {@code this} builder for chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder valuetype(String valuetype) {
      this.valuetype = Objects.requireNonNull(valuetype, "valuetype");
      return this;
    }

    /**
     * Initializes the optional value {@link CursorQuad#getValuetype() valuetype} to valuetype.
     * @param valuetype The value for valuetype
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder valuetype(Optional<String> valuetype) {
      this.valuetype = valuetype.orElse(null);
      return this;
    }

    /**
     * Initializes the optional value {@link CursorQuad#getLanguage() language} to language.
     * @param language The value for language
     * @return {@code this} builder for chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder language(String language) {
      this.language = Objects.requireNonNull(language, "language");
      return this;
    }

    /**
     * Initializes the optional value {@link CursorQuad#getLanguage() language} to language.
     * @param language The value for language
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder language(Optional<String> language) {
      this.language = language.orElse(null);
      return this;
    }

    /**
     * Initializes the optional value {@link CursorQuad#getGraph() graph} to graph.
     * @param graph The value for graph
     * @return {@code this} builder for chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder graph(String graph) {
      this.graph = Objects.requireNonNull(graph, "graph");
      return this;
    }

    /**
     * Initializes the optional value {@link CursorQuad#getGraph() graph} to graph.
     * @param graph The value for graph
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder graph(Optional<String> graph) {
      this.graph = graph.orElse(null);
      return this;
    }

    /**
     * Initializes the value for the {@link CursorQuad#getDirection() direction} attribute.
     * @param direction The value for direction
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder direction(Direction direction) {
      this.direction = Objects.requireNonNull(direction, "direction");
      initBits &= ~INIT_BIT_DIRECTION;
      return this;
    }

    /**
     * Initializes the value for the {@link CursorQuad#getChangeType() changeType} attribute.
     * @param changeType The value for changeType
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder changeType(ChangeType changeType) {
      this.changeType = Objects.requireNonNull(changeType, "changeType");
      initBits &= ~INIT_BIT_CHANGE_TYPE;
      return this;
    }

    /**
     * Builds a new {@link ImmutableCursorQuad ImmutableCursorQuad}.
     * @return An immutable instance of CursorQuad
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableCursorQuad build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableCursorQuad(cursor, subject, predicate, object, valuetype, language, graph, direction, changeType);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_CURSOR) != 0) attributes.add("cursor");
      if ((initBits & INIT_BIT_SUBJECT) != 0) attributes.add("subject");
      if ((initBits & INIT_BIT_PREDICATE) != 0) attributes.add("predicate");
      if ((initBits & INIT_BIT_OBJECT) != 0) attributes.add("object");
      if ((initBits & INIT_BIT_DIRECTION) != 0) attributes.add("direction");
      if ((initBits & INIT_BIT_CHANGE_TYPE) != 0) attributes.add("changeType");
      return "Cannot build CursorQuad, some of required attributes are not set " + attributes;
    }
  }
}
