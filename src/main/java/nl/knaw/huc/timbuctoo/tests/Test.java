package nl.knaw.huc.timbuctoo.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class Test {
    public abstract String getName();

    public abstract String getUrl();

    public abstract String getBaseUri();

    public abstract String getTestRdfType();

    public abstract String getTestSubjectUri();

    public InputStream getInputStream() throws IOException {
        return new GZIPInputStream(new FileInputStream("./data/dataset"));
    }

    public void downloadDataset() throws IOException {
        File file = new File("./data/dataset");
        file.getParentFile().mkdirs();

        if (!file.exists()) {
            file.createNewFile();

            try (InputStream inputStream = new URL(getUrl()).openStream();
                 OutputStream outputStream = new GZIPOutputStream(new FileOutputStream(file))) {
                inputStream.transferTo(outputStream);
            }
        }
    }
}
