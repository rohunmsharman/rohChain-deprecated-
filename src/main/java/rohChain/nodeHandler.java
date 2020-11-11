package rohChain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class nodeHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public nodeHandler(Socket s, DataInputStream dis, DataOutputStream dos){
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

        //run method called when thread is started in node

    //the run method below acts as a server, an equivalent must be written to handle client side
    public void run(){
        String received;
        String toreturn;
        while(true){
            try{
                //write hello to connected node
                dos.writeUTF("hello!");

                //receive the answer from the client
                received = dis.readUTF();

                if(received.equals("exit")){
                    System.out.println("node " + this.s + " is disconnecting");
                    System.out.println("closing connection");
                    this.s.close();
                    System.out.println("connection closed");
                    break;
                }

                if(received.equals("hello!")){
                    toreturn = "nice to meet you";
                    dos.writeUTF(toreturn);
                }
                else{
                    toreturn = "sorry, what did you say?";
                    dos.writeUTF(toreturn);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
