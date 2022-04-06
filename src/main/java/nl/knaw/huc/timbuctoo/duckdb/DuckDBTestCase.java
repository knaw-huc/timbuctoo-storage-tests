package nl.knaw.huc.timbuctoo.duckdb;

import nl.knaw.huc.timbuctoo.postgresql.PostgresTestCase;
import nl.knaw.huc.timbuctoo.tests.Test;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdbi.v3.core.Jdbi;

import java.io.File;

public class DuckDBTestCase extends PostgresTestCase {
    public DuckDBTestCase(Test test) throws Exception {
        super(test, DigestUtils.md5Hex(test.getName()));

        jdbi = Jdbi.create("jdbc:duckdb:" + new File("./data/duckdb").getCanonicalPath());
        rdfHandler = new DuckDBRdfHandler(test.getName(), test.getBaseUri(), test.getName(), jdbi, 0);

        runTests();
    }

    @Override
    public String getName() {
        return "DuckDB";
    }
}
