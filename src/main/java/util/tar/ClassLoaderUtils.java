package util.tar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is extremely useful for loading resources and classes in a fault tolerant manner
 * that works across different applications servers.

 *
 * @author $Author: jnolen $
 * @version $Revision: 1.6 $
 */
public class ClassLoaderUtils {

    /**
     * Load a class with a given name.
     * <p>
     * It will try to load the class in the following order:
     * <ul>
     *  <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     *  <li>Using the basic {@link Class#forName(String) }
     *  <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     *  <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param className The name of the class to load
     * @param callingClass The Class object of the calling object
     * @throws ClassNotFoundException If the class cannot be found anywhere.
     */
    public static Class<?> loadClass(String className, Class<?> callingClass) throws ClassNotFoundException {
        return loadClass(className, callingClass.getClassLoader());
    }

    /**
     * Load a class with a given name.
     * <p>
     * It will try to load the class in the following order:
     * <ul>
     *  <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     *  <li>Using the basic {@link Class#forName(String) }
     *  <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     *  <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param className The name of the class to load
     * @param callingClassLoader The ClassLoader the calling object which will be used to look up className
     * @throws ClassNotFoundException If the class cannot be found anywhere.
     */
    public static Class<?> loadClass(String className, ClassLoader callingClassLoader)
            throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoaderUtils.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    return callingClassLoader.loadClass(className);
                }

            }
        }
    }

    /**
     * Load a given resource.
     * <p>
     * This method will try to load the resource using the following methods (in order):
     * <ul>
     *  <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     *  <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     *  <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param resourceName The name of the resource to load
     * @param callingClass The Class object of the calling object
     */
    public static URL getResource(String resourceName, Class<?> callingClass) {
        URL url = null;

        url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = ClassLoaderUtils.class.getClassLoader().getResource(resourceName);
        }

        if (url == null) {
            url = callingClass.getClassLoader().getResource(resourceName);
        }
        return url;
    }

    /**
     * getBundle() version of getResource() (that checks against the same list of class loaders)
     * @param resourceName
     * @param locale
     * @param callingClass
     */
    public static ResourceBundle getBundle(String resourceName, Locale locale, Class<?> callingClass) {
        ResourceBundle bundle = null;

        bundle = ResourceBundle.getBundle(resourceName, locale, Thread.currentThread()
                .getContextClassLoader());

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(resourceName, locale, ClassLoaderUtils.class.getClassLoader());
        }

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(resourceName, locale, callingClass.getClassLoader());
        }
        return bundle;
    }

    /**
    * returns all found resources as java.net.URLs.
    * <p>
    * This method will try to load the resource using the following methods (in order):
    * <ul>
    *  <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
    *  <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
    *  <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
    * </ul>
    *
    * @param resourceName The name of the resource to load
    * @param callingClass The Class object of the calling object
    */
    public static Enumeration<URL> getResources(String resourceName, Class<?> callingClass)
            throws IOException {
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(resourceName);
        if (urls == null) {
            urls = ClassLoaderUtils.class.getClassLoader().getResources(resourceName);
            if (urls == null) {
                urls = callingClass.getClassLoader().getResources(resourceName);
            }
        }

        return urls;
    }

    /**
     * This is a convenience method to load a resource as a stream.
     *
     * The algorithm used to find the resource is given in getResource()
     *
     * @param resourceName The name of the resource to load
     * @param callingClass The Class object of the calling object
     */
    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);
        try {
            return url != null ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Prints the current classloader hierarchy - useful for debugging.
     */
    public static void printClassLoader() {
        System.out.println("ClassLoaderUtils.printClassLoader");
        printClassLoader(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Prints the classloader hierarchy from a given classloader - useful for debugging.
     */
    public static void printClassLoader(ClassLoader cl) {
        System.out.println("ClassLoaderUtils.printClassLoader(cl = " + cl + ")");
        if (cl != null) {
            printClassLoader(cl.getParent());
        }
    }
}