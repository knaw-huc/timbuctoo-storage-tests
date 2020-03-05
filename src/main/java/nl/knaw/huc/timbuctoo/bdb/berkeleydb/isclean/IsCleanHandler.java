package nl.knaw.huc.timbuctoo.bdb.berkeleydb.isclean;

public interface IsCleanHandler<TKey, TValue> {
  TKey getKey();

  TValue getValue();
}
