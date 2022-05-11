package nl.knaw.huc.timbuctoo.util;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class ID {
    private static final CRC32 CRC_32 = new CRC32();

    public static long createValue(String value) {
        CRC_32.reset();
        CRC_32.update(value.getBytes(StandardCharsets.UTF_8));
        return CRC_32.getValue();
    }
}
