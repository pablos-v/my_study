public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");
        System.out.println(doIt());


    }


    public static String doIt() {
        System.out.println("Method starts");

        new Thread(() -> {
            System.out.println("Thread starts");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread ends");
        }).start();

        return "Method ends";
    }

}
