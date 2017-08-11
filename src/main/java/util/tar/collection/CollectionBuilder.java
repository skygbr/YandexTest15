package util.tar.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.*;

import static java.util.Collections.*;
import static util.tar.Assert.notNull;
import static util.tar.collection.CollectionUtil.toList;

/**
 * Convenience class for creating collections ({@link Set} and {@link List}) instances or
 * {@link EnclosedIterable enclosed iterables}.
 * <p>
 * The default methods {@link #asList()} and {@link #asSet()} and {@link #asSortedSet()} create immutable collections.
 *
 * @param <T> contained in the created collections.
 */
public final class CollectionBuilder<T> {

    private static final Ordering<?> NATURAL_ORDER = new NaturalOrdering();

    public static <T> CollectionBuilder<T> newBuilder() {
        return new CollectionBuilder<T>(Collections.<T> emptyList());
    }

    public static <T> CollectionBuilder<T> newBuilder(final T... elements) {
        return new CollectionBuilder<T>(Arrays.asList(elements));
    }

    public static <T> CollectionBuilder<T> newBuilder(final List<T> elements) {
        return new CollectionBuilder<T>(elements);
    }

    public static <T> List<T> list(final T... elements) {
        return unmodifiableList(Arrays.asList(elements));
    }

    static <T> Comparator<T> natural() {
        @SuppressWarnings("unchecked")
        final Comparator<T> result = (Comparator<T>) NATURAL_ORDER;
        return result;
    }

    private final List<T> elements = Lists.<T> newLinkedList();

    CollectionBuilder(final Collection<? extends T> initialElements) {
        elements.addAll(initialElements);
    }

    public CollectionBuilder<T> add(final T element) {
        elements.add(element);
        return this;
    }

    public <E extends T> CollectionBuilder<T> addAll(final E... elements) {
        this.elements.addAll(Arrays.asList(notNull(elements, "elements")));
        return this;
    }

    public CollectionBuilder<T> addAll(final Collection<? extends T> elements) {
        this.elements.addAll(notNull(elements, "elements"));
        return this;
    }

    public CollectionBuilder<T> addAll(final Enumeration<? extends T> elements) {
        this.elements.addAll(toList(notNull(elements, "elements")));
        return this;
    }

    public Collection<T> asCollection() {
        return asList();
    }

    public Collection<T> asMutableCollection() {
        return asMutableList();
    }

    public List<T> asArrayList() {
        return Lists.newArrayList(elements);
    }

    public List<T> asLinkedList() {
        return Lists.newLinkedList(elements);
    }

    public List<T> asList() {
        return unmodifiableList(new ArrayList<T>(elements));
    }

    public List<T> asMutableList() {
        return asArrayList();
    }

    public Set<T> asHashSet() {
        return Sets.newHashSet(elements);
    }

    public Set<T> asListOrderedSet() {
        return Sets.newLinkedHashSet(elements);
    }

    public Set<T> asImmutableListOrderedSet() {
        return unmodifiableSet(new LinkedHashSet<T>(elements));
    }

    public Set<T> asSet() {
        return unmodifiableSet(new HashSet<T>(elements));
    }

    public Set<T> asMutableSet() {
        return asHashSet();
    }

    public SortedSet<T> asTreeSet() {
        return new TreeSet<T>(elements);
    }

    /**
     * Return a {@link SortedSet} of the elements of this builder in their natural order.
     * Note, will throw an exception if the elements are not comparable.
     *
     * @return an immutable sorted set.
     * @throws ClassCastException if the elements do not implement {@link Comparable}.
     */
    public SortedSet<T> asSortedSet() {
        return unmodifiableSortedSet(new TreeSet<T>(elements));
    }

    public SortedSet<T> asSortedSet(final Comparator<? super T> comparator) {
        final SortedSet<T> result = new TreeSet<T>(comparator);
        result.addAll(elements);
        return unmodifiableSortedSet(result);
    }

    public SortedSet<T> asMutableSortedSet() {
        return asTreeSet();
    }

    public EnclosedIterable<T> asEnclosedIterable() {
        return CollectionEnclosedIterable.copy(elements);
    }

    static class NaturalOrdering extends Ordering<Comparable<Object>> implements Serializable {

        @Override
        public int compare(final Comparable<Object> left, final Comparable<Object> right) {
            notNull(right, "right"); // left null is caught later
            if (left == right) {
                return 0;
            }

            return left.compareTo(right);
        }

        // preserving singleton-ness gives equals()/hashCode() for free
        private Object readResolve() {
            return NATURAL_ORDER;
        }

        @Override
        public String toString() {
            return "Ordering.natural()";
        }

        private static final long serialVersionUID = 0;
    }
}
