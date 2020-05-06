package edu.hust.soict.cbls.common.datastructure;

public class Commodity implements Entity{
    private Point pickup;
    private Point deliver;
    private double weight;

    public Commodity(Point pickup, Point deliver, double weight) {
        this.pickup = pickup;
        this.deliver = deliver;
        this.weight = weight;
    }

    public double getWeight(){
        return this.weight;
    }

    public Point getPickup() {
        return pickup;
    }

    public Point getDeliver() {
        return deliver;
    }

    @Override
    public Point startPoint() {
        return pickup;
    }

    @Override
    public Point stopPoint() {
        return deliver;
    }
}
