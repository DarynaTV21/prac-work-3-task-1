package task1;

import java.util.concurrent.Callable;

public class CallableTask implements Callable<Integer> {
    private final int[] array;
    private final int start;
    private final int end;

    public CallableTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() {
        return ArrayUtils.calculatePairwiseSum(array, start, end);
    }
}
