package nl.knaw.huc.timbuctoo.bdb;

import nl.knaw.huc.timbuctoo.TestCase;

import java.io.File;
import java.net.URL;

public class BdbTestCase extends TestCase {
    private static final String NAME = "Berkeley DB";

    private final String url;
    private final String baseUri;
    private final int version;
    private final BdbRdfHandler rdfHandler;

    public BdbTestCase(String url, String userId, String name, String baseUri, String defaultGraph,
                       String fileName, int version) throws Exception {
        this.url = url;
        this.baseUri = baseUri;
        this.version = version;

        String currentPath = new File("./data/bdb").getCanonicalPath();
        rdfHandler = new BdbRdfHandler(userId, name, baseUri, defaultGraph, fileName, currentPath, version);

        runTests();
    }

    public void printReport() {
        super.printReport(NAME);
    }

    private void runTests() throws Exception {
        super.runTests(NAME, this::importAll, this::getLatest, this::getSubjectsOfVersion,
                this::getAddedChangesOfVersion, this::getDeletedChangesOfVersion);
    }

    private void importAll() throws Exception {
        importRdf(new URL(url).openStream(), baseUri, rdfHandler);
        rdfHandler.bdbDataSource.commit();
    }

    private void getLatest() {
        rdfHandler.bdbDataSource.quadStore.getAllQuads().count();
    }

    private void getSubjectsOfVersion() {
        rdfHandler.bdbDataSource.updatedPerPatchStore.ofVersion(version).count();
    }

    private void getAddedChangesOfVersion() {
        rdfHandler.bdbDataSource.truePatchStore.getChangesOfVersion(version, true).count();
    }

    private void getDeletedChangesOfVersion() {
        rdfHandler.bdbDataSource.truePatchStore.getChangesOfVersion(version, false).count();
    }
}
