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

import static org.apache.commons.lang3.StringUtils.replaceEach;

public class PostgresRdfHandler extends RDFHandler {
    private static final String CREATE_TABLE = """
            CREATE TABLE "%s" (
                subject   VARCHAR(2048) NOT NULL,
                predicate VARCHAR(2048) NOT NULL,
                graph     VARCHAR(2048) NOT NULL,
                language  VARCHAR(3),
                type      VARCHAR(2048),
                is_out    BOOLEAN       NOT NULL,
                version   INTEGER       NOT NULL,
                is_insert BOOLEAN       NOT NULL,
                object    TEXT          NOT NULL
            )
            """;

    private static final String COPY = "COPY \"%s\" FROM STDIN";

    private final int version;
    private final String tableName;
    private final Handle handle;
    private final CopyManager copyManager;

    private int count = 0;
    private ByteArrayOutputStream outputStream;

    public PostgresRdfHandler(String dataSetId, String baseUri, String fileName, Jdbi jdbi, int version) throws SQLException {
        super(baseUri, null, fileName);
        this.version = version;

        tableName = DigestUtils.md5Hex(dataSetId);

        handle = jdbi.open();
        handle.execute(String.format(CREATE_TABLE, tableName));

        copyManager = new CopyManager((BaseConnection) handle.getConnection());
        outputStream = new ByteArrayOutputStream();
    }

    public void commit() throws SQLException, IOException {
        executeBatch();

        handle.execute(String.format("CREATE INDEX ON \"%s\" (version, subject)", tableName));
        handle.execute(String.format("CREATE INDEX ON \"%s\" (version, is_insert)", tableName));
        handle.execute(String.format("ANALYZE \"%s\"", tableName));

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
        String data = escapeString(subject) + "\t" +
                escapeString(predicate) + "\t" +
                escapeString(graph) + "\t" +
                escapeString(language) + "\t" +
                escapeString(valueType) + "\t" +
                (direction == Direction.OUT) + "\t" +
                version + "\t" +
                isInsert + "\t" +
                escapeString(object) + "\n";

        outputStream.write(data.getBytes(StandardCharsets.UTF_8));

        count++;
        if (count % 5000 == 0) {
            executeBatch();
        }
    }

    private void executeBatch() throws SQLException, IOException {
        copyManager.copyIn(String.format(COPY, tableName), new ByteArrayInputStream(outputStream.toByteArray()));
        outputStream = new ByteArrayOutputStream();
    }

    private static String escapeString(String value) {
        if (value == null)
            return "\\N";

        return replaceEach(value,
                new String[]{"\\", "\b", "\f", "\n", "\r", "\t"},
                new String[]{"\\\\", "\\b", "\\f", "\\n", "\\r", "\\t"});
    }
}
