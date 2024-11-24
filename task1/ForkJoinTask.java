package task1;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 1000; // Поріг для розбиття завдань
    private final int[] array;
    private final int start;
    private final int end;

    public ForkJoinTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            return ArrayUtils.calculatePairwiseSum(array, start, end);
        } else {
            // Інакше розбити на підзадачі
            int mid = (start + end) / 2;
            ForkJoinTask leftTask = new ForkJoinTask(array, start, mid);
            ForkJoinTask rightTask = new ForkJoinTask(array, mid + 1, end);

            leftTask.fork(); // Виконати ліву підзадачу асинхронно
            int rightResult = rightTask.compute(); // Виконати праву задачу
            int leftResult = leftTask.join(); // Дочекатися результату лівої задачі

            return leftResult + rightResult;
        }
    }
}