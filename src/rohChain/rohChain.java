package rohChain; //written by Rohun Sharman (started 7/9/2020)

import java.util.ArrayList;
import com.google.gson.GsonBuilder;


public class rohChain {

    public static ArrayList<block> rChain = new ArrayList<block>();

    public static int difficulty = 5;

    public static void main(String[] args){
        genesisBlock();
        addMessage.addMessage();
        System.out.println("chain valid? " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(rChain);
        System.out.println("current blockchain");
        System.out.println(blockchainJson);

    }

    //adds genesis rohChain.rohChain.block and mines it
    public static void genesisBlock(){
        rChain.add(new block("genesis", "0"));
        rChain.get(0).mineBlock(difficulty);
    }



    public static boolean isChainValid(){
        block currentBlock;
        block prevBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < rChain.size(); i++){
            currentBlock = rChain.get(i);
            prevBlock = rChain.get(i - 1);

            //checks if current rohChain.rohChain.block hash equals registered rohChain.rohChain.block hash
            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("current hashes not equal");
                return false;
            }
            //compares previous hash and and registered previous hash (prevHash of current rohChain.rohChain.block)
            if(!prevBlock.hash.equals(currentBlock.prevHash)){
                System.out.println("previous hashes not equal");
                return false;
            }
            //checks if hash is solved
            if(!currentBlock.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.println(currentBlock + " hasn't been hashed yet");
                return false;
            }
        }
        return true;
    }




}