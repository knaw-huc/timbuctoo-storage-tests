package nl.knaw.huc.timbuctoo.postgresql;

import nl.knaw.huc.timbuctoo.TestCase;
import nl.knaw.huc.timbuctoo.tests.Test;
import nl.knaw.huc.timbuctoo.util.ChangeType;
import nl.knaw.huc.timbuctoo.util.CursorQuad;
import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.RDFHandler;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementContext;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresTestCase extends TestCase {
    private static final String GET_LATEST = "SELECT * FROM \"%s\"";
    private static final String GET_FROM_TYPE = "SELECT * FROM \"%s\" WHERE subject=:subject AND predicate=:predicate AND is_out=:is_out";
    private static final String GET_ABOUT_SUBJECT = "SELECT * FROM \"%s\" WHERE subject=:subject";
    private static final String GET_VERSION = "SELECT * FROM \"%s\" WHERE version=:version AND is_insert=:is_insert";
    private static final String GET_SUBJECTS = "SELECT DISTINCT subject FROM \"%s\" WHERE version=:version";

    private final Test test;
    private final String tableName;

    protected Jdbi jdbi;
    protected RDFHandler rdfHandler;

    public PostgresTestCase(Test test) throws Exception {
        this(test, DigestUtils.md5Hex(test.getName()));

        jdbi = Jdbi.create("jdbc:postgresql://localhost/timbuctoo");
        rdfHandler = new PostgresRdfHandler(test.getName(), test.getBaseUri(), test.getName(), jdbi, 0);

        runTests();
    }

    protected PostgresTestCase(Test test, String tableName) {
        this.test = test;
        this.tableName = tableName;
    }

    @Override
    public String getName() {
        return "PostgreSQL";
    }

    protected void runTests() throws Exception {
        super.runTests(this::importAll, this::getLatest, this::getAllFromType, this::getAllAboutSubject,
                this::getSubjectsOfVersion, this::getAddedChangesOfVersion, this::getDeletedChangesOfVersion);
    }

    private long importAll() throws Exception {
        importRdf(new URL(test.getUrl()).openStream(), test.getBaseUri(), rdfHandler);
        return 0;
    }

    private long getLatest() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_LATEST, tableName))
                .map(PostgresTestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getAllFromType() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_FROM_TYPE, tableName))
                .bind("subject", test.getTestRdfType())
                .bind("predicate", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
                .bind("is_out", false)
                .map(PostgresTestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getAllAboutSubject() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_ABOUT_SUBJECT, tableName))
                .bind("subject", test.getTestSubjectUri())
                .map(PostgresTestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getSubjectsOfVersion() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_SUBJECTS, tableName))
                .bind("version", 0)
                .mapTo(String.class)
                .stream()).count();
    }

    private long getAddedChangesOfVersion() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_VERSION, tableName))
                .bind("version", 0)
                .bind("is_insert", true)
                .map(PostgresTestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getDeletedChangesOfVersion() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_VERSION, tableName))
                .bind("version", 0)
                .bind("is_insert", false)
                .map(PostgresTestCase::mapToCursorQuad)
                .stream()).count();
    }

    private static CursorQuad mapToCursorQuad(ResultSet rs, StatementContext ctx) throws SQLException {
        return CursorQuad.create(
                rs.getString("subject"),
                rs.getString("predicate"),
                rs.getBoolean("is_out") ? Direction.OUT : Direction.IN,
                rs.getBoolean("is_insert") ? ChangeType.ASSERTED : ChangeType.RETRACTED,
                rs.getString("object"),
                rs.getString("type"),
                rs.getString("language"),
                rs.getString("graph"),
                ""
        );
    }
}
