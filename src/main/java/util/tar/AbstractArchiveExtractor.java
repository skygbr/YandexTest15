package util.tar;

import java.io.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Abstract ArchiveExtractor to implement org.apache.commons.compress archivers.
 */
public abstract class AbstractArchiveExtractor<I extends ArchiveInputStream> implements IArchiveExtractor {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * Archive file.
     */
    private final Resource archiveFile;

    /**
     * Creates a new extractor for the given file.
     *
     * @param archiveFile the archive file that will be extracted.
     * @throws FileNotFoundException if the file does not exist.
     */
    public AbstractArchiveExtractor(Resource archiveFile) throws FileNotFoundException {
        Assert.notNull(archiveFile);
        if (!archiveFile.exists()) {
            throw new FileNotFoundException("File not found " + archiveFile.getFilename());
        }

        this.archiveFile = archiveFile;

    }

    protected abstract I createArchiveInputStream(InputStream fileInputStream);

    /**
     * {@inheritDoc}
     */
    @Override
    public void deflate(File destination) throws IOException {
        deflate(destination, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deflate(File destination, String outputFilePattern) throws IOException {
        deflate(destination, outputFilePattern, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deflate(File destination, String outputFilePattern, boolean flat) throws IOException {
        Assert.notNull(destination);
        if (!destination.exists() || !destination.isDirectory()) {
            throw new IllegalArgumentException("Invalid destination: " + destination.getCanonicalPath());
        }

        AntPathMatcher matcher = new AntPathMatcher();
        I archiveInputStream = null;

        try {
            archiveInputStream = createArchiveInputStream(archiveFile.getInputStream());

            for (ArchiveEntry entry = archiveInputStream.getNextEntry(); entry != null; entry = archiveInputStream.getNextEntry()) {

                String entryName = entry.getName();

                // Is a directory
                if (entry.isDirectory() && !flat) {
                    File newDir = new File(destination, entryName);
                    newDir.mkdirs();
                    continue;
                }

                // Output file pattern check
                if (StringUtils.isNotEmpty(outputFilePattern)
                        && !matcher.match(outputFilePattern, new File(entryName).getName())) {
                    continue;
                }

                // Remove directory strucutre if flat mode actived
                if (flat) {
                    entryName = new File(entryName).getName();
                }
                File newFile = new File(destination, entryName);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Deflating" + archiveFile.getFilename());
                    LOGGER.debug("Extracting file " + newFile.getAbsolutePath());
                }

                // Make the directory structure
                newFile.getParentFile().mkdirs();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(newFile);
                    IOUtils.copy(archiveInputStream, fileOutputStream);
                } finally {
                    IOUtils.closeQuietly(fileOutputStream);
                }
            }
        } finally {
            IOUtils.closeQuietly(archiveInputStream);
        }
    }

    /**
     * Returns the first outputstream according to the <code>outputFilePattern</code> parameter.
     * @param outputFilePattern
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public InputStream deflate(String outputFilePattern) throws IOException {
        AntPathMatcher matcher = new AntPathMatcher();
        I archiveInputStream = null;
        try {
            archiveInputStream = createArchiveInputStream(archiveFile.getInputStream());

            for (ArchiveEntry entry = archiveInputStream.getNextEntry(); entry != null; entry = archiveInputStream.getNextEntry()) {

                String entryName = entry.getName();

                // Output file pattern check
                if (StringUtils.isNotEmpty(outputFilePattern)
                        && !matcher.match(outputFilePattern, new File(entryName).getName())) {
                    continue;
                }
                return IOUtils.toBufferedInputStream(archiveInputStream);
            }
        } finally {
            IOUtils.closeQuietly(archiveInputStream);
        }
        return null;
    }

    @SuppressWarnings("resource")
    public boolean entryExist(String outputFilePattern) throws IOException {
        Assert.hasText(outputFilePattern);
        AntPathMatcher matcher = new AntPathMatcher();
        I archiveInputStream = null;

        try {
            archiveInputStream = createArchiveInputStream(archiveFile.getInputStream());

            for (ArchiveEntry entry = archiveInputStream.getNextEntry(); entry != null; entry = archiveInputStream.getNextEntry()) {

                String entryName = entry.getName();

                // Output file pattern check
                if (StringUtils.isNotEmpty(outputFilePattern)
                        && !matcher.match(outputFilePattern, new File(entryName).getName())) {
                    continue;
                }

                return true;
            }
        } finally {
            IOUtils.closeQuietly(archiveInputStream);
        }
        return false;
    }
}
