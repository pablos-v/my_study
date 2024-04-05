package bell_tasks;

import java.util.Random;

public class BellTask2 {
    // random matrix
    public static int[][] generateRandomMatrix(int N) {
        int[][] matrix = new int[N][N];
        Random random = new Random();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = random.nextInt(100); // Генерация случайного числа от 0 до 99 (включительно)
            }
        }

        return matrix;
    }


    public static void main(String[] args) {
        int N = 5; // Задаем размер N для матрицы (5x5 в данном случае)
        int[][] matrix = generateRandomMatrix(N);

        // Выводим сгенерированную матрицу на экран
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        int min = getMin(matrix);

        System.out.println(min);

    }

    public static int getMin(int[][] matrix) {
        int min = matrix[0][matrix.length - 1];

        for (int i = 1, j = matrix.length - 2; i < matrix.length; i++, j--) {
            if (i != j) min = Math.min(min, matrix[i][j]);
        }

        return min;
    }


}
