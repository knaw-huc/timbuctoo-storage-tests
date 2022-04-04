package nl.knaw.huc.timbuctoo.bdb;

import com.sleepycat.bind.tuple.TupleBinding;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.BdbEnvironmentCreator;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.exceptions.BdbDbCreationException;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.isclean.StringIntegerIsCleanHandler;
import nl.knaw.huc.timbuctoo.bdb.berkeleydb.isclean.StringStringIsCleanHandler;
import nl.knaw.huc.timbuctoo.bdb.datastores.implementations.bdb.BdbQuadStore;
import nl.knaw.huc.timbuctoo.bdb.datastores.implementations.bdb.BdbTruePatchStore;
import nl.knaw.huc.timbuctoo.bdb.datastores.implementations.bdb.UpdatedPerPatchStore;
import nl.knaw.huc.timbuctoo.util.DataStoreCreationException;

public class BdbDataSource {
    private final BdbEnvironmentCreator dataStoreFactory;
    private final String userId;
    private final String dataSetId;

    public BdbQuadStore quadStore;
    public UpdatedPerPatchStore updatedPerPatchStore;
    public BdbTruePatchStore truePatchStore;

    public BdbDataSource(BdbEnvironmentCreator dataStoreFactory, String userId, String dataSetId) {
        this.dataStoreFactory = dataStoreFactory;
        this.userId = userId;
        this.dataSetId = dataSetId;

        create();
    }

    public void commit() {
        quadStore.commit();
        updatedPerPatchStore.commit();
        truePatchStore.commit();
    }

    private void create() {
        try {
            final TupleBinding<String> stringBinding = TupleBinding.getPrimitiveBinding(String.class);
            final TupleBinding<Integer> integerBinding = TupleBinding.getPrimitiveBinding(Integer.class);

            final StringStringIsCleanHandler stringStringIsCleanHandler = new StringStringIsCleanHandler();
            final StringIntegerIsCleanHandler stringIntegerIsCleanHandler = new StringIntegerIsCleanHandler();

            quadStore = new BdbQuadStore(dataStoreFactory.getDatabase(
                    userId,
                    dataSetId,
                    "rdfData",
                    true,
                    stringBinding,
                    stringBinding,
                    stringStringIsCleanHandler
            ));


            updatedPerPatchStore = new UpdatedPerPatchStore(
                    dataStoreFactory.getDatabase(
                            userId,
                            dataSetId,
                            "updatedPerPatch",
                            true,
                            stringBinding,
                            integerBinding,
                            stringIntegerIsCleanHandler
                    )
            );

            truePatchStore = new BdbTruePatchStore(version ->
                    dataStoreFactory.getDatabase(
                            userId,
                            dataSetId,
                            "truePatch" + version,
                            true,
                            stringBinding,
                            stringBinding,
                            stringStringIsCleanHandler
                    ),
                    updatedPerPatchStore
            );
        } catch (BdbDbCreationException | DataStoreCreationException e) {
            e.printStackTrace();
        }
    }
}
