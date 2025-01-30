import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionProcessor {
    private ExecutorService executorService;

    public TransactionProcessor() {
        this.executorService = Executors.newFixedThreadPool(5); // Thread pool with 5 threads
    }

    public void processTransaction(Runnable transactionTask) {
        executorService.submit(transactionTask);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
