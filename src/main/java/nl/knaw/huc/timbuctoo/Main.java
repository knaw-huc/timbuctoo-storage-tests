package nl.knaw.huc.timbuctoo;

import nl.knaw.huc.timbuctoo.bdb.BdbTestCase;
import nl.knaw.huc.timbuctoo.postgresql.PostgresTestCase;

public class Main {
    private static final String USER_ID = "12345";
    private static final int VERSION = 0;

    private static final String ECARTICO_URL = "https://repository.goldenagents.org/v5/resourcesync/ufab7d657a250e3461361c982ce9b38f3816e0c4b/ecartico_20190805/dataset.nq";
    private static final String ECARTICO_BASE_URI = "https://data.goldenagents.org/datasets/ufab7d657a250e3461361c982ce9b38f3816e0c4b/ecartico_20190805/";
    private static final String ECARTICO_GRAPH_URI = "https://data.goldenagents.org/datasets/ufab7d657a250e3461361c982ce9b38f3816e0c4b/ecartico_20190805";
    private static final String ECARTICO_NAME = "ecartico_20190805";

    public static void main(String[] args) throws Exception {
        BdbTestCase bdbTestCase = new BdbTestCase(ECARTICO_URL, USER_ID, ECARTICO_NAME, ECARTICO_BASE_URI, ECARTICO_GRAPH_URI, ECARTICO_NAME, VERSION);
        PostgresTestCase postgresTestCase = new PostgresTestCase(ECARTICO_URL, USER_ID, ECARTICO_NAME, ECARTICO_BASE_URI, ECARTICO_GRAPH_URI, ECARTICO_NAME, VERSION);

        bdbTestCase.printReport();
        postgresTestCase.printReport();
    }
}
