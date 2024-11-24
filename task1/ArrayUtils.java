package task1;

import java.util.Random;

public class ArrayUtils {
    public static int[] initNumbers(int size, int start, int end) {
        Random random = new Random();
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = random.nextInt(end - start + 1) + start;
        }
        return numbers;
    }

    public static int calculatePairwiseSum(int[] array, int start, int end) {
        int sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i] + array[i + 1];
        }
        return sum;
    }
}
