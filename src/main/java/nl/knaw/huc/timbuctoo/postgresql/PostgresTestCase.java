package nl.knaw.huc.timbuctoo.postgresql;

import nl.knaw.huc.timbuctoo.TestCase;
import nl.knaw.huc.timbuctoo.bdb.datastores.quadstore.dto.ChangeType;
import nl.knaw.huc.timbuctoo.bdb.datastores.quadstore.dto.CursorQuad;
import nl.knaw.huc.timbuctoo.util.Direction;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementContext;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresTestCase extends TestCase {
    private static final String NAME = "PostgreSQL";

    private static final String GET_LATEST = "SELECT * FROM \"%s\"";
    private static final String GET_VERSION = "SELECT * FROM \"%s\" WHERE version=:version AND is_insert=:is_insert";
    private static final String GET_SUBJECTS = "SELECT DISTINCT subject FROM \"%s\" WHERE version=:version";

    private final String url;
    private final String baseUri;
    private final int version;
    private final Jdbi jdbi;
    private final PostgresRdfHandler rdfHandler;
    private final String tableName;

    public PostgresTestCase(String url, String userId, String name, String baseUri, int version) throws Exception {
        this.url = url;
        this.baseUri = baseUri;
        this.version = version;

        jdbi = Jdbi.create("jdbc:postgresql://localhost/timbuctoo");
        rdfHandler = new PostgresRdfHandler(userId, name, baseUri, jdbi, version);
        tableName = DigestUtils.md5Hex(String.format("%s_%s", userId, name));

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
        rdfHandler.commitAndClose();
    }

    private void getLatest() {
        jdbi.withHandle(handle -> handle.createQuery(String.format(GET_LATEST, tableName))
                .map(PostgresTestCase::mapToCursorQuad)
                .stream()).count();
    }

    private void getSubjectsOfVersion() {
        jdbi.withHandle(handle -> handle.createQuery(String.format(GET_SUBJECTS, tableName))
                .bind("version", version)
                .mapTo(String.class)
                .stream()).count();
    }

    private void getAddedChangesOfVersion() {
        jdbi.withHandle(handle -> handle.createQuery(String.format(GET_VERSION, tableName))
                .bind("version", version)
                .bind("is_insert", true)
                .map(PostgresTestCase::mapToCursorQuad)
                .stream()).count();
    }

    private void getDeletedChangesOfVersion() {
        jdbi.withHandle(handle -> handle.createQuery(String.format(GET_VERSION, tableName))
                .bind("version", version)
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
                ""
        );
    }
}
