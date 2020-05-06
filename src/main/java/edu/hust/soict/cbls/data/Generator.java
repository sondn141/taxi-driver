package edu.hust.soict.cbls.data;

public class Generator {

    public static void main(String[] args) {
        String path = "data/tiny.txt";

        new Generate()
                .generate()
                .save(path);
    }
}
