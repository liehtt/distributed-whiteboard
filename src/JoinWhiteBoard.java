/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * This main class checks the inputs by the user. It outputs the correct
 * instruction if the user has the wrong argument. It creates a whiteboard
 * for client if the arguments are correct and server accepts the client's
 * access to server.
 */

import java.io.IOException;
import java.net.Socket;

public class JoinWhiteBoard {

    public static void main(String[] args) {
        // if wrong argument, output correct instruction
        if (args.length != 3) {
            System.out.println("Correct instruction: java " +
                    "JoinWhiteBoard <serverIPAddress>" +
                    " <serverPort> <username>");
        } else {
            try {
                // Create a whiteboard for client to use without
                // privilege of server
                Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
                WBClient client = new WBClient(socket, args[2]);
                client.setVisible(true);
            } catch (IOException e) {
                System.out.println("error: Connecting to server failed");
            }
        }
    }
}
