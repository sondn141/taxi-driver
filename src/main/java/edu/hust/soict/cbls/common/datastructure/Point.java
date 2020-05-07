package edu.hust.soict.cbls.common.datastructure;

public class Point {

    private int index;
    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double distance(Point other){
        return Math.sqrt(Math.pow(this.x - other.getX(), 2) + Math.pow(this.y - other.getY(), 2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
