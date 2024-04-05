package bell_tasks;

public class BellTask3 {
    public void printer(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        BellTask3 b = new BellTask3() ;
        b.printer(b.fillMatrix(3));
    }

    public int[][] fillMatrix(int maxValue) {
        int size = (maxValue + 1) * 2;
        int[][] matrix = new int[size][size];

        for (int begin = 0, end = size; maxValue >= 0; begin++, end--, maxValue--) {
            for (int i = begin; i < end; i++) {
                for (int j = begin; j < end; j++) {
                    matrix[i][j] = maxValue;
                }
            }
        }
        return matrix;
    }
}
