package nl.knaw.huc.timbuctoo.postgresql;

import nl.knaw.huc.timbuctoo.util.Direction;
import nl.knaw.huc.timbuctoo.util.RDFHandler;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

public class PostgresRdfHandler extends RDFHandler {
    private static final String CREATE_TABLE = "CREATE TABLE \"%s\" (\n" +
            "        subject VARCHAR(2048) NOT NULL,\n" +
            "        predicate VARCHAR(2048) NOT NULL,\n" +
            "        language VARCHAR(3),\n" +
            "        type VARCHAR(2048),\n" +
            "        is_out BOOLEAN NOT NULL,\n" +
            "        version INTEGER NOT NULL,\n" +
            "        is_insert BOOLEAN NOT NULL,\n" +
            "        object TEXT NOT NULL\n" +
            ")\n";

    private static final String PUT =
            "INSERT INTO \"%s\" VALUES (:subject, :predicate, :language, :type, :is_out, :version, :is_insert, :object)";

    private final int currentversion;
    private final String tableName;
    private final Handle handle;
    private final PreparedBatch batch;

    private int count = 0;

    public PostgresRdfHandler(String userId, String dataSetId, String defaultGraph, Jdbi jdbi, int currentversion) {
        super(defaultGraph);
        this.currentversion = currentversion;

        tableName = DigestUtils.md5Hex(String.format("%s_%s", userId, dataSetId));

        handle = jdbi.open();
        handle.execute(String.format(CREATE_TABLE, tableName));

        batch = handle.prepareBatch(String.format(PUT, tableName));
    }

    public void commitAndClose() {
        batch.execute();

        handle.execute(String.format("CREATE INDEX ON \"%s\" (version, subject)", tableName));
        handle.execute(String.format("CREATE INDEX ON \"%s\" (version, is_insert)", tableName));
        handle.execute(String.format("ANALYZE \"%s\"", tableName));

        handle.close();
    }

    protected void putQuad(String subject, String predicate, Direction direction, String object, String valueType,
                           String language) {
        insertQuad(subject, predicate, direction, object, valueType, language, true);
    }

    protected void deleteQuad(String subject, String predicate, Direction direction, String object, String valueType,
                              String language) {
        insertQuad(subject, predicate, direction, object, valueType, language, false);
    }

    private void insertQuad(String subject, String predicate, Direction direction, String object, String valueType,
                            String language, boolean isInsert) {
        batch.bind("subject", subject)
                .bind("predicate", predicate)
                .bind("language", language)
                .bind("type", valueType)
                .bind("is_out", direction == Direction.OUT)
                .bind("version", currentversion)
                .bind("is_insert", isInsert)
                .bind("object", object)
                .add();

        count++;
        if (count % 5000 == 0) {
            batch.execute();
        }
    }
}
