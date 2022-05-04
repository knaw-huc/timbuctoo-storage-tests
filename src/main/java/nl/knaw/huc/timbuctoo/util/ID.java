package nl.knaw.huc.timbuctoo.util;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class ID {
    private static final java.util.zip.CRC32 CRC32 = new CRC32();

    public static long createValue(String value) {
        CRC32.reset();
        CRC32.update(value.getBytes(StandardCharsets.UTF_8));
        return CRC32.getValue();
    }
}
