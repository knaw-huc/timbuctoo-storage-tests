package nl.knaw.huc.timbuctoo.rdf4j_lmdb;

import nl.knaw.huc.timbuctoo.TestCase;
import nl.knaw.huc.timbuctoo.tests.Test;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.lmdb.LmdbStore;

import java.io.File;
import java.net.URL;

public class Rdf4JLmdbTestCase extends TestCase {
    private static final String GET_LATEST = "SELECT ?s ?p ?o WHERE { ?s ?p ?o }";
    private static final String GET_FROM_TYPE = "SELECT ?s WHERE { ?s a <%s> }";
    private static final String GET_ABOUT_SUBJECT = "SELECT ?s ?p WHERE { { ?s ?p <%s> } UNION { <%s> ?p ?s } }";

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
        return "RDF4J LMDB";
    }

    protected void runTests() throws Exception {
        super.runTests(this::importAll, this::getLatest, this::getAllFromType, this::getAllAboutSubject,
                null, null, null);
    }

    private long importAll() throws Exception {
        try (final RepositoryConnection connection = repository.getConnection()) {
            connection.add(new URL(test.getUrl()).openStream(), test.getBaseUri(), RDFFormat.NQUADS);
            return 0;
        }
    }

    private long getLatest() {
        try (final RepositoryConnection connection = repository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(GET_LATEST);
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                return result.stream().count();
            }
        }
    }

    private long getAllFromType() {
        try (final RepositoryConnection connection = repository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(String.format(GET_FROM_TYPE, test.getTestRdfType()));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                return result.stream().count();
            }
        }
    }

    private long getAllAboutSubject() {
        try (final RepositoryConnection connection = repository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(
                    String.format(GET_ABOUT_SUBJECT, test.getTestSubjectUri(), test.getTestSubjectUri()));
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                return result.stream().count();
            }
        }
    }
}
