package nl.knaw.huc.timbuctoo.postgresql;

import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.RDFHandler;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static nl.knaw.huc.timbuctoo.util.ID.createValue;
import static org.apache.commons.lang3.StringUtils.replaceEach;

public class Postgres2RdfHandler extends RDFHandler {
    private static final String CREATE_RDF_TABLE = """
            CREATE TABLE "%s_rdf" (
                subject   BIGINT  NOT NULL,
                predicate BIGINT  NOT NULL,
                object    BIGINT  NOT NULL,
                graph     BIGINT  NOT NULL,
                is_out    BOOLEAN NOT NULL,
                version   INTEGER NOT NULL,
                is_insert BOOLEAN NOT NULL
            )
            """;
    private static final String CREATE_VALUES_TABLE = """
            CREATE TABLE "%s_values" (
                id       BIGINT        NOT NULL PRIMARY KEY,
                value    TEXT          NOT NULL,
                language VARCHAR(3),
                type     VARCHAR(2048)
            )
            """;

    private static final String CREATE_VALUES_TEMP_TABLE = """
            CREATE TABLE "%s_values_temp" (
                id       BIGINT        NOT NULL,
                value    TEXT          NOT NULL,
                language VARCHAR(3),
                type     VARCHAR(2048)
            )
            """;

    private static final String INSERT = """
            INSERT INTO "%s_values"
            SELECT * FROM "%s_values_temp"
            ON CONFLICT (id) DO NOTHING;
            """;

    private static final String DROP = "DROP TABLE \"%s_values_temp\"";

    private static final String COPY = "COPY \"%s\" FROM STDIN";

    private final int version;
    private final String tableName;
    private final Handle handle;
    private final CopyManager copyManager;

    private int count = 0;
    private ByteArrayOutputStream rdfOutputStream;
    private ByteArrayOutputStream valuesOutputStream;

    public Postgres2RdfHandler(String dataSetId, String baseUri, String fileName, Jdbi jdbi, int version) throws SQLException {
        super(baseUri, null, fileName);
        this.version = version;

        tableName = DigestUtils.md5Hex(dataSetId);

        handle = jdbi.open();
        handle.execute(String.format(CREATE_RDF_TABLE, tableName));
        handle.execute(String.format(CREATE_VALUES_TABLE, tableName));
        handle.execute(String.format(CREATE_VALUES_TEMP_TABLE, tableName));

        copyManager = new CopyManager((BaseConnection) handle.getConnection());
        rdfOutputStream = new ByteArrayOutputStream();
        valuesOutputStream = new ByteArrayOutputStream();
    }

    public void commit() throws SQLException, IOException {
        executeBatch();

        handle.execute(String.format(INSERT, tableName, tableName));
        handle.execute(String.format(DROP, tableName));

        handle.execute(String.format("CREATE INDEX ON \"%s\" (version, subject)", tableName + "_rdf"));
        handle.execute(String.format("CREATE INDEX ON \"%s\" (version, is_insert)", tableName + "_rdf"));

        handle.execute(String.format("ANALYZE \"%s\"", tableName + "_rdf"));
        handle.execute(String.format("ANALYZE \"%s\"", tableName + "_values"));

        handle.close();
    }

    protected void putQuad(String subject, String predicate, Direction direction, String object, String valueType,
                           String language, String graph) throws SQLException, IOException {
        insertQuad(subject, predicate, direction, object, valueType, language, graph, true);
    }

    protected void deleteQuad(String subject, String predicate, Direction direction, String object, String valueType,
                              String language, String graph) throws SQLException, IOException {
        insertQuad(subject, predicate, direction, object, valueType, language, graph, false);
    }

    private void insertQuad(String subject, String predicate, Direction direction, String object, String valueType,
                            String language, String graph, boolean isInsert) throws SQLException, IOException {
        long subjectId = createValue(subject);
        long predicateId = createValue(predicate);
        long objectId = createValue((valueType == null ? "" : valueType) + "\n" + (language == null ? "" : language) + "\n" + object);
        long graphId = createValue(graph);

        String data = subjectId + "\t" +
                predicateId + "\t" +
                objectId + "\t" +
                graphId + "\t" +
                (direction == Direction.OUT) + "\t" +
                version + "\t" +
                isInsert + "\n";

        rdfOutputStream.write(data.getBytes(StandardCharsets.UTF_8));

        valuesOutputStream.write((subjectId + "\t" + escapeString(subject) + "\t\\N\t\\N\n").getBytes(StandardCharsets.UTF_8));
        valuesOutputStream.write((predicateId + "\t" + escapeString(predicate) + "\t\\N\t\\N\n").getBytes(StandardCharsets.UTF_8));
        valuesOutputStream.write((graphId + "\t" + escapeString(graph) + "\t\\N\t\\N\n").getBytes(StandardCharsets.UTF_8));

        String valuesData = objectId + "\t" +
                escapeString(object) + "\t" +
                escapeString(language) + "\t" +
                escapeString(valueType) + "\n";

        valuesOutputStream.write(valuesData.getBytes(StandardCharsets.UTF_8));

        count++;
        if (count % 5000 == 0) {
            executeBatch();
        }
    }

    private void executeBatch() throws SQLException, IOException {
        copyManager.copyIn(String.format(COPY, tableName + "_rdf"), new ByteArrayInputStream(rdfOutputStream.toByteArray()));
        copyManager.copyIn(String.format(COPY, tableName + "_values_temp"), new ByteArrayInputStream(valuesOutputStream.toByteArray()));

        rdfOutputStream = new ByteArrayOutputStream();
        valuesOutputStream = new ByteArrayOutputStream();
    }

    private static String escapeString(String value) {
        if (value == null)
            return "\\N";

        return replaceEach(value,
                new String[]{"\\", "\b", "\f", "\n", "\r", "\t"},
                new String[]{"\\\\", "\\b", "\\f", "\\n", "\\r", "\\t"});
    }
}
