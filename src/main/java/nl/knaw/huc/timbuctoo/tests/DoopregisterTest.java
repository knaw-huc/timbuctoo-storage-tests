package nl.knaw.huc.timbuctoo.tests;

public class DoopregisterTest extends Test {
    @Override
    public String getName() {
        return "index_op_doopregister_enriched_20191204";
    }

    @Override
    public String getUrl() {
        return "https://repository.goldenagents.org/v5/resourcesync/ufab7d657a250e3461361c982ce9b38f3816e0c4b/index_op_doopregister_enriched_20191204/dataset.nq";
    }

    @Override
    public String getBaseUri() {
        return "https://data.goldenagents.org/datasets/ufab7d657a250e3461361c982ce9b38f3816e0c4b/index_op_doopregister_enriched_20191204/";
    }

    @Override
    public String getTestRdfType() {
        return "https://data.goldenagents.org/datasets/SAA/ontology/Person";
    }

    @Override
    public String getTestSubjectUri() {
        return "https://data.goldenagents.org/datasets/SAA/Person/saaId23491746p3";
    }
}
