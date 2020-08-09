/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * WBServer is a GUI for the server. It creates a Whiteboard
 * with userlist. WBServer can draw shapes on whiteboard
 * which will be propagated to other users and also
 * control the server. It has other functions like
 * reset the drawing area, kick users or close
 * the server.
 *
 * WBServer communicates with multiple clients via ClientThread inner
 * class.
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WBServer extends JFrame {

    private static final long serialVersionUID = -5698965795968512940L;

    // show list of users on left side
    private JTextArea userList;
    // buttons for drawing shapes
    private JButton lineButton, circleButton, rectangleButton, textButton;
    // buttons for managing server and whiteboard
    private JButton newButton, closeButton, kickButton;


    private DrawingArea drawingArea;
    // shape selected to draw on whiteboard
    private String selectedShapeType;
    // coordinates recorded for drawing shapes
    private ArrayList<Coordinate> coordinates = new ArrayList<>();

    // track the number of clients
    private int userId = 1;
    // username of the server
    private String serverUsername;
    // port of the server
    private int port;
    // track the status of server
    private boolean serverOn = true;

    // store the clients connected
    private ArrayList<ClientThread> clients = new ArrayList<>();

    public WBServer(int port, String serverUsername) {
        this.serverUsername = serverUsername;
        this.port = port;

        // initialise the server frame
        setTitle("Server WhiteBoard of " + serverUsername);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        setSize(800,700);

        // initialise the drawing area
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

        // initialise panel for manager actions
        Panel managerTools = new Panel(new GridLayout(1, 3));
        this.add(managerTools, BorderLayout.SOUTH);
        newButton = new JButton("New Whiteboard");
        newButton.addActionListener(new ButtonEventListener());
        managerTools.add(newButton);
        kickButton = new JButton("Kick User");
        kickButton.addActionListener(new ButtonEventListener());
        managerTools.add(kickButton);
        closeButton = new JButton("Close Server");
        closeButton.addActionListener(new ButtonEventListener());
        managerTools.add(closeButton);

        // initialise user list
        Panel showUsers = new Panel(new GridLayout(1, 1));
        this.add(showUsers, BorderLayout.WEST);
        userList = new JTextArea();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(userList);
        showUsers.add(scrollPane);

        // default selected shape is line
        selectedShapeType = "line";

        // update user list and set drawing area
        // to center
        setLocationRelativeTo(null);
        updateUserList();
        new ServerThread().start();
    }

    // Create the server and wait for clients
    // to connect to server. Create a client
    // thread when client is connected
    private class ServerThread extends Thread {
        @Override
        public void run() {

            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (serverOn) {
                    Socket clientSocket = serverSocket.accept();
                    ClientThread client = new ClientThread(clientSocket);
                    client.start();
                }
            } catch (IOException e) {
                System.out.println("error: server starting failed");
                System.exit(0);
            }

        }
    }


    // Action listeners for shape buttons and manager buttons
    // to determine the shape that is going to be drawn
    private class ButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            coordinates.clear();
            // reset the drawing area when NEW button is pressed
            if (e.getSource() == newButton) {
                newDrawingArea();
            // close the server when CLOSE button is pressed
            } else if (e.getSource() == closeButton) {
                serverOn = false;
                System.exit(0);
            // kick the user when KICK is pressed
            } else if (e.getSource() == kickButton) {
                String clientName = JOptionPane.showInputDialog("Type " +
                        "the name of user to kick the user out");
                if (clientName != null) {
                    ClientThread client = searchClient(clientName);
                    if (client != null) {
                        try {
                            client.out.writeObject("kick");
                        } catch (IOException e1) {
                            System.out.println("error: kicking " +
                                    "the client failed");
                        }

                        removeClient(client);
                    } else {
                        JOptionPane.showMessageDialog(null, "No user found");
                    }
                }
            // Else draw the shape depending on button pressed
            } else if (e.getSource() == lineButton) {
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

    // Search the client when username is typed to kick
    // the user
    private ClientThread searchClient(String clientName) {
        for (ClientThread client : clients) {
            if (client.clientUsername.equals(clientName)) {
                return client;
            }
        }
        return null;
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
                addShape(shape);
            }
        }
    }

    // add shape drawn and propagated to other users
    public synchronized void addShape(ShapeBlueprint shape) {
        drawingArea.drawShape(shape);
        for (ClientThread client : clients) {
            try {
                client.out.writeObject(shape.getShapeType());
                client.out.writeObject(shape);
            } catch (IOException e) {
                System.out.println("error: writing failed " +
                        "when sending shape");
            }
        }
    }

    // reset the drawing area
    public synchronized void newDrawingArea() {
        drawingArea.newDrawingArea();

        for (ClientThread client : clients) {
            try {
                client.out.writeObject("new");
            } catch (IOException e) {
                System.out.println("error: writing failed " +
                        "when sending clear instructions");
            }
        }
    }

    // add the client and update user list
    public synchronized void addClient(ClientThread client) {
        clients.add(client);
        updateUserList();
    }

    // remove the client and update user list
    public synchronized void removeClient(ClientThread client) {
        clients.remove(client);
        updateUserList();
    }

    // update the user list when there's changes to client list
    private void updateUserList() {
        StringBuilder list = new StringBuilder(serverUsername + "\n");
        for (ClientThread client : clients) {
            list.append(client.clientUsername).append("\n");
        }
        userList.setText(list.toString());
        for (ClientThread client : clients) {
            try {
                client.out.writeObject("userlist");
                client.out.writeObject(list.toString());
            } catch (IOException e) {
                System.out.println("error: writing failed " +
                        "when sending userlist");
            }
        }
    }

    // update the whiteboard of client thats newly joined
    public synchronized void updateNewClientDA(ClientThread client) {
        for (ShapeBlueprint shape : drawingArea.getShapes()) {
            try {
                client.out.writeObject(shape.getShapeType());
                client.out.writeObject(shape);
            } catch (IOException e) {
                System.out.println("error: updating clients' " +
                        "drawing area failed");
            }
        }
    }

    // Created when new client is joined. Used for communication
    // between server and client. Server keeps track
    // of each ClientThread object
    private class ClientThread extends Thread {
        Socket socket;
        int clientId;
        String clientUsername;

        ObjectInputStream in;
        ObjectOutputStream out;


        public ClientThread(Socket socket) throws IOException {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            clientId = userId++;
        }

        @Override
        public void run() {
            try {
                // if server is still online
                while (serverOn) {
                    // read instruction if there is
                    String instruction = in.readObject().toString();
                    // if instruction is "join"
                    if (instruction.equals("join")) {
                        // Either accept or reject client access
                        clientUsername = in.readObject().toString()
                                + " ["+clientId+"]";
                        int confirmationFromServer = JOptionPane.
                                showConfirmDialog(null,
                                "Do you allow " + clientUsername
                                        + " to join the server?",
                                "A new client approaching...",
                                JOptionPane.OK_CANCEL_OPTION);
                        // if accept, add the client, sends reply back
                        // to client, and update client's whiteboard
                        if (confirmationFromServer == JOptionPane.OK_OPTION) {
                            addClient(this);
                            out.writeObject("join");
                            out.writeObject(clientUsername);
                            updateNewClientDA(this);
                        // if reject, sends reply back to client
                        } else {
                            out.writeObject("join");
                            out.writeObject("no");
                        }
                    // else, add shape drawn by client and propagrate
                    // shape data to other users
                    } else {
                        ShapeBlueprint shape = (ShapeBlueprint)
                                in.readObject();
                        addShape(shape);
                    }

                }
            // if server is off, remove the client
            } catch (Exception  e) {
                removeClient(this);
            }
        }
    }
}
