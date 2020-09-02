package rohChain;

//import org.json.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
//import java.net.serverSocket;
import java.security.spec.ECGenParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mongodb.*;
import org.bouncycastle.util.IPAddress;
//import org.mongodb.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.concurrent.Executors;

import static rohChain.rohChain.*;

public class masterNode {
    //public static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    //public static DB database = mongoClient.getDB("roCoinTest");
    //public static DBCollection collection = database.getCollection("blocks");

    //public static MongoClient mongoCLient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    //public static DB nodesListTest = mongoClient.getDB("nodesListTest");
    //public static DBCollection ports = nodesListTest.getCollection("ports");

    //public static Set<String> names = new HashSet<>(); // all node names, check for duplicates upon registraction
    //public static HashMap nodes = new HashMap();
    //list of known nodes has to be stored in a database, setup basic networking, then create separate classes
    //ip address and socket number
    public static HashMap<InetAddress, Integer> knownNodes = new HashMap<InetAddress, Integer>(); //SWTICH TO MONGODB SERVER when pacakged for serparate network test

    public static String nodeName;
    public static int port;
    public static InetAddress address;

    public static PublicKey nodePubKey;
    private static PrivateKey nodePrivKey;
    public byte[] nodeConfirmation; // node confirmation signature

    //public static Socket node = null;
    public static ObjectOutputStream out = null;
    public static ObjectInputStream in = null;

    public static wallet walletA;
    public static txn genesisTxn;
    public static wallet coinbase;


    public masterNode(String nodeName, int port, InetAddress address){
        this.nodeName = nodeName;
        this.port = port;
        this.address = address;

    }

    //get rid of main
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // migrate genesis txn to broadcast
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



        //wallet coinbase = new wallet();
        //masterNodeWallet = new wallet();

        //start connection to node ( i know its not p2p, im getting there)
        /*
            ServerSocket masterNode = new ServerSocket(8888); //listening on port 8888 (local ip 127.0.0.1)
            node = masterNode.accept(); // blocking I/O call, won't be called unless a connection is established
            System.out.println("node connected: ");

            out = new ObjectOutputStream(node.getOutputStream()); // get output stream of node
            in = new ObjectInputStream(node.getInputStream()); //get input stream of node

         */

        /*
            //get node name
            String nodeName = (String) in.readObject();
            System.out.println("node name: " + nodeName);

         */

            //send full rchain to node
            //out.writeObject(rohChain.rChain);
            //System.out.println()

        //block incomingBlock = (block) in.readObject();
        //System.out.println("incoming block hash: " + incomingBlock.hash + "number of txns: " + incomingBlock.txns.size());

        /*
        out.close();
        in.close();
        node.close();
        masterNode.close();

         */

    }

    public void generateKeyPair(){

        try{
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // initiate keyGen and generate keyPair
            KeyPair keyPair = keyGen.generateKeyPair();
            // set pub/priv keys
            nodePrivKey = keyPair.getPrivate();
            nodePubKey = keyPair.getPublic();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

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

    //takes node private key
    public void genConfirmation(PrivateKey privateKey, txn TXN){
        String txnInfo = TXN.txnId + StringUtil.getStringFromKey(nodePubKey);
        nodeConfirmation = StringUtil.applyECDSASig(privateKey, txnInfo);
    }

    //takes block and converts it into a bson document to be sent to the block db
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
}



    //activates node as server: depracated test
    /*
    public static void activateNode() throws IOException {
        System.out.println("node is live...");
        var pool = Executors.newFixedThreadPool(500);

        ServerSocket ss = new ServerSocket(5056);

        //loop for client requests
        while(true){
            Socket s = null;

            try{
                //socket object to receive incoming client requests
                s = ss.accept();
                System.out.println("a new client connected");

                //obtaining i/o streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("assigning thread for new client: ");

                //pull from node class
                Thread t = new nodeHandler(s, dis, dos);

                //invoke start
                t.start();
            }catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

     */

/*
class nodeHandler extends Thread{
    block sendBlock;
    ObjectOutputStream out;
    ObjectInputStream in;
    DataInputStream dis;
    DataOutputStream dos;
    Socket s;

    public nodeHandler(Socket s, DataInputStream dis, DataOutputStream dos){
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }


    @Override
    public void run(){
        String received;
        String toreturn;
        //block outBlock;  // temporary, will change after i figure out broadcasting
        txn outTxn;

        while(true){
            Scanner in1 = new Scanner(System.in);
            System.out.println("broadcast block or txn");
            String choice = in1.nextLine();
            switch(choice){
                /*
                case "block":
                    try{
                        out = new ObjectOutputStream(s.getOutputStream());
                        //in = new ObjectInputStream(s.getInputStream());
                        dos.writeUTF("block");
                        out.writeObject(outBlock);
                        System.out.println("block sent");

                        //ask user what they want
                        //dos.writeUTF("wathcha want?");

                        //receive answer from node
                        //received = dis.readUTF();

                    } catch (IOException e) { e.printStackTrace(); }
                    break;



                case "txn":
                    try{
                        out = new ObjectOutputStream(s.getOutputStream());
                        outTxn = masterNode.genesisTxn;
                        out.writeObject(outTxn);

                    }catch (IOException e){ e.printStackTrace(); }
                    break;

                default:
                    System.out.println("invalid input");
                    break;

            }
            try{
                this.dis.close();
                this.dos.close();
                in1.close();
                in.close();
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
*/

