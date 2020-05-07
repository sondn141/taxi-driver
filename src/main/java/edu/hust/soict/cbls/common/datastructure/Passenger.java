package edu.hust.soict.cbls.common.datastructure;

public class Passenger implements Entity{
    private Point getIn;
    private Point getOff;

    public Passenger(Point getIn, Point getOff) {
        this.getIn = getIn;
        this.getOff = getOff;
    }

    public Point getGetIn() {
        return getIn;
    }

    public Point getGetOff() {
        return getOff;
    }

    @Override
    public Point startPoint() {
        return getIn;
    }

    @Override
    public Point stopPoint() {
        return getOff;
    }
}
