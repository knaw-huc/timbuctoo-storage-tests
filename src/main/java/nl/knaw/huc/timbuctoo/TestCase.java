package nl.knaw.huc.timbuctoo;

import com.google.common.base.Stopwatch;
import nl.knaw.huc.timbuctoo.util.RDFHandler;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import java.io.InputStream;

public abstract class TestCase {
    private static final String IMPORT = "import";
    private static final String GET_ALL = "get all";
    private static final String GET_SUBJECTS_VERSION = "get subjects of version";
    private static final String GET_ADDED_CHANGES_VERSION = "get added changes of version";
    private static final String GET_DELETED_CHANGES_VERSION = "get deleted changes of version";

    private Stopwatch importAll;
    private Stopwatch getAll;
    private Stopwatch getSubjectsOfVersion;
    private Stopwatch getAddedChangesOfVersion;
    private Stopwatch getDeletedChangesOfVersion;

    public abstract void printReport();

    protected void runTests(String name, Test importTest, Test getAllTest, Test getSubjectsOfVersionTest,
                            Test getAddedChangesOfVersionTest, Test getDeletedChangesOfVersionTest) throws Exception {
        this.importAll = performTest(name, "import", importTest);
        this.getAll = performTest(name, "get all", getAllTest);
        this.getSubjectsOfVersion = performTest(name, "get subjects of version", getSubjectsOfVersionTest);
        this.getAddedChangesOfVersion = performTest(name, "get added changes of version", getAddedChangesOfVersionTest);
        this.getDeletedChangesOfVersion = performTest(name, "get deleted changes of version", getDeletedChangesOfVersionTest);
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
                String.format("%-40s%10s", GET_ALL, getAll) + "\n" +
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
