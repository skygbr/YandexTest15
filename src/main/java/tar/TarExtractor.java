package tar;

import java.io.*;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import util.tar.AbstractArchiveExtractor;
import org.springframework.core.io.Resource;

/**
 * Helper class to extract in directory tar archive.
 */
public class TarExtractor extends AbstractArchiveExtractor<TarArchiveInputStream> {

    public TarExtractor(Resource archiveFile) throws FileNotFoundException {
        super(archiveFile);
    }

    @Override
    protected TarArchiveInputStream createArchiveInputStream(InputStream fileInputStream) {
        return new TarArchiveInputStream(fileInputStream);
    }
}
