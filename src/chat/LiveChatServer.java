package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class LiveChatServer {

    // Connection state info
    private static Vector<Room> rooms = new Vector<>();

    // TCP Components
    private ServerSocket serverSocket;

    // Main Constructor
    public LiveChatServer() {
        startServer();// start the server
    }

    public void startServer() {
        String port = "5001";

        try {
            // in constractor we are passing port no, back log and bind address whick will be local
            // host
            // port no - the specified port, or 0 to use any free port.
            // backlog - the maximum length of the queue. use default if it is equal or less than 0
            // bindAddr - the local InetAddress the server will bind to

            int portNo = Integer.valueOf(port);
            serverSocket = new ServerSocket(portNo, 0, InetAddress.getByName("127.0.0.1"));
            System.out.println(serverSocket);

            System.out.println(serverSocket.getInetAddress().getHostName() + ":"
                    + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                boolean found = false;
                for(int i = 0; i < rooms.size() && !found; i++){
                    if(!rooms.get(i).isfull){
                        rooms.get(i).setPartner(socket);
                        found = true;
                    }
                }
                if(!found)
                    rooms.add(new Room(socket));
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e);
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Number Format Exception:" + e);
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        new LiveChatServer();
    }

}