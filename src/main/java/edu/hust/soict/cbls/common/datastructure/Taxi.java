package edu.hust.soict.cbls.common.datastructure;

public class Taxi implements Entity{
    private Point station;
    private double cap;

    public Taxi(Point station, double cap) {
        this.station = station;
        this.cap = cap;
    }

    public double getCap(){
        return this.cap;
    }

    @Override
    public Point startPoint() {
        return station;
    }

    @Override
    public Point stopPoint() {
        return station;
    }
}
