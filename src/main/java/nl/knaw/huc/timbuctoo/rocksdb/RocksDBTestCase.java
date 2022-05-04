package nl.knaw.huc.timbuctoo.rocksdb;

import nl.knaw.huc.timbuctoo.TestCase;
import nl.knaw.huc.timbuctoo.tests.Test;
import nl.knaw.huc.timbuctoo.util.Direction;

import java.io.File;
import java.net.URL;

public class RocksDBTestCase extends TestCase {
    private final Test test;
    private final RocksDBRdfHandler rdfHandler;

    public RocksDBTestCase(Test test) throws Exception {
        this.test = test;
        rdfHandler = new RocksDBRdfHandler(test.getBaseUri(), test.getName(), new File("./data/rocksdb").getCanonicalPath(), 0);

        runTests();
    }

    @Override
    public String getName() {
        return "RocksDB";
    }

    private void runTests() throws Exception {
        super.runTests(this::importAll, this::getLatest, this::getAllFromType, this::getAllAboutSubject,
                this::getSubjectsOfVersion, this::getAddedChangesOfVersion, this::getDeletedChangesOfVersion);
    }

    private long importAll() throws Exception {
        importRdf(test.getInputStream(), test.getBaseUri(), rdfHandler);
        return 0;
    }

    private long getLatest() {
        return rdfHandler.dataSource.quadStore.getAllQuads().count();
    }

    private long getAllFromType() {
        return rdfHandler.dataSource.quadStore.getQuads(test.getTestRdfType(),
                "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", Direction.IN).count();
    }

    private long getAllAboutSubject() {
        return rdfHandler.dataSource.quadStore.getQuads(test.getTestSubjectUri()).count();
    }

    private long getSubjectsOfVersion() {
        return rdfHandler.dataSource.updatedPerPatchStore.ofVersion(0).count();
    }

    private long getAddedChangesOfVersion() {
        return rdfHandler.dataSource.truePatchStore.getChangesOfVersion(0, true).count();
    }

    private long getDeletedChangesOfVersion() {
        return rdfHandler.dataSource.truePatchStore.getChangesOfVersion(0, false).count();
    }
}
