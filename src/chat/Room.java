package chat;

/************************ Client Thread *******************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Room implements Runnable {
    // TCP Components
    private Socket clientA;
    private Socket clientB;
    private BufferedReader inA;
    private PrintWriter outA;
    private BufferedReader inB;
    private PrintWriter outB;

    // seperate thread
    private Thread thread;

    // boolean variable to check that client is running or not
    public volatile boolean isfull = false;
    private volatile boolean isRunning = true;

    public Room(Socket firstClient) {
        if(clientA == null && firstClient != null){
            try {
                this.clientA = firstClient;
                inA = new BufferedReader(new InputStreamReader(firstClient.getInputStream()));
                outA = new PrintWriter(firstClient.getOutputStream(), true);
                thread = new Thread(this);
                thread.start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    
    public void setPartner(Socket secondClient) {
        if(clientB == null && secondClient != null){
            try {
                this.clientB = secondClient;
                inB = new BufferedReader(new InputStreamReader(secondClient.getInputStream()));
                outB = new PrintWriter(secondClient.getOutputStream(), true);
                isfull = true;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    
    public void run() {
        try {          
            String resA = "";
            String buff = inA.readLine(); // getting buffer from first client
            while(!buff.isEmpty()){
                resA = resA + buff + "\r\n";
                if(inA.ready())
                    buff = inA.readLine();
            }
            char[] finaltag = new char[50];
            if(inA.ready())
                inA.read(finaltag);
            resA = resA + String.valueOf(finaltag);
            while (clientB == null)
                Thread.sleep(100);
                
            String resB = "";
            buff = inB.readLine(); // getting buffer from second client
            while(!buff.isEmpty()){
                resB = resB + buff + "\r\n";
                if(inB.ready())
                    buff = inB.readLine();
            }
            finaltag = new char[50];
            if(inB.ready())
                inB.read(finaltag);
            resB = resB + String.valueOf(finaltag);
            outA.println(resB);
            outB.println(resA);
            
            while (isRunning) {
                resA = "";
                if(inA.ready()){
                    buff = inA.readLine(); // getting buffer from first client
                    while(!buff.isEmpty()){
                        resA = resA + buff + "\r\n";
                        buff = inA.readLine();
                    }
                    finaltag = new char[1000];
                    if(inA.ready())
                        inA.read(finaltag);
                    resA = resA + String.valueOf(finaltag) + "\r\n";
                }
                resB = "";
                if(inB.ready()){
                    buff = inB.readLine(); // getting buffer from second client
                    while(!buff.isEmpty()){
                        resB = resB + buff + "\r\n";
                        buff = inB.readLine();
                    }
                    finaltag = new char[1000];
                    if(inB.ready())
                        inB.read(finaltag);
                    resB = resB + String.valueOf(finaltag) + "\r\n";
                    if(resB.contains("<synchronizeRequest>"))
                        resB = "";
                }
                if(!resA.isEmpty())
                    outB.println(resA);
                if(!resB.isEmpty())
                    outA.println(resB);
            }

            // close all connections
            outA.close();
            inA.close();
            clientA.close();
            outB.close();
            inB.close();
            clientB.close();
            clientA = null;
            clientB = null;
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            clientA = null;
            clientB = null;
        }
    }
}
