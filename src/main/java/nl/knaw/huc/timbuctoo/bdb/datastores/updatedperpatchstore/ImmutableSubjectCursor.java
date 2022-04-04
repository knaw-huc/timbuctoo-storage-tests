package nl.knaw.huc.timbuctoo.bdb.datastores.updatedperpatchstore;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;
//import com.google.errorprone.annotations.CanIgnoreReturnValue;
//import com.google.errorprone.annotations.Var;
import nl.knaw.huc.timbuctoo.bdb.datastores.CursorValue;
//import org.immutables.value.Generated;

//import javax.annotation.CheckReturnValue;
//import javax.annotation.Nullable;
//import javax.annotation.ParametersAreNonnullByDefault;
//import javax.annotation.concurrent.Immutable;
//import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Immutable implementation of {@link SubjectCursor}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableSubjectCursor.builder()}.
 */
//@Generated(from = "SubjectCursor", generator = "Immutables")
@SuppressWarnings({"all"})
//@ParametersAreNonnullByDefault
//@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
//@Immutable
//@CheckReturnValue
public final class ImmutableSubjectCursor
    implements SubjectCursor {
  private final String cursor;
  private final String subject;
  private final ImmutableSet<Integer> versions;

  private ImmutableSubjectCursor(
      String cursor,
      String subject,
      ImmutableSet<Integer> versions) {
    this.cursor = cursor;
    this.subject = subject;
    this.versions = versions;
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
   * @return The value of the {@code versions} attribute
   */
  @Override
  public ImmutableSet<Integer> getVersions() {
    return versions;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link SubjectCursor#getCursor() cursor} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for cursor
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableSubjectCursor withCursor(String value) {
    String newValue = Objects.requireNonNull(value, "cursor");
    if (this.cursor.equals(newValue)) return this;
    return new ImmutableSubjectCursor(newValue, this.subject, this.versions);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link SubjectCursor#getSubject() subject} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for subject
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableSubjectCursor withSubject(String value) {
    String newValue = Objects.requireNonNull(value, "subject");
    if (this.subject.equals(newValue)) return this;
    return new ImmutableSubjectCursor(this.cursor, newValue, this.versions);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link SubjectCursor#getVersions() versions}.
   * @param elements The elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableSubjectCursor withVersions(int... elements) {
    ImmutableSet<Integer> newValue = ImmutableSet.copyOf(Ints.asList(elements));
    return new ImmutableSubjectCursor(this.cursor, this.subject, newValue);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link SubjectCursor#getVersions() versions}.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param elements An iterable of versions elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableSubjectCursor withVersions(Iterable<Integer> elements) {
    if (this.versions == elements) return this;
    ImmutableSet<Integer> newValue = ImmutableSet.copyOf(elements);
    return new ImmutableSubjectCursor(this.cursor, this.subject, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableSubjectCursor} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableSubjectCursor
        && equalTo((ImmutableSubjectCursor) another);
  }

  private boolean equalTo(ImmutableSubjectCursor another) {
    return subject.equals(another.subject)
        && versions.equals(another.versions);
  }

  /**
   * Computes a hash code from attributes: {@code subject}, {@code versions}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + subject.hashCode();
    h += (h << 5) + versions.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code SubjectCursor} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("SubjectCursor")
        .omitNullValues()
        .add("subject", subject)
        .add("versions", versions)
        .toString();
  }

  /**
   * Creates an immutable copy of a {@link SubjectCursor} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable SubjectCursor instance
   */
  public static ImmutableSubjectCursor copyOf(SubjectCursor instance) {
    if (instance instanceof ImmutableSubjectCursor) {
      return (ImmutableSubjectCursor) instance;
    }
    return ImmutableSubjectCursor.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableSubjectCursor ImmutableSubjectCursor}.
   * <pre>
   * ImmutableSubjectCursor.builder()
   *    .cursor(String) // required {@link SubjectCursor#getCursor() cursor}
   *    .subject(String) // required {@link SubjectCursor#getSubject() subject}
   *    .addVersions|addAllVersions(int) // {@link SubjectCursor#getVersions() versions} elements
   *    .build();
   * </pre>
   * @return A new ImmutableSubjectCursor builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builds instances of type {@link ImmutableSubjectCursor ImmutableSubjectCursor}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  //@Generated(from = "SubjectCursor", generator = "Immutables")
  //@NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_CURSOR = 0x1L;
    private static final long INIT_BIT_SUBJECT = 0x2L;
    private long initBits = 0x3L;

    private String cursor;
    private String subject;
    private ImmutableSet.Builder<Integer> versions = ImmutableSet.builder();

    private Builder() {
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

    /**
     * Fill a builder with attribute values from the provided {@code nl.knaw.huygens.timbuctoo.v5.datastores.updatedperpatchstore.SubjectCursor} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder from(SubjectCursor instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    private void from(Object object) {
      if (object instanceof CursorValue) {
        CursorValue instance = (CursorValue) object;
        cursor(instance.getCursor());
      }
      if (object instanceof SubjectCursor) {
        SubjectCursor instance = (SubjectCursor) object;
        addAllVersions(instance.getVersions());
        subject(instance.getSubject());
      }
    }

    /**
     * Initializes the value for the {@link SubjectCursor#getCursor() cursor} attribute.
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
     * Initializes the value for the {@link SubjectCursor#getSubject() subject} attribute.
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
     * Adds one element to {@link SubjectCursor#getVersions() versions} set.
     * @param element A versions element
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder addVersions(int element) {
      this.versions.add(element);
      return this;
    }

    /**
     * Adds elements to {@link SubjectCursor#getVersions() versions} set.
     * @param elements An array of versions elements
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder addVersions(int... elements) {
      this.versions.addAll(Ints.asList(elements));
      return this;
    }


    /**
     * Sets or replaces all elements for {@link SubjectCursor#getVersions() versions} set.
     * @param elements An iterable of versions elements
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder versions(Iterable<Integer> elements) {
      this.versions = ImmutableSet.builder();
      return addAllVersions(elements);
    }

    /**
     * Adds elements to {@link SubjectCursor#getVersions() versions} set.
     * @param elements An iterable of versions elements
     * @return {@code this} builder for use in a chained invocation
     */
    //@CanIgnoreReturnValue
    public final Builder addAllVersions(Iterable<Integer> elements) {
      this.versions.addAll(elements);
      return this;
    }

    /**
     * Builds a new {@link ImmutableSubjectCursor ImmutableSubjectCursor}.
     * @return An immutable instance of SubjectCursor
     * @throws IllegalStateException if any required attributes are missing
     */
    public ImmutableSubjectCursor build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableSubjectCursor(cursor, subject, versions.build());
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_CURSOR) != 0) attributes.add("cursor");
      if ((initBits & INIT_BIT_SUBJECT) != 0) attributes.add("subject");
      return "Cannot build SubjectCursor, some of required attributes are not set " + attributes;
    }
  }
}
