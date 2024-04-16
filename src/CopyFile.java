import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Пример кода копирования файла
 */
public class CopyFile {

    public static void main(String[] args) {
        byte[] buffer = new byte[1024]; // размер буфера копировальщика
        try (FileInputStream input = new FileInputStream("d:\\12.jpg");
             FileOutputStream output = new FileOutputStream("d:\\21.jpg")) {
            while (input.read(buffer) != -1) { // пока есть данные
                output.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
