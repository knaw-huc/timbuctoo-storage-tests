package nl.knaw.huc.timbuctoo.rocksdb;

import org.rocksdb.RocksIterator;

import java.util.Iterator;
import java.util.function.BiFunction;

public class RocksDBIterator<R> implements Iterator<R> {
    private final RocksIterator rocksIterator;
    private final BiFunction<byte[], byte[], R> withCurrent;

    private final byte[] prefix;

    public RocksDBIterator(RocksIterator rocksIterator, BiFunction<byte[], byte[], R> withCurrent) {
        this.rocksIterator = rocksIterator;
        this.withCurrent = withCurrent;

        this.prefix = null;
        rocksIterator.seekToFirst();
    }

    public RocksDBIterator(RocksIterator rocksIterator, BiFunction<byte[], byte[], R> withCurrent, byte[] prefix) {
        this.rocksIterator = rocksIterator;
        this.withCurrent = withCurrent;

        this.prefix = prefix;
        rocksIterator.seek(prefix);

    }

    public boolean hasNext() {
        if (!rocksIterator.isValid()) {
            rocksIterator.close();
            return false;
        }

        if (prefix != null) {
            byte[] key = rocksIterator.key();
            for (int i = 0; i < prefix.length; i++) {
                if (key[i] != prefix[i]) {
                    rocksIterator.close();
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public R next() {
        byte[] key = rocksIterator.key();
        byte[] value = rocksIterator.value();
        rocksIterator.next();
        return withCurrent.apply(key, value);
    }
}
