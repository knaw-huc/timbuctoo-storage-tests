package nl.knaw.huc.timbuctoo.bdb.berkeleydb.isclean;

public class StringStringIsCleanHandler implements IsCleanHandler<String, String> {
  public String getKey() {
    return "isClean";
  }

  public String getValue() {
    return "isClean";
  }
}
