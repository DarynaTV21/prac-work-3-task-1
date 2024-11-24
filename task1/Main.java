package task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scr = new Scanner(System.in);
        System.out.print("Введіть кількість елементів масиву: ");

        // Перевірка на коректність вводу
        if (!scr.hasNextInt()) {
            System.out.println("Помилка: потрібно ввести ціле число.");
            return;
        }
        final int size = scr.nextInt();

        System.out.print("Введіть діапазон генерації чисел (у форматі minNum maxNum): ");

        if (!scr.hasNextInt()) {
            System.out.println("Помилка: потрібно ввести ціле число.");
            return;
        }
        int start = scr.nextInt();

        if (!scr.hasNextInt()) {
            System.out.println("Помилка: потрібно ввести ціле число.");
            return;
        }
        int end = scr.nextInt();

        if (start > end) {
            System.out.println("Помилка: minNum має бути меншим або дорівнювати maxNum.");
            return;
        }

        // Генерація масиву
        int[] array = ArrayUtils.initNumbers(size, start, end);

        // Вимірювання часу виконання
        long stealingStartTime = System.currentTimeMillis();

        //Work stealing підхід
        try (ForkJoinPool pool = new ForkJoinPool()) {
            long result = pool.invoke(new ForkJoinTask(array, 0, array.length - 1));

            long stealingEndTime = System.currentTimeMillis();
            System.out.println("Згенерований масив: " + Arrays.toString(array));
            System.out.println("Work stealing результат: " + result);
            System.out.println("Час виконання: " + (stealingEndTime - stealingStartTime) + " мс");
        }

        // Вимірювання часу виконання
        long dealingStartTime = System.currentTimeMillis();

        //Work dealing підхід
        ExecutorService executor = Executors.newFixedThreadPool(4);
        // Ділимо завдання на частини
        int chunkSize = array.length / 4; // Кількість потоків = 4
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < array.length - 1; i += chunkSize) {
            int endIdx = Math.min(i + chunkSize, array.length - 1); // Масив обробляється попарно
            futures.add(executor.submit(new CallableTask(array, i, endIdx)));
        }
        executor.shutdown();

        // Підрахунок результату
        int sum = 0;
        for (Future<Integer> future : futures) {
            sum += future.get(); // Отримуємо результат з кожного завдання
        }
        long dealingEndTime = System.currentTimeMillis();

        System.out.println("Work dealing результат: " + sum);
        System.out.println("Час виконання: " + (dealingEndTime - dealingStartTime) + " мс");
    }
}