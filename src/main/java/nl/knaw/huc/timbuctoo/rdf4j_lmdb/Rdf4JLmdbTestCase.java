package nl.knaw.huc.timbuctoo.rdf4j_lmdb;

import nl.knaw.huc.timbuctoo.TestCase;
import nl.knaw.huc.timbuctoo.tests.Test;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.lmdb.LmdbStore;

import java.io.File;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class Rdf4JLmdbTestCase extends TestCase {
    private final Test test;
    private final Repository repository;

    public Rdf4JLmdbTestCase(Test test) throws Exception {
        this.test = test;
        repository = new SailRepository(new LmdbStore(new File(
                "./data/rdf4j_lmdb/" + DigestUtils.md5Hex(test.getName()))));

        runTests();
    }

    @Override
    public String getName() {
        return "RDF4J LMDB v2";
    }

    protected void runTests() throws Exception {
        super.runTests(this::importAll, this::getLatest, this::getAllFromType, this::getAllAboutSubject,
                null, null, null);
    }

    private long importAll() throws Exception {
        try (final RepositoryConnection connection = repository.getConnection()) {
            connection.add(test.getInputStream(), test.getBaseUri(), RDFFormat.NQUADS);
            return 0;
        }
    }

    private long getLatest() {
        try (final RepositoryConnection connection = repository.getConnection()) {
            try (RepositoryResult<Statement> result =
                         connection.getStatements(null, null, null, false)) {
                return result.stream().count();
            }
        }
    }

    private long getAllFromType() {
        try (final RepositoryConnection connection = repository.getConnection()) {
            try (RepositoryResult<Statement> result =
                         connection.getStatements(null, RDF.TYPE, iri(test.getTestRdfType()), false)) {
                return result.stream().count();
            }
        }
    }

    private long getAllAboutSubject() {
        try (final RepositoryConnection connection = repository.getConnection()) {
            long count;

            try (RepositoryResult<Statement> result =
                         connection.getStatements(iri(test.getTestSubjectUri()), null, null, false)) {
                count = result.stream().count();
            }

            try (RepositoryResult<Statement> result =
                         connection.getStatements(null, null, iri(test.getTestSubjectUri()), false)) {
                return count + result.stream().count();
            }
        }
    }
}
