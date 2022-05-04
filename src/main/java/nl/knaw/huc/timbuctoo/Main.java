package nl.knaw.huc.timbuctoo;

import nl.knaw.huc.timbuctoo.bdb.BdbTestCase;
import nl.knaw.huc.timbuctoo.cockroach.CockroachTestCase;
import nl.knaw.huc.timbuctoo.duckdb.DuckDBTestCase;
import nl.knaw.huc.timbuctoo.postgresql.Postgres2TestCase;
import nl.knaw.huc.timbuctoo.postgresql.PostgresTestCase;
import nl.knaw.huc.timbuctoo.rdf4j_lmdb.Rdf4JLmdbSparqlTestCase;
import nl.knaw.huc.timbuctoo.rdf4j_lmdb.Rdf4JLmdbTestCase;
import nl.knaw.huc.timbuctoo.rocksdb.RocksDBTestCase;
import nl.knaw.huc.timbuctoo.tests.DoopregisterTest;
import nl.knaw.huc.timbuctoo.tests.EcarticoTest;
import nl.knaw.huc.timbuctoo.tests.Test;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        withTest(new EcarticoTest());
        //withTest(new DoopregisterTest());
    }

    private static void withTest(Test test) throws Exception {
        test.downloadDataset();

        List<TestCase> testCases = List.of(
                new BdbTestCase(test),
                new PostgresTestCase(test),
                new Postgres2TestCase(test),
                new CockroachTestCase(test),
                new DuckDBTestCase(test),
                new RocksDBTestCase(test),
                new Rdf4JLmdbSparqlTestCase(test),
                new Rdf4JLmdbTestCase(test)
        );

        TestReport report = new TestReport(testCases);
        System.out.println(report.getReports());
        System.out.println(report.getFullReport());
    }
}
