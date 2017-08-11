package util.tar;

import java.util.HashSet;
import java.util.Set;

public class ClassUtils {

    private ClassUtils() {
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you absolutely need a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
     * reference as well).
     * @return the default ClassLoader (never <code>null</code>)
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread()
                    .getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * Finds all super classes and interfaces for a given class
     * @param cls The class to scan
     * @return The collected related classes found
     */
    public static Set<Class<?>> findAllTypes(final Class<?> cls) {
        final Set<Class<?>> types = new HashSet<Class<?>>();
        findAllTypes(cls, types);
        return types;
    }

    /**
     * Finds all super classes and interfaces for a given class
     * @param cls The class to scan
     * @param types The collected related classes found
     */
    public static void findAllTypes(final Class<?> cls, final Set<Class<?>> types) {
        if (cls == null) {
            return;
        }

        // check to ensure it hasn't been scanned yet
        if (types.contains(cls)) {
            return;
        }

        types.add(cls);

        findAllTypes(cls.getSuperclass(), types);
        for (int x = 0; x < cls.getInterfaces().length; x++) {
            findAllTypes(cls.getInterfaces()[x], types);
        }
    }
}
