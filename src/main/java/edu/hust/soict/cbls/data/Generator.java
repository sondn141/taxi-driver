package edu.hust.soict.cbls.data;

public class Generator {

    public static void main(String[] args) {
        String path = "data/tiny.txt";

        new Generate()
                .setNumOfPassengers(4)
                .setNumOfCommodities(6)
                .setNumOfTaxi(3)
                .generate()
                .save(path);
    }
}
