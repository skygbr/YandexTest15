package util.tar;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

import static util.tar.AbstractJunitTest.WORK_HOME;
import static util.tar.AbstractJunitTest.WORK_HOME_FOLDER;

/**
 * Test for testTarFile.tar file untar in - "target/workOutputToTar")
 */
public class TarUntarTest {

    public static void main(String[] args) throws IOException, ArchiveException {
        String inputTarFile = WORK_HOME + "/" + "testTarFile.tar";
        String unTarDirectory = WORK_HOME + "";
        FileUtils.deleteDirectory(WORK_HOME_FOLDER);
        final InputStream is = new FileInputStream(inputTarFile);
        final TarArchiveInputStream in = (TarArchiveInputStream) new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, is);
        TarArchiveEntry entry = null;
        while ((entry = (TarArchiveEntry) in.getNextEntry()) != null) {
            byte[] content = new byte[(int) entry.getSize()];
            in.read(content);
            final File entryFile = new File(unTarDirectory, entry.getName());
            if (entry.isDirectory() && !entryFile.exists()) {
                if (!entryFile.mkdirs()) {
                    throw new IOException("Create directory failed: "
                            + entryFile.getAbsolutePath());
                }
            } else {
                final OutputStream out = new FileOutputStream(entryFile);
                IOUtils.write(content, out);
                out.close();
            }
        }
        in.close();
        System.out.println("Untar files done");
    }
}

