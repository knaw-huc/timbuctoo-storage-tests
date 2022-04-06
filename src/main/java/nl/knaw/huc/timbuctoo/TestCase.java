package nl.knaw.huc.timbuctoo;

import com.google.common.base.Stopwatch;
import nl.knaw.huc.timbuctoo.util.RDFHandler;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import java.io.InputStream;

public abstract class TestCase {
    public static final String IMPORT = "import";
    public static final String GET_ALL_LATEST = "get all of latest version";
    public static final String GET_ALL_FROM_TYPE_LATEST = "get all from RDF type of latest version";
    public static final String GET_ALL_ABOUT_SUBJECT_LATEST = "get all about subject URI of latest version";
    public static final String GET_SUBJECTS_VERSION = "get subjects of version";
    public static final String GET_ADDED_CHANGES_VERSION = "get added changes of version";
    public static final String GET_DELETED_CHANGES_VERSION = "get deleted changes of version";

    public TestResult importAll;
    public TestResult getAllLatest;
    public TestResult getAllFromTypeLatest;
    public TestResult getAllAboutSubjectLatest;
    public TestResult getSubjectsOfVersion;
    public TestResult getAddedChangesOfVersion;
    public TestResult getDeletedChangesOfVersion;

    public abstract String getName();

    protected void runTests(TestRun importTest, TestRun getAllLatestTest, TestRun getAllFromTypeLatest,
                            TestRun getAllAboutSubjectLatest, TestRun getSubjectsOfVersionTest,
                            TestRun getAddedChangesOfVersionTest, TestRun getDeletedChangesOfVersionTest) throws Exception {
        this.importAll = performTest(IMPORT, importTest);
        this.getAllLatest = performTest(GET_ALL_LATEST, getAllLatestTest);
        this.getAllFromTypeLatest = performTest(GET_ALL_FROM_TYPE_LATEST, getAllFromTypeLatest);
        this.getAllAboutSubjectLatest = performTest(GET_ALL_ABOUT_SUBJECT_LATEST, getAllAboutSubjectLatest);
        this.getSubjectsOfVersion = performTest(GET_SUBJECTS_VERSION, getSubjectsOfVersionTest);
        this.getAddedChangesOfVersion = performTest(GET_ADDED_CHANGES_VERSION, getAddedChangesOfVersionTest);
        this.getDeletedChangesOfVersion = performTest(GET_DELETED_CHANGES_VERSION, getDeletedChangesOfVersionTest);
    }

    protected void importRdf(InputStream inputStream, String baseUri, RDFHandler rdfHandler) throws Exception {
        RDFFormat format = Rio.getParserFormatForMIMEType("application/n-quads").get();
        RDFParser rdfParser = Rio.createParser(format);
        rdfParser.setPreserveBNodeIDs(true);
        rdfParser.setRDFHandler(rdfHandler);
        rdfParser.parse(inputStream, baseUri);
        rdfHandler.commit();
    }

    private TestResult performTest(String description, TestRun test) throws Exception {
        if (test != null) {
            System.out.println("Started \"" + description + "\" for " + getName());

            Stopwatch stopwatch = Stopwatch.createStarted();
            long count = test.run();
            stopwatch.stop();

            System.out.println("Finished \"" + description + "\" for " + getName());
            System.out.println();

            return new TestResult(stopwatch, count);
        }

        return null;
    }

    public interface TestRun {
        long run() throws Exception;
    }
}
