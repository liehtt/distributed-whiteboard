/**
 * Name: LiehTsorng Then
 * SurName: Then
 * StudentID: 900842
 *
 * This main class checks the inputs by the user. It outputs the correct 
 * instruction if the user has the wrong argument. It creates a whiteboard
 * server if the arguments are correct.
*/
public class CreateWhiteBoard {
·
    public static void main(String[] args) {
		
		// if the command types is not in the correct format, output 
		// the correct format
        if(args.length != 3) {
            System.out.println("Correct instructions: java CreateWhiteBoard" +
                    " <serverIPAddress> <serverPort> username");
        } else {
            // if its in the correct format, pass in the port number and
            // and the server username to create server
            WBServer server = new WBServer(Integer.parseInt(args[1]), args[2]);
            server.setVisible(true);
        }
    }
}
