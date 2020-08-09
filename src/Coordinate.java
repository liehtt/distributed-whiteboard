/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * This class represents the coordinates of the drawing area.
 * It is serializable to be used for sending and receiving
 * between client and server.
*/

import java.io.Serializable;

public class Coordinate implements Serializable {

    private static final long serialVersionUID = -5438707458680598876L;
    
    // Coordinate x and y
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
