package nl.knaw.huc.timbuctoo.bdb;

import nl.knaw.huc.timbuctoo.TestCase;
import nl.knaw.huc.timbuctoo.tests.Test;
import nl.knaw.huc.timbuctoo.util.Direction;

import java.io.File;
import java.net.URL;

public class BdbTestCase extends TestCase {
    private final Test test;
    private final BdbRdfHandler rdfHandler;

    public BdbTestCase(Test test) throws Exception {
        this.test = test;
        rdfHandler = new BdbRdfHandler("12345", test.getName(), test.getBaseUri(), null,
                test.getName(), new File("./data/bdb").getCanonicalPath(), 0);

        runTests();
    }

    @Override
    public String getName() {
        return "Berkeley DB";
    }

    private void runTests() throws Exception {
        super.runTests(this::importAll, this::getLatest, this::getAllFromType, this::getAllAboutSubject,
                this::getSubjectsOfVersion, this::getAddedChangesOfVersion, this::getDeletedChangesOfVersion);
    }

    private long importAll() throws Exception {
        importRdf(test.getInputStream(), test.getBaseUri(), rdfHandler);
        rdfHandler.bdbDataSource.commit();
        return 0;
    }

    private long getLatest() {
        return rdfHandler.bdbDataSource.quadStore.getAllQuads().count();
    }

    private long getAllFromType() {
        return rdfHandler.bdbDataSource.quadStore.getQuads(test.getTestRdfType(), "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", Direction.IN, "").count();
    }

    private long getAllAboutSubject() {
        return rdfHandler.bdbDataSource.quadStore.getQuads(test.getTestSubjectUri()).count();
    }

    private long getSubjectsOfVersion() {
        return rdfHandler.bdbDataSource.updatedPerPatchStore.ofVersion(0).count();
    }

    private long getAddedChangesOfVersion() {
        return rdfHandler.bdbDataSource.truePatchStore.getChangesOfVersion(0, true).count();
    }

    private long getDeletedChangesOfVersion() {
        return rdfHandler.bdbDataSource.truePatchStore.getChangesOfVersion(0, false).count();
    }
}
