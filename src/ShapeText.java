/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * ShapeText extends ShapeBlueprint and implements the function
 * of writing text on whiteboard
 */

import java.awt.*;
import java.awt.geom.Line2D;

public class ShapeText extends ShapeBlueprint {

    private static final long serialVersionUID = 8293562278806066474L;
    // specify the position of the text
    private Coordinate coor;
    // record the text written
    private String text;

    public ShapeText(Coordinate c1, String text) {
        super("text");
        coor = c1;
        this.text = text;
    }

    public Shape drawShape() {
        return null;
    }

    public Coordinate getCoor() {
        return coor;
    }

    public void setCoor(Coordinate coor) {
        this.coor = coor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
