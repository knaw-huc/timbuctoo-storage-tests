package nl.knaw.huc.timbuctoo;

import com.google.common.base.Stopwatch;
import nl.knaw.huc.timbuctoo.util.RDFHandler;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import java.io.InputStream;

public abstract class TestCase {
    private static final String IMPORT = "import";
    private static final String GET_LATEST = "get latest";
    private static final String GET_SUBJECTS_VERSION = "get subjects of version";
    private static final String GET_ADDED_CHANGES_VERSION = "get added changes of version";
    private static final String GET_DELETED_CHANGES_VERSION = "get deleted changes of version";

    private Stopwatch importAll;
    private Stopwatch getLatest;
    private Stopwatch getSubjectsOfVersion;
    private Stopwatch getAddedChangesOfVersion;
    private Stopwatch getDeletedChangesOfVersion;

    public abstract void printReport();

    protected void runTests(String name, Test importTest, Test getAllTest, Test getSubjectsOfVersionTest,
                            Test getAddedChangesOfVersionTest, Test getDeletedChangesOfVersionTest) throws Exception {
        this.importAll = performTest(name, IMPORT, importTest);
        this.getLatest = performTest(name, GET_LATEST, getAllTest);
        this.getSubjectsOfVersion = performTest(name, GET_SUBJECTS_VERSION, getSubjectsOfVersionTest);
        this.getAddedChangesOfVersion = performTest(name, GET_ADDED_CHANGES_VERSION, getAddedChangesOfVersionTest);
        this.getDeletedChangesOfVersion = performTest(name, GET_DELETED_CHANGES_VERSION, getDeletedChangesOfVersionTest);
    }

    protected void importRdf(InputStream inputStream, String baseUri, RDFHandler rdfHandler) throws Exception {
        RDFFormat format = Rio.getParserFormatForMIMEType("application/n-quads").get();
        RDFParser rdfParser = Rio.createParser(format);
        rdfParser.setPreserveBNodeIDs(true);
        rdfParser.setRDFHandler(rdfHandler);
        rdfParser.parse(inputStream, baseUri);
    }

    protected void printReport(String name) {
        System.out.println(name + "\n" +
                "##################################################\n" +
                String.format("%-40s%10s", IMPORT, importAll) + "\n" +
                String.format("%-40s%10s", GET_LATEST, getLatest) + "\n" +
                String.format("%-40s%10s", GET_SUBJECTS_VERSION, getSubjectsOfVersion) + "\n" +
                String.format("%-40s%10s", GET_ADDED_CHANGES_VERSION, getAddedChangesOfVersion) + "\n" +
                String.format("%-40s%10s", GET_DELETED_CHANGES_VERSION, getDeletedChangesOfVersion) + "\n" +
                "##################################################\n\n");
    }

    private Stopwatch performTest(String name, String description, Test test) throws Exception {
        System.out.println("Started \"" + description + "\" for " + name);

        Stopwatch stopwatch = Stopwatch.createStarted();
        test.run();
        stopwatch.stop();

        System.out.println("Finished \"" + description + "\" for " + name);
        System.out.println();

        return stopwatch;
    }

    public interface Test {
        void run() throws Exception;
    }
}
