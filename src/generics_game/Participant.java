package generics_game;

import lombok.Data;

@Data
public abstract class Participant {
String name;
int age;

    public Participant(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
