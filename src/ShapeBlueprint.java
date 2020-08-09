/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * This class implements Serializable interface for sending and receiving
 * between client and server. This class is abstract and acts as blueprint
 * to allow shapes like circle, line to extends from it.
 */

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class ShapeBlueprint implements Serializable {

    private static final long serialVersionUID = -7979448098479980628L;

    // specify what shape is this: line, circle, rectangle or text
    private String shapeType;

    public ShapeBlueprint(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    // abstract to let child class implement their own
    // draw shape method
    public abstract Shape drawShape();
}
