package rohChain;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.serverSocket;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.*;
import java.util.Date;
import java.util.Scanner;

//private static final long serialVerisonUID = 2L;
//test node

public class node implements Serializable  {
    //connection vars
    public static String nodeID = "tstNode";
    public static Socket nodePort = null;
    public static ObjectOutputStream out = null;
    public static ObjectInputStream in = null;

    public static ArrayList<block> localChain = new ArrayList<block>();

    public static wallet testNodeWallet;




    public static void main (String[] args) throws IOException, ClassNotFoundException {

        //testNodeWallet = new wallet();
        //connectToMasterNode();
        //closeMasterNodeConnection();
        //System.out.println(fullChain.size());
        receiveBlock();



    }



    //broadcast txn for later use, after block send and receive test is done
    /*
    public void broadcastTXN(txn sentTxn){
        try{
            out.writeObject(sentTxn);

        }catch (Exception e){

        }
    }

     */
    /*
    //runs through UTXOdb and verifies all txns
    public boolean verifyUTXO(){
        return true;
    }

    //mine block method
    public void mineBlock(block block){
        int difficulty = rohChain.difficulty;
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!block.hash.substring(0, difficulty).equals(target)){
            block.nonce++;
            block.hash = block.calculateHash();
            //System.out.println("mining");
        }
        System.out.println("block " + block.hash + " mined");
    }

    //download chain
    /*
    public static void downloadChain() throws IOException, ClassNotFoundException {
        fullChain = (ArrayList) in.readObject();
        System.out.println("fullChain length: " + fullChain.size());

        //String chainHash = StringUtil.applySha256();
    }

     */
    public static void receiveBlock() throws IOException {
        //getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        //establish connection with port 5056
        Socket s = new Socket(ip, 5056);
        out = new ObjectOutputStream(s.getOutputStream());
        in = new ObjectInputStream(s.getInputStream());
        try{
            Scanner scn = new Scanner(System.in);

            //obtaining i/o streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            try{
                String type = dis.readUTF();
                switch (type){
                    case "block":
                        try{
                            in = new ObjectInputStream(s.getInputStream());
                            block newBlock = (block) in.readObject();
                            dos.writeUTF("block received");
                            System.out.println("block prevHash: " + newBlock.prevHash);


                        }catch (IOException e){ e.printStackTrace(); }
                        break;
                    case "txn":
                        try{
                            in = new ObjectInputStream(s.getInputStream());
                            txn newTxn = (txn) in.readObject();
                            dos.writeUTF("txn received");
                            System.out.println(newTxn.signature);
                        }catch (IOException e){ e.printStackTrace(); }
                        break;
                }

            }catch (IOException e){ e.printStackTrace(); }

            //exchange between client server

            //closing resources
            scn.close();
            dis.close();
            dos.close();
            s.close();
            out.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            out.writeObject(nodeID);
        }catch (Exception e){
            System.exit(1);
        }
    }

    //closes master node connection
    public static void closeMasterNodeConnection() throws IOException {
        out.close();
        in.close();
        nodePort.close();
    }

}

