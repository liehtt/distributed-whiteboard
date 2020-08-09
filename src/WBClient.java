/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * WBClient is a GUI for the client. It creates a Whiteboard
 * with userlist. WBClient can draw shapes on whiteboard
 * which will be propagated to other users.
 *
 * WBClient communicates with the Server via ClientThread inner
 * class in WBServer.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WBClient extends JFrame {

    private static final long serialVersionUID = 3722454170431575687L;
    // show list of users on left side
    private JTextArea userList;
    // buttons for drawing shapes
    private JButton lineButton, circleButton, rectangleButton, textButton;

    private DrawingArea drawingArea;
    // shape selected to draw on whiteboard
    private String selectedShapeType;
    // coordinates recorded for drawing shapes
    private ArrayList<Coordinate> coordinates = new ArrayList<>();

    // username of the client
    private String clientUsername;
    private Socket socket;
    // writing and reading object data for server
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public WBClient(Socket clientSocket, String clientUsername)
            throws IOException {
        this.clientUsername = clientUsername;
        this.socket = clientSocket;

        // initialise the writing and reading streams
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        // initialise JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        setSize(800,650);

        // initialise drawing area
        drawingArea = new DrawingArea();
        this.add(drawingArea, BorderLayout.CENTER);
        drawingArea.addMouseListener(new MouseEventListener());

        // initialise shape buttons
        Panel shapeTools = new Panel(new GridLayout(1, 4));
        this.add(shapeTools, BorderLayout.NORTH);
        lineButton = new JButton("Draw line");
        lineButton.addActionListener(new ButtonEventListener());
        shapeTools.add(lineButton);
        circleButton = new JButton("Draw circle");
        circleButton.addActionListener(new ButtonEventListener());
        shapeTools.add(circleButton);
        rectangleButton = new JButton("Draw rectangle");
        rectangleButton.addActionListener(new ButtonEventListener());
        shapeTools.add(rectangleButton);
        textButton = new JButton("Write text");
        textButton.addActionListener(new ButtonEventListener());
        shapeTools.add(textButton);

        // initialise user list
        Panel showUsers = new Panel(new GridLayout(1, 1));
        this.add(showUsers, BorderLayout.WEST);
        userList = new JTextArea();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(userList);
        showUsers.add(scrollPane);

        // default selected shape is line
        selectedShapeType = "line";
        setLocationRelativeTo(null);

        // sends request to server for joining
        out.writeObject("join");
        out.writeObject(clientUsername);
        new Client().start();

    }

    // Client extends Thread that runs until server broke
    // or kicked
    private class Client extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    // read instruction from server
                    String instruction = in.readObject().toString();
                    switch (instruction) {
                        // if instruction is "join"
                        case "join":
                            String reply = in.readObject().toString();
                            // Exit the client if server replies no
                            if (reply.equals("no")) {
                                System.out.println("error: server " +
                                        "is online but access is denied");
                                System.exit(0);
                            // Set the title of JFrame
                            } else {
                                setTitle("Client WhiteBoard of "
                                        + clientUsername);
                            }
                            break;
                        // if instruction is "userlist"
                        case "userlist":
                            String list = in.readObject().toString();
                            // set the userlist here with list given
                            // from server
                            userList.setText(list);
                            break;
                        // if instruction is "new"
                        case "new":
                            // reset the drawing area
                            drawingArea.newDrawingArea();
                            break;
                        // if instruction is "kick"
                        case "kick":
                            // Exit the client system and notify the client
                            JOptionPane.showMessageDialog(null, "you " +
                                    "are kicked by the server");
                            System.exit(0);
                        // else, which the instruction is shapes
                        default:
                            // draw the shape on whiteboard
                            ShapeBlueprint shape = (ShapeBlueprint)
                                    in.readObject();
                            drawingArea.drawShape(shape);
                            break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "server is not available now.");
                System.exit(0);
            }
        }
    }

    // Action listeners for shape buttons to determine
    // the shape that is going to be drawn
    private class ButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            coordinates.clear();
            if (e.getSource() == lineButton) {
                selectedShapeType = "line";
            } else if (e.getSource() == circleButton) {
                selectedShapeType = "circle";
            } else if (e.getSource() == rectangleButton) {
                selectedShapeType = "rectangle";
            }  else if (e.getSource() == textButton) {
                selectedShapeType = "text";
            }
        }
    }

    // Action listener for mouse to determine
    // the coordinates on drawing area when the mouse
    // is clicked

    // If shape is selected, draw the corresponding
    // shape on whiteboard when the number of
    // mouseclicked is satisfied
    private class MouseEventListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {

            super.mouseClicked(e);
            coordinates.add(new Coordinate(e.getX(), e.getY()));

            ShapeBlueprint shape = null;

            switch (selectedShapeType) {
                case "line":
                    if (coordinates.size() == 2) {
                        shape = new ShapeLine(coordinates.get(0),
                                coordinates.get(1));
                        coordinates.clear();
                    }
                    break;
                case "circle":
                    if (coordinates.size() == 2) {
                        shape = new ShapeCircle(coordinates.get(0),
                                coordinates.get(1));
                        coordinates.clear();
                    }
                    break;
                case "rectangle":
                    if (coordinates.size() == 2) {
                        shape = new ShapeRectangle(coordinates.get(0),
                                coordinates.get(1));
                        coordinates.clear();
                    }
                    break;
                case "text":
                    if (coordinates.size() == 1) {
                        String text = JOptionPane.
                                showInputDialog("Your Text:");
                        if (!text.isEmpty()) {
                            shape = new ShapeText(coordinates.get(0), text);
                            coordinates.clear();
                        }
                    }
                    break;
            }

            if (shape != null) {
                try {
                    out.writeObject(shape.getShapeType());
                    out.writeObject(shape);
                } catch (IOException er) {
                    JOptionPane.showMessageDialog(null, "error: " +
                            "failed to connect to server");
                }
            }
        }
    }
}
