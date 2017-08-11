package tar;

import java.io.*;
import org.apache.commons.compress.archivers.tar.*;
import util.tar.AbstractArchiveCreator;

/**
 * Helper class to create in directory tar archive.
 */
public class TarCreator extends AbstractArchiveCreator<TarArchiveOutputStream, TarArchiveEntry> {

    public TarCreator(File archiveFile) {
        super(archiveFile);
    }

    @Override
    protected TarArchiveEntry createArchiveEntry(String name, File file) {
        TarArchiveEntry entry = new TarArchiveEntry(name);
        entry.setSize(file.length());
        return entry;
    }

    @Override
    protected TarArchiveOutputStream createArchiveOutputStream(BufferedOutputStream stream) {
        return new TarArchiveOutputStream(stream);
    }
}
