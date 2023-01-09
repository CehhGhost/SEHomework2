import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        File dir = new File("C:\\Users\\ghost\\IdeaProjects");
//        File[] files = dir.listFiles();
//        for (var f : files) {
//            if (f.isDirectory()) {
//                System.out.print("I am a dir and my name is " + f.getName());
//            } else if (f.isFile()) {
//                System.out.print("I am a file and my name is " + f.getName());
//            }
//            System.out.println();
//        }
//        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
//        map.put(null, false);
//        System.out.println(map.get(null));
        File dir = new File("C:\\Users\\ghost\\OneDrive\\Рабочий стол\\Test");
        DependencyList list = new DependencyList(dir);
    }
}