import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Main {
    public static final int NUMBER = 1_000_000;
    public static final int RANGE = 100_000_0000;

    public static void main(String[] args) throws InterruptedException {
        Map<Integer, Double> map1 = new ConcurrentHashMap<>();
        Map<Integer, Double> map2 = Collections.synchronizedMap(new HashMap<>());

        long startTime1 = System.currentTimeMillis();
        ExecutorService executorService = saveToMap(map1);
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        executorService = readFromMap(map1);
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long finishTime1 = System.currentTimeMillis();


        long startTime2 = System.currentTimeMillis();
        executorService = saveToMap(map2);
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        executorService = readFromMap(map2);
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long finishTime2 = System.currentTimeMillis();


        System.out.println("Size of elements: " + NUMBER);
        System.out.println("ConcurrentHashMap Size: " + map1.size());
        System.out.println("ConcurrentHashMap Time: " + (finishTime1 - startTime1));

        System.out.println("Collections.synchronizedMap Size: " + map2.size());
        System.out.println("Collections.synchronizedMap Time: " + (finishTime2 - startTime2));

    }

    public static ExecutorService saveToMap(final Map<Integer, Double> map) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < NUMBER; i++) {
            executorService.submit(() -> {
                map.put(ThreadLocalRandom.current().nextInt(RANGE), ThreadLocalRandom.current().nextInt(RANGE) * 0.1);
                //System.out.println("Saving " + Thread.currentThread().getName() + "  " + map);
            });
        }
        executorService.shutdown();
        return executorService;
    }

    public static ExecutorService readFromMap(Map<Integer, Double> map) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (Map.Entry<Integer, Double> item : map.entrySet()) {
            //executorService.submit(() -> System.out.println("Reading " + item.getKey() + " - " + item.getValue() + " " + Thread.currentThread().getName()));
            int a = item.getKey();
            double b = item.getValue();
        }
        executorService.shutdown();
        return executorService;
    }
}

