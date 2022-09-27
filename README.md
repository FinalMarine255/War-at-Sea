# War at Sea
A Batleship game written in Java. This was a SAIT project where the project requirements were that the project must make use of networking.

The two most difficult features of this project were the networking and the rendering. The networking made debuging some issues extremely challenging. The rendering system was also a challenge as it a custom render had to be written ontop of the Java AWT.Canvas class in order to get the render speed required for having animated sprites. Early in the design I experimented with using the Java Native Interface to implement a DirectX render (something which I had some experience with after [Blarg](https://github.com/JoryVardas/Blarg)), however this was quickly abandoned due to the complexity and instead the AWT.Canvas renderer was created.

There are two components to this project: the server and the client.

## Server

The server code is located in **src/ca/sait/cprg311/WarAtSea/Server/** with the **ServerDriver.java** class being the entry point for the server.

The server will bind to port 5555 to receive messages from clients. It will wait for clients to connect and when two clients are available it will match them and start a thread for managing the game.

## Client

The server code is located in **src/ca/sait/cprg311/WarAtSea/Client/** with the **ClientDriver.java** class being the entry point for the server.

The client will ask for the server it should connect to and the name the user would like to use. It will then connect to the server and wait for a match.

## Know issues

There are some issues with the networking resulting in the two clients and the server entering different states, resulting in difficult to debug errors.

- both clients believing it is their turn to make a move and the server rejecting both moves.
- The match ending after a single move in some cases.
- The server ending matches as soon as they start after one match has ended.