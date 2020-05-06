package edu.hust.soict.cbls.common.datastructure;

public class Commodity extends Point {
    private double weight;

    public Commodity(double x, double y, double weight) {
        super(x, y);
        this.weight = weight;
    }

    public double getWeight(){
        return this.weight;
    }
}
