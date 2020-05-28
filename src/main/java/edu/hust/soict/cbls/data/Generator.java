package edu.hust.soict.cbls.data;

public class Generator {

    public static void main(String[] args) {
        String path = "data/medium.txt";

        new Generate()
                .setNumOfPassengers(20)
                .setNumOfCommodities(30)
                .setNumOfTaxi(5)
                .generate()
                .save(path);
    }
}
