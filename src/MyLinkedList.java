/**
 * Так я бы реализовал Линкед Лист
 * @param <T>
 */
public class MyLinkedList<T> {
    Node<T> first;

    private static class Node<T> {
        private Node<T> prev;
        private Node<T> next;
        private T container;

        protected Node(T item) {
            this.prev = null;
            this.next = null;
            this.container = item;
        }

        @Override
        public String toString() {

            String p = this.prev == null ? "null" : this.prev.container.toString();
            String n = this.next == null ? "null" : this.next.container.toString();
            return p + "-" + this.container.toString() + "-" + n;
        }
    }

    public void add(T item) {
        Node<T> newNode = new Node<>(item);
        if (first == null) {
            first = newNode;
        } else {
            Node<T> last = first;
            while (last.next != null) {
                last = last.next;
            }
            last.next = newNode;
            newNode.prev = last;
        }
    }

    public void print() {
        Node<T> last = first;
        System.out.println(last);

        while (last.next != null) {
            System.out.println(last.next);
            last = last.next;
        }
    }


    public static void main(String[] args) {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.add(5);
        list.add(4);
        list.add(52);
        list.add(544);
        list.print();
    }
}
