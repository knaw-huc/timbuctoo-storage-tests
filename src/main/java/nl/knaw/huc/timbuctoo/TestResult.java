package nl.knaw.huc.timbuctoo;

import com.google.common.base.Stopwatch;

public class TestResult {
    public Stopwatch stopwatch;
    public long count;

    public TestResult(Stopwatch stopwatch, long count) {
        this.stopwatch = stopwatch;
        this.count = count;
    }
}