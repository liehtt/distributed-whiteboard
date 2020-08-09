/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * ShapeRectangle extends ShapeBlueprint and implements the functionality
 * of drawing rectangle on whiteboard
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ShapeRectangle extends ShapeBlueprint {

    private static final long serialVersionUID = -3165830406147775510L;

    // Coordinates for specifying the starting location and the size
    Coordinate coor1;
    Coordinate coor2;

    public ShapeRectangle(Coordinate c1, Coordinate c2) {
        super("rectangle");
        // Get the starting location for rectangle
        coor1 = new Coordinate(Math.min(c1.getX(), c2.getX()),
                Math.min(c1.getY(), c2.getY()));
        // Get the width and height of rectangle
        coor2 = new Coordinate(Math.max(c1.getX(), c2.getX()),
                Math.max(c1.getY(), c2.getY()));
    }

    @Override
    public Shape drawShape() {
        return new Rectangle2D.Double(coor1.getX(), coor1.getY(),
                coor2.getX() - coor1.getX(),
                coor2.getY() - coor1.getY());
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
