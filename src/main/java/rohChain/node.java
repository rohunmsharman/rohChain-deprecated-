package rohChain;
/**
 * node
 *
 * Code is somewhat messy, I was trying to hash out how I wanted the UTXO database to work while
 * simultaneously figuring out the networking. Code needs to be cleaned up and rewritten, the tentative plan
 * is to use a mongoDB accessible by all network participants to store UTXOs.
 *
 * networking:
 * each node binds to a particular socket, runs a new thread for every connection to another node.
 * each thread is set to either accept or send data depending on connection ....
 *
 * Networking is going to be kept simple, there will be a constructor for any node that joins the network,
 * each nodes information will be stored in a local file containing all known nodes, everytime information is to be broadcast
 * a node will loop through and send the info to all known nodes.
 */
//import org.json.*;
import com.mongodb.connection.Server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;

//import org.mongodb.*;
import java.io.*;

public class node {


    public static String nodeName;
    private static Socket inPort;
    private static ServerSocket outPort;
    public static InetAddress address;

    private Socket nodeSocket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;


    public node(String nodeName, ServerSocket outPort, Socket inPort) { //took out InetAdderss from constructor
        this.nodeName = nodeName;
        this.inPort = inPort;
        this.outPort = outPort;
        //this.address = address;

    }

    public static void nodeConection() throws IOException {
        inPort = outPort.accept();

        while(true){
            try{
                //socket object to receive incoming client
                inPort =  outPort.accept();
                System.out.println("a new client is connected : " + inPort);

                //obtaining i/o streams
                DataInputStream dis = new DataInputStream(inPort.getInputStream());
                DataOutputStream dos = new DataOutputStream(inPort.getOutputStream());

                System.out.println("assigning new thread for this client");

                Thread t  = new nodeHandler(inPort, dis, dos);

                t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }



    //get rid of main

        //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());


}




        // migrate genesis txn to broadcast
        /*
        genesisTxn = new txn(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTxn.genSig(coinbase.privateKey); // manual signature
        genesisTxn.txnId = "0"; // set txn id
        genesisTxn.outputs.add(new txnOut(genesisTxn.recipient, genesisTxn.value, genesisTxn.txnId)); // manually add txn output

        UTXOs.put(genesisTxn.outputs.get(0).id, genesisTxn.outputs.get(0)); // stores txn in UTXO list: CHANGE TO MONGODB

        System.out.println("creating & mining genesis block");
        block genesis = new block("0");
        genesis.addTxn(genesisTxn);
        addBlock(genesis);

        //System.out.println("wallet b balance: " + walletB.getBalance());
        rohChain.isChainValid();

         */

        //wallet coinbase = new wallet();
        //masterNodeWallet = new wallet();

        //start connection to node ( i know its not p2p, im getting there)


        //block incomingBlock = (block) in.readObject();
        //System.out.println("incoming block hash: " + incomingBlock.hash + "number of txns: " + incomingBlock.txns.size());




    /*
    public static void testConnection() throws IOException, ClassNotFoundException {
        ServerSocket node = new ServerSocket(port); //listening on port 8888 (local ip 127.0.0.1)
        nodeSocket = node.accept(); // blocking I/O call, won't be called unless a connection is established
        System.out.println("node connected: ");

        out = new ObjectOutputStream(nodeSocket.getOutputStream()); // get output stream of node
        in = new ObjectInputStream(nodeSocket.getInputStream()); //get input stream of node
        out.writeObject("hello! i am node: " + nodeName + " connected to port: " + port);

        String inString = (String) in.readObject();
        System.out.println(inString);

        out.close();
        in.close();
        node.close();
        nodeSocket.close();

    }

     */
    //takes block and converts it into a bson document to be sent to the block db
        /*
    public static BasicDBObject bsonBlock(block block){
        BasicDBObject docBlock = new BasicDBObject();
        //temp to be changed
        docBlock.put("hash:", block.hash);
        docBlock.put("prevHash:", block.prevHash);
        docBlock.put("merkleRoot:", block.merkleRoot);
        //tbd
        for(int i = 0; i < block.txns.size(); i++){
            docBlock.put("txn" + i, block.txns.get(i));
        }

        //collection.insert(docBlock);

        return docBlock;
    }

         */








