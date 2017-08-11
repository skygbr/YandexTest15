package util.tar;

import java.io.*;

/**
 * Helper interface to extract archive.
 */
public interface IArchiveExtractor {

    /**
     * Extract the archive file to the given destination.
     *
     * @param destination the destination the file will be extracted to.
     * @throws IOException if an io exception occures.
     */
    void deflate(File destination) throws IOException;

    /**
     * Extract the archive file to the given destination.
     *
     * @param destination the destination the file will be extracted to.
     * @param outputFilePattern only file matching this pattern will be deflated.
     * @throws IOException if an io exception occures.
     */
    void deflate(File destination, String outputFilePattern) throws IOException;

    /**
     * Extract the archive file to the given destination, without directory tree structure.
     *
     * @param destination the destination the file will be extracted to.
     * @param outputFilePattern only file matching this pattern will be deflated.
     * @param flat all files will be placed in the destination folder without the directory structure.
     * @throws IOException if an io exception occures.
     */
    void deflate(File destination, String outputFilePattern, boolean flat) throws IOException;

}
