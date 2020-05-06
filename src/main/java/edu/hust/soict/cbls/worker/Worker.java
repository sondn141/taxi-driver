package edu.hust.soict.cbls.worker;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.common.datastructure.Point;
import edu.hust.soict.cbls.data.Reader;

import java.util.ArrayList;
import java.util.List;

public class Worker {

    public static void main(String[] args) {
        Input input = Reader.read("data/tiny.txt");
        List<Point> ps = new ArrayList<>();

        System.out.println(input.toString());
    }
}
