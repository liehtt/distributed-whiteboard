/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * This class is a graphical component. It represents the drawing area
 * where shapes and text are drawn. The stroke is black in color while
 * the background is white.
*/

import java.awt.*;
import java.util.ArrayList;

public class DrawingArea extends Panel {

    // track number of shape objects in the drawing area
    private ArrayList<ShapeBlueprint> shapes;

    public DrawingArea() {
        shapes = new ArrayList<>();
    }

    // draw the shape on drawing area by adding shape data
    // to arraylist
    public void drawShape(ShapeBlueprint shape) {
        shapes.add(shape);
        repaint();
    }

    // make a new drawing area by deleting all shape data
    // from array list
    public void newDrawingArea() {
        shapes.clear();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        graphics2D.setColor(Color.BLACK);

        for (int i = 0; i < shapes.size(); i ++) {
            if(shapes.get(i).getShapeType().equals("text")) {
                ShapeText shapeText = (ShapeText) shapes.get(i);
                graphics2D.drawString(shapeText.getText(),
                        shapeText.getCoor().getX(),
                        shapeText.getCoor().getY());
            } else {
                graphics2D.draw(shapes.get(i).drawShape());

            }
        }
    }

    public ArrayList<ShapeBlueprint> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<ShapeBlueprint> shapes) {
        this.shapes = shapes;
    }
}
