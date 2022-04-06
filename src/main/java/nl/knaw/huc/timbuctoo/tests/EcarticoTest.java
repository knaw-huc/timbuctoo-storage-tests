package nl.knaw.huc.timbuctoo.tests;

public class EcarticoTest implements Test {
    @Override
    public String getName() {
        return "ecartico_20200316";
    }

    @Override
    public String getUrl() {
        return "https://repository.goldenagents.org/v5/resourcesync/ufab7d657a250e3461361c982ce9b38f3816e0c4b/ecartico_20200316/dataset.nq";
    }

    @Override
    public String getBaseUri() {
        return "https://data.goldenagents.org/datasets/ufab7d657a250e3461361c982ce9b38f3816e0c4b/ecartico_20200316/";
    }

    @Override
    public String getTestRdfType() {
        return "http://xmlns.com/foaf/0.1/Person";
    }

    @Override
    public String getTestSubjectUri() {
        return "http://www.vondel.humanities.uva.nl/ecartico/persons/10000";
    }
}
