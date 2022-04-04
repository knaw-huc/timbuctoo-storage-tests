package nl.knaw.huc.timbuctoo.bdb.berkeleydb.isclean;

public class StringIntegerIsCleanHandler implements IsCleanHandler<String, Integer> {
  @Override
  public String getKey() {
    return "isClean";
  }

  @Override
  public Integer getValue() {
    return Integer.MAX_VALUE;
  }
}
