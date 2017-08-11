package util.tar.collection;

import com.google.common.base.Function;

import java.util.Iterator;

import static util.tar.Assert.notNull;

/**
 * {@link Iterator} implementation that decorates another {@link Iterator} who
 * contains values of type I and uses a {@link Function} that converts that I
 * into a V.
 * <p>
 * This implementation is unmodifiable.
 *
 * @param <I> the value in the underlying iterator
 * @param <E> the value it is converted to
 */
class TransformingIterator<I, E> implements Iterator<E> {

    private final Iterator<? extends I> iterator;

    private final Function<I, E> decorator;

    TransformingIterator(final Iterator<? extends I> iterator, final Function<I, E> decorator) {
        notNull(iterator);
        notNull(decorator);
        this.iterator = iterator;
        this.decorator = decorator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return decorator.apply(iterator.next());
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
