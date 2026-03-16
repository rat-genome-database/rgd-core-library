package edu.mcw.rgd.process;

public class MemoryMonitor {

    private static final long DEFAULT_SAMPLE_INTERVAL_MS = 5_000;

    private final long sampleIntervalMs;
    private volatile boolean running;
    private Thread thread;

    private int sampleCount;
    private long sum;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    public MemoryMonitor() {
        this(DEFAULT_SAMPLE_INTERVAL_MS);
    }

    public MemoryMonitor(long sampleIntervalMs) {
        this.sampleIntervalMs = sampleIntervalMs;
    }

    public void start() {
        running = true;
        thread = new Thread(() -> {
            while( running ) {
                long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                synchronized( this ) {
                    // skip first sample -- it is often a big outlier
                    if( sampleCount > 0 ) {
                        sum += usedMemory;
                        if( usedMemory < min ) min = usedMemory;
                        if( usedMemory > max ) max = usedMemory;
                    }
                    sampleCount++;
                }
                try {
                    Thread.sleep(sampleIntervalMs);
                } catch( InterruptedException e ) {
                    break;
                }
            }
        }, "MemoryMonitor");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
        if( thread != null ) {
            thread.interrupt();
        }
    }

    public synchronized String getSummary() {
        int effectiveSamples = sampleCount - 1; // first sample is skipped
        if( effectiveSamples <= 0 ) {
            return "MEMORY: no samples collected";
        }
        long avg = sum / effectiveSamples;
        return "MEMORY: samples=" + effectiveSamples
                + "  min=" + formatGB(min)
                + "  max=" + formatGB(max)
                + "  avg=" + formatGB(avg);
    }

    private String formatGB(long bytes) {
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}
