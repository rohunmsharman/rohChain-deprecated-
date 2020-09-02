package rohChain; //created by rohun sharman (with help from the internet)

import java.io.Serializable;
import java.util.ArrayList;
import java.security.Security;
import java.util.HashMap;


// MIGRATE TO MASTER NDOE
public class rohChain implements Serializable {

    //will move to mongodb with verificatio method
    public static ArrayList<block> rChain = new ArrayList<block>();

    //UTXO stored as <txnID, txn amount> , txn contains pubKey of sender and recipient, thus a check can be made during processing.
    public static HashMap<String, txnOut> UTXOs = new HashMap<String, txnOut>();

    public static int difficulty = 3;
    public static float minTxn = 0.1f;
    /*
    public static wallet walletA;
    public static wallet walletB;
    public static txn genesisTxn;

     */


    public static void main(String[] args){
        //bouncy castle security provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        /*
        //create test wallets
        walletA = new wallet();
        walletB = new wallet();
        wallet coinbase = new wallet();

        //txn test:
        //genesis txn, 100 rohCoin to walletA:
        genesisTxn = new txn(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTxn.genSig(coinbase.privateKey); // manual signature
        genesisTxn.txnId = "0"; // set txn id
        genesisTxn.outputs.add(new txnOut(genesisTxn.recipient, genesisTxn.value, genesisTxn.txnId)); // manually add txn output
        UTXOs.put(genesisTxn.outputs.get(0).id, genesisTxn.outputs.get(0)); // stores txn in UTXO list

        System.out.println("creating & mining genesis block");
        block genesis = new block("0");
        genesis.addTxn(genesisTxn);
        addBlock(genesis);
        System.out.println("wallet b balance: " + walletB.getBalance());
        isChainValid();

        //testing
        block block1 = new block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");

        //ISSUE with send funds method
        block1.addTxn(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

         */


        /*
        block block2 = new block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTxn(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

         */
        /*
        block block2 = new block(block1.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block2.addTxn(walletB.sendFunds( walletA.publicKey, 20f));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

         */

        isChainValid();

        //priv/pub, 1st wallet test
        /*
        // test pub/priv keys
        System.out.println("priv and pub keys:");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

        //test txn walletA to walletB
        txn txn1 = new txn(walletA.publicKey, walletB.publicKey, 5, null);
        txn1.genSig(walletA.privateKey);

        //verify signing works from pub key
        System.out.println("is sig valid? ");
        System.out.println(txn1.verifySignature());

         */

        //initial chain test: add message and mine
        /*genesisBlock();
        addMessage.addMessage();
        System.out.println("chain valid? " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(rChain);
        System.out.println("current blockchain");
        System.out.println(blockchainJson);

         */
    }


    //deprecated
    public static void genesisBlock(){

    }


    //works through all transactions on the chain to make sure UTXO list is correct
    public static boolean fullChainValidity(){
        return false;
    }

    public static boolean isChainValid(){
        block currentBlock;
        block prevBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, txnOut> tempUTXOs = new HashMap<String, txnOut>(); // temp list of working txn at a given block state
        //tempUTXOs.put(genesisTxn.ouptuts)

        for(int i = 1; i < rChain.size(); i++) {
            currentBlock = rChain.get(i);
            prevBlock = rChain.get(i - 1);

            //checks if current rohChain.rohChain.block hash equals registered rohChain.rohChain.block hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("current hashes not equal");
                return false;
            }
            //compares previous hash and and registered previous hash (prevHash of current rohChain.rohChain.block)
            if (!prevBlock.hash.equals(currentBlock.prevHash)) {
                System.out.println("previous hash not equal");
                return false;
            }
            //checks if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println(currentBlock + " hasn't been hashed yet");
                return false;
            }


            //loop through blockchain txns
            txnOut tempOut;
            for (int t = 0; t < currentBlock.txns.size(); t++) {
                txn currentTxn = currentBlock.txns.get(t);

                if(!currentTxn.verifySignature()){
                    System.out.println("# signature on txn(" + t + ") is invalid");
                    return false;
                }
                if (currentTxn.getInputsValue() != currentTxn.getOutputsValue()){
                    System.out.println("#inputs are not equal to outputs in txn(" + t + ")");
                    return false;
                }

                for(txnIn input: currentTxn.inputs){
                    tempOut = tempUTXOs.get(input.txnOutId);

                    if(tempOut == null){
                        System.out.println("#referenced input on txn(" + t + ") is missing");
                        return false;
                    }
                    if(input.UTXO.value != tempOut.value){
                        System.out.println("#referenced input txn(" + ") value is invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.txnOutId);

                }

                for(txnOut output: currentTxn.outputs){
                    tempUTXOs.put(output.id, output);
                }

                if(currentTxn.outputs.get(0).recipient != currentTxn.recipient){
                    System.out.println("#txn(" + t + ") output recipient is not the intended recipient");
                    return false;
                }
                if(currentTxn.outputs.get(1).recipient != currentTxn.sender){
                    System.out.println("#txn(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }

        }
        System.out.println("blockchain is valid");
        return true;
    }


    public static void addBlock(block newBlock){
        newBlock.mineBlock(difficulty);
        rChain.add(newBlock);
    }






}
