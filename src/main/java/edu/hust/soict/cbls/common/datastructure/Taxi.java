package edu.hust.soict.cbls.common.datastructure;

public class Taxi implements Entity{
    private static Point station;
    private double cap;

    public Taxi(Point stt, double cap) {
        station = stt;
        this.cap = cap;
    }

    public double getCap(){
        return this.cap;
    }

    public static Point station(){
        return station;
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
