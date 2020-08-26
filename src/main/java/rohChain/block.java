package rohChain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

public class block implements Serializable {

    private static final long serialVerisonUID = 2L;

    public String hash;
    public String prevHash;
    public String merkleRoot;
    public ArrayList<txn> txns = new ArrayList<txn>();
    //private String data;  // literally a small message, have yet to implement coin
    private long timeStamp;
    private int nonce = 0;

    //to be added with proof of work
    //private int nonce

    //rohChain.rohChain.block constructor
    public block(String prevHash){
        //this.data = data;
        this.prevHash = prevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        String calculatedHash = StringUtil.applySha256(prevHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
        return calculatedHash;
    }


    //will be moved to a separate class, multithreaded mining will be implemented.
    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
            //System.out.println("mining");
        }
        System.out.println("block " + hash + " mined");
    }

    public boolean addTxn(txn Txn){
        //process txn, determine txn validity, exception for genesis block
        if(Txn == null) return false;
        if(prevHash != "0"){
            if(Txn.processTxn() != true){
                System.out.println("txn failed to process. it has been discarded");
                return false;
            }
        }
        txns.add(Txn);
        System.out.println("txn successfull added to block");
        return true;

    }

    public void writeObject(ObjectOutputStream oos)throws IOException{
        System.out.println("custom serialization logic invoked ");
        oos.defaultWriteObject(); //calls default serializaiton logic
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        System.out.println("custom deserialization logic invoked");
        ois.defaultReadObject();
    }

}
