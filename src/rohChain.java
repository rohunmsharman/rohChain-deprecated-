import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class rohChain {

    public static ArrayList<block> blockchain = new ArrayList<block>();

    public static int difficulty = 5;

    public static void main(String[] args){

        blockchain.add(new block("genesis", "0"));
        System.out.println("mining block 1");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new block("adgh", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("mining block 2");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new block("afjl", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("mining block 3");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\n chain valid? " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("current blockchain");
        System.out.println(blockchainJson);
    }

    public static boolean isChainValid(){
        block currentBlock;
        block prevBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            prevBlock = blockchain.get(i - 1);

            //checks if current block hash equals registered block hash
            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("current hashes not equal");
                return false;
            }
            //compares previous hash and and registered previous hash (prevHash of current block)
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
