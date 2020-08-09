/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * ShapeCircle extends ShapeBlueprint and implements the method
 * for drawing circle on whiteboard.
 */

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ShapeCircle extends ShapeBlueprint {


    private static final long serialVersionUID = -3639618165561940845L;

    // Two coordinates used for drawing circle
    private Coordinate coor1;
    private Coordinate coor2;

    public ShapeCircle(Coordinate c1 , Coordinate c2) {
        super("circle");

        // choose a coordinate as starting position of shape circle
        coor1 = new Coordinate(Math.min(c1.getX(), c2.getX()),
                Math.min(c1.getY(), c2.getY()));

        // calculate the diameter
        int diameter  = calcDiameter(c1, c2);

        // set the width and height of the circle
        coor2 = new Coordinate((Math.min(c1.getX(), c2.getX()) + diameter),
                Math.min(c1.getY(), c2.getY()) + diameter);
    }

    private int calcDiameter(Coordinate c1, Coordinate c2) {
        int diff1 = Math.abs(c2.getX() - c1.getX());
        int diff2 = Math.abs(c2.getY() - c1.getY());
        return Math.max(diff1, diff2);
    }

    @Override
    public Shape drawShape() {
    
        return new Ellipse2D.Double(coor1.getX(), coor1.getY(),
                coor2.getX()-coor1.getX(), coor2.getY() - coor1.getY());
    }

    public Coordinate getCoor1() {
        return coor1;
    }

    public void setCoor1(Coordinate coor1) {
        this.coor1 = coor1;
    }

    public Coordinate getCoor2() {
        return coor2;
    }

    public void setCoor2(Coordinate coor2) {
        this.coor2 = coor2;
    }
}
