package rohChain;

import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.serverSocket;
import java.util.ArrayList;
import java.io.*;

//private static final long serialVerisonUID = 2L;

public class node implements Serializable {
    public static String username = "testNode1";
    public String usernameHash;
    public static Socket nodePort = null;
    public static ObjectOutputStream out = null;
    public static ObjectInputStream in = null;
    public static ArrayList fullChain;

    //constructor for new node
    public node(){
    }

    //runs through UTXOdb and verifies all txns
    public boolean verifyUTXO(){
        return true;
    }

    public static void main (String[] args) throws IOException, ClassNotFoundException {
        connectToMasterNode();
        downloadChain();
        //System.out.println(fullChain.size());

    }

    //download chain
    public static void downloadChain() throws IOException, ClassNotFoundException {
        fullChain = (ArrayList) in.readObject();
        System.out.println("fullChain length: " + fullChain.size());

        //String chainHash = StringUtil.applySha256();
    }

    //compare updated chain and last chain saved in node
    public void updateChain(){

    }

    //connects to master node
    public static void connectToMasterNode() throws IOException {

        try{
            nodePort = new Socket("localhost", 8888); // listens on this port, will update to be actual masternode
            out = new ObjectOutputStream(nodePort.getOutputStream());
            in = new ObjectInputStream(nodePort.getInputStream());
            System.out.println("connected to master node");
            out.writeObject(username);
        }catch (Exception e){
            System.exit(1);
        }
    }

    //closes master node connection
    public void closeMasterNodeConnection() throws IOException {
        out.close();
        in.close();
        nodePort.close();
    }

}
