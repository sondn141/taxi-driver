package edu.hust.soict.cbls.data;

import static edu.hust.soict.cbls.data.Generate.Distribution.*;

public class Generator {

    public static void main(String[] args) {

        new Generate()
                .setNumOfPassengers(2)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(3)
                .setCommodityLocationDistribution(UNIFORM)
                .setNumOfTaxi(2)
                .generate()
                .save("data/exac/exac_232.txt");

        new Generate()
                .setNumOfPassengers(3)
                .setPassengerLocationDistribution(NORMAL)
                .setNumOfCommodities(2)
                .setCommodityLocationDistribution(UNIFORM)
                .setNumOfTaxi(2)
                .generate()
                .save("data/exac/exac_322.txt");

        new Generate()
                .setNumOfPassengers(2)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(3)
                .setCommodityLocationDistribution(NORMAL)
                .setNumOfTaxi(3)
                .generate()
                .save("data/exac/exac_233.txt");

        new Generate()
                .setNumOfPassengers(4)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(3)
                .setCommodityLocationDistribution(UNIFORM)
                .setNumOfTaxi(3)
                .generate()
                .save("data/exac/exac_433.txt");

        // ==============================================================================

        new Generate()
                .setNumOfPassengers(20)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(30)
                .setCommodityLocationDistribution(UNIFORM)
                .setNumOfTaxi(5)
                .generate()
                .save("data/appr/appr_20305.txt");

        new Generate()
                .setNumOfPassengers(30)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(40)
                .setCommodityLocationDistribution(NORMAL)
                .setNumOfTaxi(7)
                .generate()
                .save("data/appr/appr_30407.txt");

        new Generate()
                .setNumOfPassengers(60)
                .setPassengerLocationDistribution(NORMAL)
                .setNumOfCommodities(80)
                .setCommodityLocationDistribution(UNIFORM)
                .setNumOfTaxi(10)
                .generate()
                .save("data/appr/appr_608010.txt");

        new Generate()
                .setNumOfPassengers(100)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(120)
                .setCommodityLocationDistribution(UNIFORM)
                .setNumOfTaxi(10)
                .generate()
                .save("data/appr/appr_10012010.txt");

        new Generate()
                .setNumOfPassengers(200)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(150)
                .setCommodityLocationDistribution(UNIFORM)
                .setNumOfTaxi(20)
                .generate()
                .save("data/appr/appr_20015020.txt");

        new Generate()
                .setNumOfPassengers(500)
                .setPassengerLocationDistribution(NORMAL)
                .setNumOfCommodities(200)
                .setCommodityLocationDistribution(NORMAL)
                .setNumOfTaxi(50)
                .generate()
                .save("data/appr/appr_50020050.txt");

        new Generate()
                .setNumOfPassengers(1000)
                .setPassengerLocationDistribution(UNIFORM)
                .setNumOfCommodities(400)
                .setCommodityLocationDistribution(NORMAL)
                .setNumOfTaxi(100)
                .generate()
                .save("data/appr/appr_1000400100.txt");
    }
}
