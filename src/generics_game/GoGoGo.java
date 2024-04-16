package generics_game;

import java.util.ArrayList;
import java.util.List;

public class GoGoGo {
    public static void main(String[] args) {
        Student stud1 = new Student("qqq", 21);
        Student stud2 = new Student("eee", 20);

        Alumn al1 = new Alumn("evvvee", 25);
        Alumn al2 = new Alumn("ccc", 24);

        Employee e1 = new Employee("bbb", 41);
        Employee e2 = new Employee("eennne", 36);


        Team<Student> t1 = new Team<>("Loins");
        t1.addNewParticipant(stud2);
        t1.addNewParticipant(stud1);

        Team<Alumn> exp = new Team<>("rr");
        Team<Alumn> exp2 = new Team<>("rr");
        exp.addNewParticipant(al2);
        exp2.addNewParticipant(al1);
//        t1.addNewParticipant(al1);
//        t1.addNewParticipant(e1);
        exp.playWith(exp2);

        


    }

}
