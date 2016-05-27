package nl.ordina.jtech.mavendependencygraph.neo4j;

import nl.ordina.jtech.mavendependencygraph.model.GSonConverter;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Class: UploaderStatus
 */
public class UploaderStatus implements GSonConverter {

    private static long errorCount;
    private final long executedCount;
    private final int activeCount;
    private final long completedTaskCount;
    private final int poolSize;
    private final int queued;
    private final long taskCount;
    private final long skippedCount;

    private UploaderStatus(long count, long skipped, long errorCnt, ThreadPoolExecutor service) {
        executedCount = count;
        activeCount = service.getActiveCount();
        completedTaskCount = service.getCompletedTaskCount();
        poolSize = service.getPoolSize();
        queued = service.getQueue().size();
        taskCount = service.getTaskCount();
        skippedCount = skipped;
        errorCount = errorCnt;
    }

    public static final UploaderStatus build(final long executeCount, final long skipped, final long errorCnt, final ThreadPoolExecutor service) {
        return new UploaderStatus(executeCount, skipped, errorCnt, service);
    }

    public long getExecutedCount() {
        return executedCount;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public int getQueued() {
        return queued;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public long getSkippedCount() {
        return skippedCount;
    }

    public static long getErrorCount() {
        return errorCount;
    }
}
