package nl.knaw.huc.timbuctoo.bdb.berkeleydb;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

public interface DatabaseFunction {
  OperationStatus apply(Cursor cursor) throws DatabaseException;
}
