package util.tar.collection;

/**
 * Consume the object a {@link Supplier} produces.
 */
public interface Consumer<T> {

    /**
     * Consume the product.
     * 
     * @param element must not be null
     */
    void consume(T element);
}