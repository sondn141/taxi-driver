package edu.hust.soict.cbls.data;

public class Generator {

    public static void main(String[] args) {
        String path = "data/small.txt";

        new Generate()
                .setNumOfPassengers(2)
                .setNumOfCommodities(3)
                .setNumOfTaxi(2)
                .generate()
                .save(path);
    }
}
