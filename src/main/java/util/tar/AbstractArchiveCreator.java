package util.tar;

import java.io.*;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Abstract ArchiveCreator to implement org.apache.commons.compress archivers.
 */
public abstract class AbstractArchiveCreator<O extends ArchiveOutputStream, E extends ArchiveEntry> implements IArchiveCreator {

    private static final int BUFFER_SIZE = 2048;

    /**
     * Archive file.
     */
    private final File archiveFile;

    /**
     * Create a new archive creator with the given file as backend.
     *
     * @param archiveFile the tar file.
     */
    public AbstractArchiveCreator(File archiveFile) {
        this.archiveFile = archiveFile;
    }

    /**
     * Add the files within the given directory to the ArchiveOutputStream. This method will call itself for each
     * subdirectory.
     *
     * @param baseName represents the relative base name within the tar file.
     * @param outStream the ouput stream.
     * @param directory the directory.
     * @throws IOException if an io exception occures.
     */
    private void addFiles(String baseName, O outStream, File directory) throws IOException {
        byte[] data = new byte[BUFFER_SIZE];

        for (File file : directory.listFiles()) {
            String relativeName = getRelativePath(baseName, file);
            E entry = createArchiveEntry(relativeName, file);
            try {
                outStream.putArchiveEntry(entry);
            } catch (ZipException ignore) {
                throw ignore;
            }

            if (file.isDirectory()) {
                // Add the files within the directory
                addFiles(baseName, outStream, file);
                continue;
            }

            FileInputStream fileInputStream = null;
            BufferedInputStream origin = null;

            try {
                fileInputStream = new FileInputStream(file);
                origin = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
                int count;
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    outStream.write(data, 0, count);
                }
            } finally {
                outStream.closeArchiveEntry();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (origin != null) {
                    origin.close();
                }
            }
        }
    }

    protected abstract E createArchiveEntry(String name, File file);

    protected abstract O createArchiveOutputStream(BufferedOutputStream stream);

    /**
     * {@inheritDoc}
     */
    @Override
    public File getArchiveFile() {
        return archiveFile;
    }

    /**
     * Returns the relative path.
     *
     * @param baseName the base path.
     * @param file the file.
     * @return the path of the given file relative to the base name.
     * @throws IOException if an io exception occures.
     */
    private String getRelativePath(String baseName, File file) throws IOException {
        String name = file.getCanonicalPath().substring(baseName.length() + 1);

        if (file.isDirectory()) {
            name += '/';
        }

        return name.replaceAll("\\\\", "/");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File inflate(File... directories) throws IOException {
        O outStream = createArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(archiveFile)));

        try {
            for (File directory : directories) {
                String baseName = directory.getCanonicalPath();
                addFiles(baseName, outStream, directory);
            }
        } finally {
            outStream.close();
        }

        return archiveFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File inflate(File file) throws IOException {
        if (file.isDirectory()) {
            inflate(new File[] { file });
        } else {
            O outStream = createArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(archiveFile)));
            E entry = createArchiveEntry(file.getName(), file);
            try {
                outStream.putArchiveEntry(entry);
            } catch (ZipException ignore) {
            }
            InputStream inputStream = new FileInputStream(file);
            try {
                IOUtils.copy(inputStream, outStream);
            } finally {
                try {
                    outStream.closeArchiveEntry();
                    outStream.close();
                } finally {
                    inputStream.close();
                }
            }
        }

        return archiveFile;
    }
}
