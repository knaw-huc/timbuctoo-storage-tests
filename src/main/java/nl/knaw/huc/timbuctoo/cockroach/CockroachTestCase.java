package nl.knaw.huc.timbuctoo.cockroach;

import nl.knaw.huc.timbuctoo.postgresql.PostgresRdfHandler;
import nl.knaw.huc.timbuctoo.postgresql.PostgresTestCase;
import nl.knaw.huc.timbuctoo.tests.Test;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdbi.v3.core.Jdbi;

public class CockroachTestCase extends PostgresTestCase {
    public CockroachTestCase(Test test) throws Exception {
        super(test, DigestUtils.md5Hex(test.getName()));

        jdbi = Jdbi.create("jdbc:postgresql://localhost:26257/defaultdb?sslmode=disable&user=root");
        rdfHandler = new PostgresRdfHandler(test.getName(), test.getBaseUri(), test.getName(), jdbi, 0);

        runTests();
    }

    @Override
    public String getName() {
        return "Cockroach";
    }
}
