import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.GsonBuilder;

public class rohChain {

    public static ArrayList<block> rohChain = new ArrayList<block>();

    public static int difficulty = 5;

    public static void main(String[] args){
        genesisBlock();
        addMessage();
        System.out.println("chain valid? " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(rohChain);
        System.out.println("current blockchain");
        System.out.println(blockchainJson);
        /*
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



         */
    }

    //adds genesis block and mines it
    public static void genesisBlock(){
        rohChain.add(new block("genesis", "0"));
        rohChain.get(0).mineBlock(difficulty);
    }

    //creates returns block w/ new messages and mines the block
    public static void addMessage(){
        String cont;
        String newMsg;
        Scanner input = new Scanner(System.in);

        //temporary, will change to try/catch error implementation for user
        System.out.println("would you like to add a new message to the rohChain? Y/N");
        cont = input.nextLine();
        while(cont.equals("Y")){
            System.out.print("message: ");
            newMsg = input.nextLine();
            rohChain.add(new block(newMsg, rohChain.get(rohChain.size() - 1).hash));
            rohChain.get(rohChain.size() - 1).mineBlock(difficulty);
            System.out.println("would you like to add another message: (Y/N)");
            cont = input.nextLine();
        }


    }

    public static boolean isChainValid(){
        block currentBlock;
        block prevBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < rohChain.size(); i++){
            currentBlock = rohChain.get(i);
            prevBlock = rohChain.get(i - 1);

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
