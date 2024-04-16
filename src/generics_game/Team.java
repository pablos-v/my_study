package generics_game;

import java.util.ArrayList;
import java.util.List;

public class Team <T extends Participant>{
private String name;

private List<T> ls = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
    public void addNewParticipant(T participant){
        ls.add(participant);
    }

    public void playWith(Team<T> team){
        System.out.println(team);
    }
}
