package edu.hust.soict.cbls.common.datastructure;

public class Taxi extends Point {
    private double cap;

    public Taxi(double x, double y, double cap) {
        super(x, y);
        this.cap = cap;
    }

    public double getCap(){
        return this.cap;
    }
}
