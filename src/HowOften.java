import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HowOften {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("вася", "петя", "маша");
        // сколько раз встр каждая буква


        Map<String, Integer> map = new HashMap<>();
        list.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .forEach(c -> map.put(c, map.getOrDefault(c, 0) + 1));

        Map<String, Integer> map2 = list.stream()
                .flatMap(s -> Arrays.stream(s.split("")))
                .collect(Collectors.toMap(c -> c, c -> 1, (a, b) -> Integer.sum(a, b)));
//        for(String s:list){
//            Arrays.stream(s.split("")).forEach(c -> map.put(c, map.getOrDefault(c, 0)+1));
//        }
        list.forEach(s ->
                Arrays.stream(s.split(""))
                        .forEach(c ->
                                map.merge(c, 1, Integer::sum)
                        ));
//        list.forEach(s -> s.chars().forEach(c -> map.put(c, map.getOrDefault(c, 0) + 1)));
//        map.forEach((key, value) -> System.out.println((char) (int) key + ": " + value));
//        map.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println(map);

        System.out.println(list.stream()
                .flatMap(s -> Arrays.stream(s.split("")))
                .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum)));

    }
}
