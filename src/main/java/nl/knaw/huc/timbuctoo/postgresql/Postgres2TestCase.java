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

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Collections.nCopies;
import static nl.knaw.huc.timbuctoo.util.ID.createValue;

public class Postgres2TestCase extends TestCase {
    private static final String GET_LATEST = """
            SELECT subj_val.value AS subject,
                   pred_val.value AS predicate,
                   is_out,
                   is_insert,
                   obj_val.value AS object,
                   obj_val.type AS type,
                   obj_val.language AS language,
                   graph_val.value AS graph
            FROM "%s_rdf"
            LEFT JOIN "%s_values" AS subj_val ON subject = subj_val.id
            LEFT JOIN "%s_values" AS pred_val ON predicate = pred_val.id
            LEFT JOIN "%s_values" AS obj_val ON object = obj_val.id
            LEFT JOIN "%s_values" AS graph_val ON graph = graph_val.id""";

    private static final String GET_FROM_TYPE = """
            SELECT subj_val.value AS subject,
                   pred_val.value AS predicate,
                   is_out,
                   is_insert,
                   obj_val.value AS object,
                   obj_val.type AS type,
                   obj_val.language AS language,
                   graph_val.value AS graph
            FROM "%s_rdf"
            LEFT JOIN "%s_values" AS subj_val ON subject = subj_val.id
            LEFT JOIN "%s_values" AS pred_val ON predicate = pred_val.id
            LEFT JOIN "%s_values" AS obj_val ON object = obj_val.id
            LEFT JOIN "%s_values" AS graph_val ON graph = graph_val.id
            WHERE subject=:subject AND predicate=:predicate AND is_out=:is_out""";

    private static final String GET_ABOUT_SUBJECT = """
            SELECT subj_val.value AS subject,
                   pred_val.value AS predicate,
                   is_out,
                   is_insert,
                   obj_val.value AS object,
                   obj_val.type AS type,
                   obj_val.language AS language,
                   graph_val.value AS graph
            FROM "%s_rdf"
            LEFT JOIN "%s_values" AS subj_val ON subject = subj_val.id
            LEFT JOIN "%s_values" AS pred_val ON predicate = pred_val.id
            LEFT JOIN "%s_values" AS obj_val ON object = obj_val.id
            LEFT JOIN "%s_values" AS graph_val ON graph = graph_val.id
            WHERE subject=:subject""";

    private static final String GET_VERSION = """
            SELECT subj_val.value AS subject,
                   pred_val.value AS predicate,
                   is_out,
                   is_insert,
                   obj_val.value AS object,
                   obj_val.type AS type,
                   obj_val.language AS language,
                   graph_val.value AS graph
            FROM "%s_rdf"
            LEFT JOIN "%s_values" AS subj_val ON subject = subj_val.id
            LEFT JOIN "%s_values" AS pred_val ON predicate = pred_val.id
            LEFT JOIN "%s_values" AS obj_val ON object = obj_val.id
            LEFT JOIN "%s_values" AS graph_val ON graph = graph_val.id
            WHERE version=:version AND is_insert=:is_insert""";

    private static final String GET_SUBJECTS = """
            SELECT DISTINCT subj_val.value FROM "%s_rdf"
            LEFT JOIN "%s_values" AS subj_val ON subject = subj_val.id
            LEFT JOIN "%s_values" AS pred_val ON predicate = pred_val.id
            LEFT JOIN "%s_values" AS obj_val ON object = obj_val.id
            LEFT JOIN "%s_values" AS graph_val ON graph = graph_val.id
            WHERE version=:version""";

    private final Test test;
    private final String tableName;

    protected Jdbi jdbi;
    protected RDFHandler rdfHandler;

    public Postgres2TestCase(Test test) throws Exception {
        this(test, DigestUtils.md5Hex(test.getName()));

        jdbi = Jdbi.create("jdbc:postgresql://localhost/timbuctoo");
        rdfHandler = new Postgres2RdfHandler(test.getName(), test.getBaseUri(), test.getName(), jdbi, 0);

        runTests();
    }

    protected Postgres2TestCase(Test test, String tableName) {
        this.test = test;
        this.tableName = tableName;
    }

    @Override
    public String getName() {
        return "PostgreSQL v2";
    }

    protected void runTests() throws Exception {
        super.runTests(this::importAll, this::getLatest, this::getAllFromType, this::getAllAboutSubject,
                this::getSubjectsOfVersion, this::getAddedChangesOfVersion, this::getDeletedChangesOfVersion);
    }

    private long importAll() throws Exception {
        importRdf(test.getInputStream(), test.getBaseUri(), rdfHandler);
        return 0;
    }

    private long getLatest() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_LATEST, nCopies(5, tableName).toArray()))
                .map(Postgres2TestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getAllFromType() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_FROM_TYPE, nCopies(5, tableName).toArray()))
                .bind("subject", createValue(test.getTestRdfType()))
                .bind("predicate", createValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
                .bind("is_out", false)
                .map(Postgres2TestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getAllAboutSubject() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_ABOUT_SUBJECT, nCopies(5, tableName).toArray()))
                .bind("subject", createValue(test.getTestSubjectUri()))
                .map(Postgres2TestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getSubjectsOfVersion() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_SUBJECTS, nCopies(5, tableName).toArray()))
                .bind("version", 0)
                .mapTo(String.class)
                .stream()).count();
    }

    private long getAddedChangesOfVersion() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_VERSION, nCopies(5, tableName).toArray()))
                .bind("version", 0)
                .bind("is_insert", true)
                .map(Postgres2TestCase::mapToCursorQuad)
                .stream()).count();
    }

    private long getDeletedChangesOfVersion() {
        return jdbi.withHandle(handle -> handle.createQuery(String.format(GET_VERSION, nCopies(5, tableName).toArray()))
                .bind("version", 0)
                .bind("is_insert", false)
                .map(Postgres2TestCase::mapToCursorQuad)
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
