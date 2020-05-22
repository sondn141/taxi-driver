package edu.hust.soict.cbls.data;

public class Generator {

    public static void main(String[] args) {
        String path = "data/test.txt";

        new Generate()
                .setNumOfPassengers(4)
                .setNumOfCommodities(3)
                .setNumOfTaxi(2)
                .generate()
                .save(path);
    }
}
