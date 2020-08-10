# Distributed Whiteboard

Distributed Whiteboard is a shared canvas allowing users to draw simultaneously on a single whiteboard. It supports simple drawing features like drawing a line, rectangle, circle or writing a text. User that controls the whiteboard can also clear the canvas or kick a user out of the server.

The program works by utilizing TCP socket of Java as communication protocol between the server (the user who creates the whiteboard) and the clients (users who join the whiteboard), allowing real-time updates on whiteboards. Thread-per-connection architecture is used so the server can serve mutiple clients. 

## Prerequisite 

To run the java program, you need to download JDK from Oracle or OpenJDK. 

## Installation

Download CreateWhiteBoard.jar and JoinWhiteBoard.jar. Execute the jars with arguments from the terminal. 

## Usage

To start a server (or create a whiteboard):





## License

The project uses [MIT License](<LICENSE>).


