package util.tar;

import static org.junit.Assert.assertTrue;

import java.io.File;

import tar.TarCreator;
import org.junit.Test;

/**
 * Test for tar create in destination - "target/workOutputToTar/testTarFile.tar"
 */
public class TarCreatorTest extends AbstractJunitTest {

    @Test
    public void testInflateFileOrFolder() throws Exception {
        File resource = ResourceUtils.getFile("classpath:tar/testData/Input/");
        File destination = new File(WORK_HOME, "testTarFile.tar");
        IArchiveCreator creator = new TarCreator(destination);
        creator.inflate(resource);
        assertTrue(destination.exists());
        assertTrue(destination.length() > 0);
        System.out.println("Create testTarFile.tar done");
    }
}
