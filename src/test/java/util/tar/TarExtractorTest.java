package util.tar;

import static org.junit.Assert.assertTrue;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * Test for testTarFile.tar exctract (untar files and directory in folder "target/workOutputToTar")
 */
public class TarExtractorTest extends AbstractJunitTest {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Test
    public void testDeflateFolder() throws Exception {
        File destination = WORK_HOME;
        FileUtils.deleteDirectory(WORK_HOME_FOLDER);
        tar.TarExtractor extractor = new tar.TarExtractor(
                resourceLoader.getResource("file:target/workOutputToTar/testTarFile.tar"));
        extractor.deflate(destination);

        /**
         * Asserts that a condition is true. If it isn't it throws an
         * {@link AssertionError} without a message.
         *
         * @param condition condition to be checked
         */
        assertTrue(new File(destination, "testFile.txt").exists());
        assertTrue(new File(destination, "Photo.jpg").exists());
        assertTrue(new File(destination, "In_sea.webm").exists());
        assertTrue(new File(destination, "In_ad.webm").exists());
        assertTrue(new File(destination, "AV_Defender.mp4").exists());
        assertTrue(new File(destination, "Catalog/testFileCatalog.txt").exists());

        /**
         * Extract testTarFile.tar in directory "target/workOutputToTar"
         */
        new File(destination, "testFile.txt").createNewFile();
        new File(destination, "Photo.jpg").createNewFile();
        new File(destination, "In_sea.webm").createNewFile();
        new File(destination, "In_ad.webm").createNewFile();
        new File(destination, "AV_Defender.mp4").createNewFile();
        new File(destination, "Catalog").createNewFile();
        new File(destination, "Catalog/testFileCatalog.txt").createNewFile();
        System.out.println("Extract files done");
    }
}