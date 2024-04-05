package bell_tasks;

import java.util.*;

public class BellTask1 {
    private List<Map<String, String>> employeeList = new ArrayList<>();
    private String[] name;
    private String[] age;
    private String[] position;
    private String[] salary;

    {
        name = new String[]{"Кирилл", "Виталий", "Александр", "Дементий"};
        position = new String[]{"Middle Java Dev", "Senior Java Automation", "Junior Functional Tester", "DEVOPS"};
        salary = new String[]{"150000 руб", "2000$", "50000 руб", "1500$"};
        age = new String[]{"26", "28", "31", "35"};
    }


    public BellTask1() {
        for (int i = 0; i < 4; i++) {
            this.employeeList.add(new LinkedHashMap<>(4));
            Map<String, String> map = employeeList.get(i);
            map.put("Имя", name[i]);
            map.put("Возраст", age[i]);
            map.put("Должность", position[i]);
            map.put("Зарплата", salary[i]);
        }

    }

    public static void main(String[] args) {
        BellTask1 b = new BellTask1();
        double v = b.employeeList.stream().mapToInt(employee -> Integer.parseInt(employee.get("Возраст")))
                .average().orElse(0);
        System.out.println(v);
//
//        for (Map<String,String> emp : b.employeeList){
//            if (Integer.parseInt(emp.get("Возраст")) < 30){
//                System.out.println(emp.get("Имя"));
//            }
//        }

        b.employeeList.stream().filter(employee -> employee.get("Зарплата").contains("руб"))
                .map(employee -> employee.get("Имя")).forEach(System.out::println);



    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(employeeList.get(0).keySet());

        for (Map<String, String> e : employeeList) {
            sb.append("\n");
            sb.append(e.values());
        }
        return sb.toString();
    }
}
