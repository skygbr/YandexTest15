package util.tar;

import java.io.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Junit Test - BlockJUnit4ClassRunner.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public abstract class AbstractJunitTest {

    protected static final File WORK_HOME = new File(getBasedir(), "target/workOutputToTar");
    protected static final File WORK_HOME_FOLDER = new File(getBasedir(), "target/workOutputToTar/Catalog");
    private static String basedir;

    static {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getBasedir() {
        if (basedir != null) {
            return basedir;
        }

        basedir = System.getProperty("basedir");

        if (basedir == null) {
            basedir = new File("").getAbsolutePath();
        }

        return basedir;
    }

    @BeforeClass
    public static void init() throws Exception {
        basedir = getBasedir();
    }

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before
    public void setUp() throws Exception {
        //Todo for delete Directory "target/workOutputToTar" unbend
        //FileUtils.deleteDirectory(WORK_HOME);
        WORK_HOME.mkdirs();
    }
}
