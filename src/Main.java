import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        List<Integer> ls = new ArrayList<>();

        Stream.generate(() -> ThreadLocalRandom.current().nextInt(10)).limit(10).forEach(ls::add);
        int toSearch = 4;
        ls.sort(Comparator.naturalOrder());
        System.out.println(ls);

        System.out.println(toSearch);

        System.out.println(Collections.binarySearch(ls, toSearch));

    }
}
