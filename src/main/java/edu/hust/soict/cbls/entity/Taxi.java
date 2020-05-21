package edu.hust.soict.cbls.entity;

public class Taxi implements Entity{
    private static Point station;
    private double cap;

    public Taxi(double cap) {
        this.cap = cap;
    }

    public double getCap(){
        return this.cap;
    }

    public static void setStation(Point stt){
        station = stt;
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
