package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class peer {

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> enter username & port # for this peer:");
        String[] setupValues = bufferedReader.readLine().split(" ");
        serverThread serverThread = new serverThread(setupValues[1]);
        serverThread.start();
        new peer().updateListenToPeers(bufferedReader, setupValues[0], serverThread);
    }

    public void updateListenToPeers(BufferedReader bufferedReader, String username, serverThread serverThread) throws Exception{
        System.out.println("> enter (space separated) hostname:port#");
        System.out.println(" peers to receive messages from (s to skip): "); //peers represented as hostname and port number
        String input = bufferedReader.readLine();
        String[] inputValues = input.split(" ");
        //s for skip
        if(!input.equals("s")) for (int i = 0; i < inputValues.length; i++){
            String[] address = inputValues[i].split(":");
            Socket socket = null;
            try{
                socket = new Socket(address[0], Integer.valueOf(address[1]));
                new peerThread(socket).start();
            }catch(Exception e){
                if(socket != null) socket.close();
                else System.out.println("invalid input. skiping to next step");
            }
        }
        communicate(bufferedReader, username, serverThread);

    }

    public void communicate(BufferedReader bufferedReader, String username, serverThread serverThread){
        try{
            System.out.println("> you can now communicate (e to exit, c to change)");
            boolean flag = true;
            while(flag){
                String message = bufferedReader.readLine();
                if(message.equals("e")){
                    flag = false;
                    break;
                }else if (message.equals("c")){
                    updateListenToPeers(bufferedReader, username, serverThread);

                    https://youtu.be/CcLOj3uhb0A?t=669
                }
            }
        }
    }
}
