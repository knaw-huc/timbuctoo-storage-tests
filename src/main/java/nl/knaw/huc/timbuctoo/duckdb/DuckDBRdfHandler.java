package nl.knaw.huc.timbuctoo.duckdb;

import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.RDFHandler;
import org.apache.commons.codec.digest.DigestUtils;
import org.duckdb.DuckDBAppender;
import org.duckdb.DuckDBConnection;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;

public class DuckDBRdfHandler extends RDFHandler {
    private static final String CREATE_TABLE = "CREATE TABLE \"%s\" (\n" +
            "        subject VARCHAR(2048) NOT NULL,\n" +
            "        predicate VARCHAR(2048) NOT NULL,\n" +
            "        graph VARCHAR(2048) NOT NULL,\n" +
            "        language VARCHAR(3),\n" +
            "        type VARCHAR(2048),\n" +
            "        is_out BOOLEAN NOT NULL,\n" +
            "        version INTEGER NOT NULL,\n" +
            "        is_insert BOOLEAN NOT NULL,\n" +
            "        object TEXT NOT NULL\n" +
            ")\n";

    private final int version;
    private final String tableName;
    private final Handle handle;

    private final DuckDBAppender appender;

    public DuckDBRdfHandler(String dataSetId, String baseUri, String fileName, Jdbi jdbi, int version) throws Exception {
        super(baseUri, null, fileName);
        this.version = version;

        tableName = DigestUtils.md5Hex(dataSetId);

        handle = jdbi.open();
        handle.execute(String.format(CREATE_TABLE, tableName));
        appender = ((DuckDBConnection) handle.getConnection()).createAppender("main", tableName);
    }

    public void commit() throws Exception {
        appender.close();

        handle.execute(String.format("CREATE INDEX subject_idx ON \"%s\" (version, subject)", tableName));
        handle.execute(String.format("CREATE INDEX insert_idx ON \"%s\" (version, is_insert)", tableName));
        handle.execute(String.format("ANALYZE \"%s\"", tableName));
        handle.close();
    }

    protected void putQuad(String subject, String predicate, Direction direction, String object, String valueType,
                           String language, String graph) throws Exception {
        insertQuad(subject, predicate, direction, object, valueType, language, graph, true);
    }

    protected void deleteQuad(String subject, String predicate, Direction direction, String object, String valueType,
                              String language, String graph) throws Exception {
        insertQuad(subject, predicate, direction, object, valueType, language, graph, false);
    }

    private void insertQuad(String subject, String predicate, Direction direction, String object, String valueType,
                            String language, String graph, boolean isInsert) throws SQLException {
        appender.beginRow();
        appender.append(subject);
        appender.append(predicate);
        appender.append(graph);
        appender.append(language);
        appender.append(valueType);
        appender.append(direction == Direction.OUT);
        appender.append(version);
        appender.append(isInsert);
        appender.append(object);
        appender.endRow();
    }
}
