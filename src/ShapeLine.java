/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * ShapeLine extends ShapeBlueprint and implenets the function
 * for drawing line on whiteboard.
 */

import java.awt.*;
import java.awt.geom.Line2D;

public class ShapeLine extends ShapeBlueprint {

    private static final long serialVersionUID = -3396510803730135078L;

    // Coordinates used for specifying the starting point of line
    // and ending point of line
    Coordinate coor1;
    Coordinate coor2;
    
    public ShapeLine(Coordinate c1, Coordinate c2) {
        super("line");
        coor1 = c1;
        coor2 = c2;
    }

    @Override
    public Shape drawShape() {
        return  new Line2D.Double(coor1.getX(), coor1.getY(),
                coor2.getX(), coor2.getY());
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
